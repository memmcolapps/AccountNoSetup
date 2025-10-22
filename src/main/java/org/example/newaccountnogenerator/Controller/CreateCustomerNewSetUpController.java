package org.example.newaccountnogenerator.Controller;

import org.example.newaccountnogenerator.Entity.CustomerNew;
import org.example.newaccountnogenerator.Repository.AccessGroupRepository;
import org.example.newaccountnogenerator.Security.ProtectedEndpoint;
import org.example.newaccountnogenerator.Service.CreateCustomerNewSetUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer/new-setup/service")
public class CreateCustomerNewSetUpController {

    private final CreateCustomerNewSetUpService createCustomerNewSetUpService;

    @Autowired
    public CreateCustomerNewSetUpController(CreateCustomerNewSetUpService createCustomerNewSetUpService) {
        this.createCustomerNewSetUpService = createCustomerNewSetUpService;
    }

    @PostMapping("/create/{accessGroupCode}/{token}")
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

    private ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();

        response.put("code", -1);
        response.put("message", "An unexpected error occurred: " + e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}

