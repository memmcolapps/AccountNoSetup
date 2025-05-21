package org.example.newaccountnogenerator.Controller;

import org.example.newaccountnogenerator.Service.AccountNumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountNoController {

    private final AccountNumberService accountNumberService;

    public AccountNoController(AccountNumberService accountNumberService) {
        this.accountNumberService = accountNumberService;
    }

    @PostMapping("/generate/account-number")
    public ResponseEntity<String> generateAccountNo(@RequestParam String utid,@RequestParam String buid,
            @RequestParam String dssId,@RequestParam String assetId) {
        return accountNumberService.generateAccountNo(utid, buid, dssId, assetId);

    }

}
