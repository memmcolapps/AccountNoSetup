package org.example.newaccountnogenerator.controller;

import org.example.newaccountnogenerator.dto.AccountGenerationRequest;
import org.example.newaccountnogenerator.dto.GenerateRequest;
import org.example.newaccountnogenerator.model.CustomerNew;
import org.example.newaccountnogenerator.security.ProtectedEndpoint;
import org.example.newaccountnogenerator.service.CreateCustomerNewSetUpService;
import org.example.newaccountnogenerator.service.GenerateAccountNumberService;
import org.example.newaccountnogenerator.service.UnusedAccountNoAvailableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/AccountGenerator/webresources")
public class CreateCustomerNewSetUpController {

    private final CreateCustomerNewSetUpService createCustomerNewSetUpService;
    private final UnusedAccountNoAvailableService unusedAccountNoSelectedService;
    private final GenerateAccountNumberService accountNumberService;

    @Autowired
    public CreateCustomerNewSetUpController(CreateCustomerNewSetUpService createCustomerNewSetUpService,
                                            GenerateAccountNumberService accountNumberService,
                                            UnusedAccountNoAvailableService unusedAccountNoSelectedService) {
        this.createCustomerNewSetUpService = createCustomerNewSetUpService;
        this.accountNumberService = accountNumberService;
        this.unusedAccountNoSelectedService = unusedAccountNoSelectedService;
    }

    @PostMapping("/save/customer/{accessGroupCode}/{token}")
    @ProtectedEndpoint
    public ResponseEntity<?> createCustomer(@RequestBody CustomerNew request,
                                            @PathVariable("accessGroupCode") String accessGroupCode,
                                            @PathVariable("token") String token) {
        try {

            ResponseEntity<Map<String, Object>> result = createCustomerNewSetUpService.createCustomerNewSetUp(request);
            return ResponseEntity.ok(result.getBody());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/account/unused/{accessGroupCode}/{token}")
    @ProtectedEndpoint
    public ResponseEntity<Map<String, Object>> UnusedAccountNoAvailable(@RequestBody GenerateRequest accountReq,
                                                                        @PathVariable("accessGroupCode") String accessGroupCode,
                                                                        @PathVariable("token") String token) {
        try {

            ResponseEntity<Map<String, Object>> result = unusedAccountNoSelectedService.getUnusedAccountNumbers(accountReq);
            return ResponseEntity.ok(result.getBody());
        } catch (Exception e) {
            return handleException(e);
        }

    }

    @PostMapping("/account/generate/{accessGroupCode}/{token}")
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

