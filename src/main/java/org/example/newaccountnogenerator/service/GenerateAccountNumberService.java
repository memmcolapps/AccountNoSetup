package org.example.newaccountnogenerator.service;

import org.example.newaccountnogenerator.dto.AccountGenerationRequest;
import org.example.newaccountnogenerator.model.CustomerAccountNoGenerated;
import org.example.newaccountnogenerator.model.UndertakingBookNumber;
import org.example.newaccountnogenerator.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GenerateAccountNumberService {

    private static final Logger logger = LoggerFactory.getLogger(GenerateAccountNumberService.class);

    private final AccountNoRepository accountNoRepository;
    private final NumbersRepository numbersRepository;
    private final BusinessUnitRepository businessUnitRepository;
    private final UndertakingBookNumberRepository undertakingBookNumberRepository;
    private final UndertakingRepository undertakingRepository;
    private final DistributionSubstationRepository distributionSubstationRepository;
    private final CustomerNewRepository customerNewRepository;
    private final Map<String, JdbcTemplate> businessJdbcTemplates;

    public GenerateAccountNumberService(
            AccountNoRepository accountNoRepository,
            NumbersRepository numbersRepository,
            BusinessUnitRepository businessUnitRepository,
            UndertakingBookNumberRepository undertakingBookNumberRepository,
            UndertakingRepository undertakingRepository,
            DistributionSubstationRepository distributionSubstationRepository,
            CustomerNewRepository customerNewRepository,
            Map<String, JdbcTemplate> businessJdbcTemplates) {

        this.accountNoRepository = accountNoRepository;
        this.numbersRepository = numbersRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.undertakingBookNumberRepository = undertakingBookNumberRepository;
        this.undertakingRepository = undertakingRepository;
        this.distributionSubstationRepository = distributionSubstationRepository;
        this.customerNewRepository = customerNewRepository;
        this.businessJdbcTemplates = businessJdbcTemplates;
    }

    /**
     * Generates and replicates an account number in both consolidated and business DBs.
     * Rolls back consolidated if business insert fails.
     */
    @Transactional("consolidatedTxManager")
    public ResponseEntity<Map<String, Object>> generateAccountNo(AccountGenerationRequest request) {
        Map<String, Object> response = new HashMap<>();

        String utid = request.getUtid();
        String buid = request.getBuid();
        String dssid = request.getDssid();
        String assetid = request.getAssetId();

        try {
            // === Input validation ===
            if (utid == null || utid.isEmpty() || buid == null || buid.isEmpty()
                    || dssid == null || dssid.isEmpty() || assetid == null || assetid.isEmpty()) {
                return buildError("All parameters (UTID, BUID, DSS_ID, AssetId) are required.", 400);
            }

            if (!businessUnitRepository.existsByBuid(buid)) {
                return buildError("Business Unit not found for provided BUID.", 404);
            }

            // === Validate DSS and Undertaking ===
            distributionSubstationRepository.findByDistributionIDAndBuid(dssid, buid)
                    .orElseThrow(() -> new RuntimeException("Distribution substation not found for provided BUID."));

            distributionSubstationRepository.findByDistributionIDAndBuidOrUtid(dssid, buid, assetid)
                    .orElseThrow(() -> new RuntimeException("Feeder ID not found for the provided DSS ID under the business hub."));

            undertakingRepository.findUndertakingByBuidAndUtid(buid, utid)
                    .orElseThrow(() -> new RuntimeException("Undertaking not found for provided BUID and UTID."));

            // === Generate book number ===
            String randomTwoDigit = String.format("%02d", new Random().nextInt(99) + 1);
            String bookNo = utid + '/' + randomTwoDigit;

            if (!undertakingBookNumberRepository.existsByBookNumber(bookNo)) {
                UndertakingBookNumber bookInfo = new UndertakingBookNumber();
                bookInfo.setBookNumber(bookNo);
                bookInfo.setUtid(bookNo.substring(0, 5));
                bookInfo.setBillingCycle(bookNo.substring(0, 5));
                bookInfo.setBillingEfficiency(BigDecimal.valueOf(78.00));
                bookInfo.setBuid(buid);
                bookInfo.setRowGuid(UUID.randomUUID());
                undertakingBookNumberRepository.save(bookInfo);
            }

            // === Generate series number ===
            String series = accountNoRepository.findTopSerialByBookNo(bookNo).stream().findFirst().orElse("001");

            if ("999".equals(series)) {
                List<Integer> availableNumbers = numbersRepository.findAvailableNumbers(bookNo);
                if (!availableNumbers.isEmpty()) {
                    series = String.format("%03d", availableNumbers.get(0));
                } else {
                    return buildError("No available series found.", 400);
                }
            } else {
                int nextSeries = Integer.parseInt(series) + 1;
                if (nextSeries > 999) return buildError("Invalid series number.", 400);
                series = String.format("%03d", nextSeries);
            }

            // === Build account number ===
            String accountPart = bookNo + "/" + series;
            String accountTemp = accountPart.replace("/", "");
            int check = 0;

            for (int i = 0; i < accountTemp.length(); i++) {
                int digit = Character.getNumericValue(accountTemp.charAt(i));
                check += digit * (i + 1);
            }

            int checkDigit = check % 10;
            String accountNumber = accountPart + checkDigit + "-01";

            if (accountNumber.length() != 16) {
                return buildError("Generated invalid account number length.", 400);
            }

            if (accountNoRepository.existsByAccountNoAndBUID(accountNumber, buid)) {
                return buildError("Duplicate account number found. Retry.", 409);
            }

            // === Save to Consolidated DB ===
            CustomerAccountNoGenerated account = new CustomerAccountNoGenerated();
            account.setBookNo(bookNo);
            account.setSerialNo(series);
            account.setAccountNo(accountNumber);
            account.setDateGenerated(LocalDateTime.now());
            account.setStatus(false);
            account.setUtid(utid);
            account.setbUID(buid);
            account.setDssId(dssid);
            account.setAssetId(assetid);
            account.setRowguid(UUID.randomUUID());
            accountNoRepository.save(account);

            // === Replicate to Business DB ===
            JdbcTemplate targetJdbc = businessJdbcTemplates.get(buid);

            if (targetJdbc == null) {
                throw new RuntimeException("No JDBC template found for business unit: " + buid);
            }

            String sql = """
                    INSERT INTO CustomerAccountNoGenerated
                    ([BookNo], [SerialNo], [AccountNo], [DateGenerated], [Status], [UTID],
                     [BUID], [DssId], [AssetId], [RowGuid])
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            targetJdbc.update(sql,
                    account.getBookNo(),
                    account.getSerialNo(),
                    account.getAccountNo(),
                    account.getDateGenerated(),
                    account.isStatus(),
                    account.getUtid(),
                    account.getbUID(),
                    account.getDssId(),
                    account.getAssetId(),
                    account.getRowguid()
            );

            // === Success Response ===
            response.put("code", 200);
            response.put("message", "Account number generated successfully and replicated to business DB.");
            response.put("accountNumber", accountNumber);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error generating account number: {}", e.getMessage(), e);
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e); // triggers rollback
        }
    }

    private ResponseEntity<Map<String, Object>> buildError(String message, int status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", status);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", LocalDate.now().toString());
        errorResponse.put("status", status);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
