package org.example.newaccountnogenerator.Controller;

import org.example.newaccountnogenerator.DTO.AccountGenerationRequest;
import org.example.newaccountnogenerator.Repository.AccessGroupRepository;
import org.example.newaccountnogenerator.Security.ProtectedEndpoint;
import org.example.newaccountnogenerator.Service.CreateCustomerNewSetUpService;
import org.example.newaccountnogenerator.Service.GenerateAccountNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/generate/account-number/service")
public class AccountNoController {

    private final GenerateAccountNumberService accountNumberService;
    private final AccessGroupRepository accessGroupRepository;

    @Autowired
    public AccountNoController(GenerateAccountNumberService accountNumberService, AccessGroupRepository accessGroupRepository) {
        this.accountNumberService = accountNumberService;
        this.accessGroupRepository = accessGroupRepository;
    }
    @PostMapping("/get/{accessGroupCode}/{token}")
    @ProtectedEndpoint
    public ResponseEntity<Map<String, Object>> generateAccountNo(@RequestBody AccountGenerationRequest request,
                                                                 @PathVariable("accessGroupCode") String accessGroupCode,
                                                                 @PathVariable("token") String token) {
        try {

            ResponseEntity<Map<String, Object>> result = accountNumberService.generateAccountNo(request.getUtid(),
                    request.getBuid(), request.getDssId(), request.getAssetId());
            return ResponseEntity.ok(result.getBody());
        } catch (Exception e) {
            return handleException(e);
        }

    }

    private ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();

        response.put("code", -1);
        response.put("message", "An unexpected error occurred: " + e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
