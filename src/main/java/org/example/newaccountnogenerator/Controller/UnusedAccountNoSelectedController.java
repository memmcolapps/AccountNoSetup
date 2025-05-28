package org.example.newaccountnogenerator.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.newaccountnogenerator.DTO.AccountRequest;
import org.example.newaccountnogenerator.Service.UnusedAccountNoSelectedService;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/update")
public class UnusedAccountNoSelectedController {

    private final UnusedAccountNoSelectedService unusedAccountNoSelectedService;

    public UnusedAccountNoSelectedController(UnusedAccountNoSelectedService unusedAccountNoSelectedService) {
        this.unusedAccountNoSelectedService = unusedAccountNoSelectedService;
    }

    @PostMapping("/unused-accountno")
    public ResponseEntity<Map<String, Object>> UpdateUnusedAccountNoSelectedV2(@RequestBody AccountRequest accountReq) {

        String accountNo = accountReq.getAccountNo();

        return unusedAccountNoSelectedService.UnusedAccountNoSelected(accountNo);
    }
}
