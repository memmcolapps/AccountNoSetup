package org.example.newaccountnogenerator.Service;

import org.example.newaccountnogenerator.Model.CustomerAccountNoGenerated;
import org.example.newaccountnogenerator.Model.UndertakingBookNumber;
import org.example.newaccountnogenerator.Repository.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountNumberService {

    private final AccountNoRepository accountNoRepository;
    private final NumbersRepository numbersRepository;
    private final BusinessUnitRepository businessUnitRepository;
    public final UndertakingBookNumberRepository undertakingBookNumberRepository;
    public final UndertakingRepository undertakingRepository;
    public final DistributionSubstationRepository distributionSubstationRepository;



    public AccountNumberService(AccountNoRepository accountNoRepository, NumbersRepository numbersRepository,
                                BusinessUnitRepository businessUnitRepository,
                                UndertakingBookNumberRepository undertakingBookNumberRepository,
                                UndertakingRepository undertakingRepository,
                                DistributionSubstationRepository distributionSubstationRepository) {
        this.accountNoRepository = accountNoRepository;
        this.numbersRepository = numbersRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.undertakingBookNumberRepository = undertakingBookNumberRepository;
        this.undertakingRepository = undertakingRepository;
        this.distributionSubstationRepository = distributionSubstationRepository;
    }

    public ResponseEntity<Map<String, Object>> generateAccountNo(String utid, String buid, String dssID, String assetId) {

        Map<String, Object> response = new HashMap<>();

        if (utid == null || utid.isEmpty() || buid == null || buid.isEmpty() || dssID == null || dssID.isEmpty()
                || assetId == null || assetId.isEmpty()) {

            response.put("success", false);
            response.put("message", "All the parameters are required");

            return ResponseEntity.badRequest().body(response);
        }

        if (!businessUnitRepository.existsByBuid(buid)){
            response.put("success", false);
            response.put("message", "Business Unit does not exist");
            return ResponseEntity.badRequest().body(response);
        }

        List<CustomerAccountNoGenerated> unusedAccountNo = accountNoRepository.findAccountNoByStatusAndBUIDAndUtid(false,buid,utid);
        if(!unusedAccountNo.isEmpty()){

            List<String> accountNumbers = unusedAccountNo.stream()
                    .map(CustomerAccountNoGenerated::getAccountNo)
                    .collect(Collectors.toList());

            response.put("code", 0);
            response.put("message", "There is Available Unused AccountNo");
            response.put("accountNo", accountNumbers);

            return ResponseEntity.ok(response);
        }

        distributionSubstationRepository.findByDistributionIDAndBuid(dssID, buid).
                orElseThrow(() -> new RuntimeException("Distribution substation not found for provided BUID"));

        distributionSubstationRepository.findByFeederIDAndBuid(assetId, buid).
                orElseThrow(() -> new RuntimeException("Asset ID not found for the provided BUID"));

        undertakingRepository.findUndertakingByBuidAndUtid(buid,utid).
                orElseThrow(() -> new RuntimeException("Undertaking ID not found for the provided BUID"));

        try {

            String randomTwoDigit = String.format("%02d", new Random().nextInt(99) + 1);

            String bookNo = utid +'/'+ randomTwoDigit;

            if (!undertakingBookNumberRepository.existsByBookNumber(bookNo)) {

                UndertakingBookNumber bookInfo = new UndertakingBookNumber();

                bookInfo.setBookNumber(bookNo);
                bookInfo.setUtid(bookNo.substring(0, 5));
                bookInfo.setBillingCycle(bookNo.substring(0,5));
                bookInfo.setBillingEfficiency(BigDecimal.valueOf(78.00));
                bookInfo.setBuid(buid);
                bookInfo.setRowGuid(UUID.randomUUID());

                undertakingBookNumberRepository.save(bookInfo);

            }

            List<String> serialsNo = accountNoRepository.findTopSerialByBookNo(bookNo);
            String series = serialsNo.isEmpty() ? "000" : serialsNo.get(0);

            if (series == null) {
                series = "000";
            }

            if ("999".equals(series)) {
                List<Integer> availableNumbers = numbersRepository.findAvailableNumbers(bookNo);

                if (!availableNumbers.isEmpty()) {
                    series = String.format("%03d", availableNumbers.get(0));
                } else {
                    response.put("code", 1);
                    response.put("message", "No Available Series Found");
                    return ResponseEntity.badRequest().body(response);
                }
            } else if (Integer.parseInt(series) <= 998) {
                // Increment the current series
                int iSeries = Integer.parseInt(series) + 1;
                series = String.format("%03d", iSeries);
            } else {
                response.put("code", 1001);
                response.put("message", "Invalid series number");
                return ResponseEntity.badRequest().body(response);
            }

            String accountPart = bookNo + "/" + series;

            String accountTemp = accountPart.replace("/", "");

            int check = 0;
            for (int i = 0; i < accountTemp.length(); i++) {
                int digit = Character.getNumericValue(accountTemp.charAt(i));
                check += digit * (i + 1);
            }

            int checkDigit = check % 10;
            String accountNumber = accountPart + checkDigit + "-01";

            // Validate account number length
            if (accountNumber.length() != 16) {
                response.put("success", false);
                response.put("message", "Generated invalid account number length");
                return ResponseEntity.badRequest().body(response);
            }

            CustomerAccountNoGenerated account = new CustomerAccountNoGenerated();

            account.setBookNo(bookNo);
            account.setSerialNo(series);
            account.setAccountNo(accountNumber);
            account.setDateGenerated(Date.valueOf(LocalDate.now()));
            account.setStatus(true);
            account.setbUID(buid);
            account.setRowguid(UUID.randomUUID());

            accountNoRepository.save(account);

            response.put("code", 1002);
            response.put("message", "Account Generated Successfully");
            response.put("accountNo", accountNumber);
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            Map<String, Object> errorDetail = new HashMap<>();

            errorDetail.put("timestamp", LocalDate.now().toString());
            errorDetail.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorDetail.put("error", "Invalid number format in series");
            errorDetail.put("message", e.getMessage());
            errorDetail.put("path", "/api/generate-account");


            response.put("code", 1004);
            response.put("errorDetails", errorDetail);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> errorDetail = new HashMap<>();

            errorDetail.put("timestamp", LocalDate.now().toString());
            errorDetail.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorDetail.put("error", "Error generating account number");
            errorDetail.put("message", e.getMessage());
            errorDetail.put("path", "/api/generate-account");

            response.put("code", 1003);
            response.put("errorDetails", errorDetail);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}