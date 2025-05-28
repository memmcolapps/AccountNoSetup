package org.example.newaccountnogenerator.Controller;

import org.example.newaccountnogenerator.DTO.AccountGenerationRequest;
import org.example.newaccountnogenerator.Service.AccountNumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AccountNoController {

    private final AccountNumberService accountNumberService;

    public AccountNoController(AccountNumberService accountNumberService) {
        this.accountNumberService = accountNumberService;
    }

    @PostMapping("/generate/account-number")
    public ResponseEntity<Map<String, Object>> generateAccountNo(@RequestBody AccountGenerationRequest request) {

        return accountNumberService.generateAccountNo(request.getUtid(),
                request.getBuid(), request.getDssId(), request.getAssetId());

    }

}
