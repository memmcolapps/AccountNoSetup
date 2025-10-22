package org.example.newaccountnogenerator.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.newaccountnogenerator.DTO.GenerateRequest;
import org.example.newaccountnogenerator.Repository.AccessGroupRepository;
import org.example.newaccountnogenerator.Security.ProtectedEndpoint;
import org.example.newaccountnogenerator.Service.UnusedAccountNoAvailableService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/unused/account-number/service")
public class UnusedAccountNoAvailableController {

    private final UnusedAccountNoAvailableService unusedAccountNoSelectedService;

    public UnusedAccountNoAvailableController(UnusedAccountNoAvailableService unusedAccountNoSelectedService) {
        this.unusedAccountNoSelectedService = unusedAccountNoSelectedService;
    }

    @GetMapping("/get/{accessGroupCode}/{token}")
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

    private ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();

        response.put("code", -1);
        response.put("message", "An unexpected error occurred: " + e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
