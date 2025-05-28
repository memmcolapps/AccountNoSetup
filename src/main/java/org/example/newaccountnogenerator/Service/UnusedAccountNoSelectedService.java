package org.example.newaccountnogenerator.Service;

import org.example.newaccountnogenerator.Model.CustomerAccountNoGenerated;
import org.example.newaccountnogenerator.Repository.AccountNoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UnusedAccountNoSelectedService {

    private final AccountNoRepository accountNoRepository;

    public UnusedAccountNoSelectedService(AccountNoRepository accountNoRepository) {
        this.accountNoRepository = accountNoRepository;
    }

    public ResponseEntity<Map<String, Object>> UnusedAccountNoSelected(String accountNo) {
        Map<String, Object> response = new HashMap<>();

        Optional<CustomerAccountNoGenerated> existingAccountNo = accountNoRepository.findByAccountNo(accountNo);

        if (existingAccountNo.isPresent()) {

            CustomerAccountNoGenerated accountToUpdate = existingAccountNo.get();

            accountToUpdate.setStatus(true);
            accountNoRepository.save(accountToUpdate);

            response.put("code", 0);
            response.put("accountNo", accountNo);
            response.put("message", "Account Number Selected");

            return ResponseEntity.ok(response);

        } else {

            response.put("code", 1);
            response.put("message", "Account Not Found");
            return ResponseEntity.ok(response);
        }
    }


}
