package org.example.newaccountnogenerator.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.newaccountnogenerator.DTO.AccountRequest;
import org.example.newaccountnogenerator.Service.UnusedAccountNoAvailableService;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/update/unused")
public class UnusedAccountNoAvailableController {

    private final UnusedAccountNoAvailableService unusedAccountNoSelectedService;

    public UnusedAccountNoAvailableController(UnusedAccountNoAvailableService unusedAccountNoSelectedService) {
        this.unusedAccountNoSelectedService = unusedAccountNoSelectedService;
    }

    @PostMapping("/account-number")
    public ResponseEntity<Map<String, Object>> UpdateUnusedAccountNoSelected(@RequestBody AccountRequest accountReq) {

        String accountNo = accountReq.getAccountNo();

        return unusedAccountNoSelectedService.UnusedAccountNoSelected(accountNo);
    }
}
