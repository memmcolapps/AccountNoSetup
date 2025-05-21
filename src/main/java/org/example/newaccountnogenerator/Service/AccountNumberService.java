package org.example.newaccountnogenerator.Service;

import org.example.newaccountnogenerator.Model.CustomerAccountNoGenerated;
import org.example.newaccountnogenerator.Model.UndertakingBookNumber;
import org.example.newaccountnogenerator.Repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    public ResponseEntity<String> generateAccountNo(String utid, String buid, String dssID, String assetId) {


        if (utid == null || utid.isEmpty() || buid == null || buid.isEmpty() || dssID == null || dssID.isEmpty()
                || assetId == null || assetId.isEmpty()) {
            return ResponseEntity.badRequest().body("All the parameters are required");
        }

        if (!businessUnitRepository.existsByBuid(buid)){
            return ResponseEntity.badRequest().body("Business Unit Not Found");
        }

        List<CustomerAccountNoGenerated> unusedAccountNo = accountNoRepository.findAccountNoByStatusAndBUID(false,buid);
        if(!unusedAccountNo.isEmpty()){

            CustomerAccountNoGenerated unusedAccountNoGenerated = unusedAccountNo.get(0);

//            unusedAccountNoGenerated.setStatus(true);
            accountNoRepository.save(unusedAccountNoGenerated);

            return ResponseEntity.ok("They are still Available Unused Account Number" + unusedAccountNoGenerated.getAccountNo());
        }

        distributionSubstationRepository.findByDistributionIdAndBuid(dssID, buid).
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
                    return ResponseEntity.badRequest().body("No available Serial number left for bookNo.");
                }
            } else if (Integer.parseInt(series) <= 998) {
                // Increment the current series
                int iSeries = Integer.parseInt(series) + 1;
                series = String.format("%03d", iSeries);
            } else {
                return ResponseEntity.badRequest().body("Invalid series number");
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
                return ResponseEntity.badRequest().body("Generated invalid account number length");
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

            return ResponseEntity.ok(accountNumber);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid number format in series");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating account number: " + e.getMessage());
        }
    }
}