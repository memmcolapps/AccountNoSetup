package org.example.newaccountnogenerator.Service;

import org.example.newaccountnogenerator.Entity.CustomerAccountNoGenerated;
import org.example.newaccountnogenerator.Entity.UndertakingBookNumber;
import org.example.newaccountnogenerator.Repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class GenerateAccountNumberService {

    private final AccountNoRepository accountNoRepository;
    private final NumbersRepository numbersRepository;
    private final BusinessUnitRepository businessUnitRepository;
    private final UndertakingBookNumberRepository undertakingBookNumberRepository;
    private final UndertakingRepository undertakingRepository;
    private final DistributionSubstationRepository distributionSubstationRepository;
    private final CustomerNewRepository customerNewRepository;

    public GenerateAccountNumberService(
            AccountNoRepository accountNoRepository,
            NumbersRepository numbersRepository,
            BusinessUnitRepository businessUnitRepository,
            UndertakingBookNumberRepository undertakingBookNumberRepository,
            UndertakingRepository undertakingRepository,
            DistributionSubstationRepository distributionSubstationRepository,
            CustomerNewRepository customerNewRepository) {

        this.accountNoRepository = accountNoRepository;
        this.numbersRepository = numbersRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.undertakingBookNumberRepository = undertakingBookNumberRepository;
        this.undertakingRepository = undertakingRepository;
        this.distributionSubstationRepository = distributionSubstationRepository;
        this.customerNewRepository = customerNewRepository;
    }

    public ResponseEntity<Map<String, Object>> generateAccountNo(String utid, String buid, String dssID, String assetId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // === Input validation ===
            if (utid == null || utid.isEmpty() || buid == null || buid.isEmpty()
                    || dssID == null || dssID.isEmpty() || assetId == null || assetId.isEmpty()) {
                return buildError("All parameters (UTID, BUID, DSS_ID, AssetId) are required.", 400);
            }

            if (!businessUnitRepository.existsByBuid(buid)) {
                return buildError("Business Unit not found for provided BUID.", 404);
            }

            // === Validate DSS and Undertaking ===
            distributionSubstationRepository.findByDistributionIDAndBuid(dssID, buid)
                    .orElseThrow(() -> new RuntimeException("Distribution substation not found for provided BUID."));

            distributionSubstationRepository.findByDistributionIDAndBuidOrUtid(dssID, buid, assetId)
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

            // === Save generated account number ===
            CustomerAccountNoGenerated account = new CustomerAccountNoGenerated();
            account.setBookNo(bookNo);
            account.setSerialNo(series);
            account.setAccountNo(accountNumber);
            account.setDateGenerated(Date.valueOf(LocalDate.now()));
            account.setStatus(false);
            account.setUtid(utid);
            account.setbUID(buid);
            account.setDssId(dssID);
            account.setAssetId(assetId);
            account.setRowguid(UUID.randomUUID());

            accountNoRepository.save(account);

            response.put("code", 200);
            response.put("message", "Account number generated successfully. Obtain supervisor approval to finalize registration.");
            response.put("accountNo", accountNumber);
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            return buildError("Invalid number format in series: " + e.getMessage(), 500);
        } catch (Exception e) {
            return buildError("Error generating account number: " + e.getMessage(), 500);
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
