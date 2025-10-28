package org.example.newaccountnogenerator.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.newaccountnogenerator.dto.AccountGenerationRequest;
import org.example.newaccountnogenerator.dto.GenerateRequest;
import org.example.newaccountnogenerator.model.CustomerNew;
import org.example.newaccountnogenerator.security.ProtectedEndpoint;
import org.example.newaccountnogenerator.service.CreateCustomerNewSetUpService;
import org.example.newaccountnogenerator.service.GenerateAccountNumberService;
import org.example.newaccountnogenerator.service.UnusedAccountNoAvailableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/AccountGenerator/webresources/account")
public class CreateCustomerNewSetUpController {

    private static final Logger log = LoggerFactory.getLogger(CreateCustomerNewSetUpController.class);

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

    @GetMapping("/unused/{accessGroupCode}/{token}")
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

    @PostMapping("/generate/{accessGroupCode}/{token}")
    @ProtectedEndpoint
    public ResponseEntity<Map<String, Object>> generateAccountNo(@RequestBody AccountGenerationRequest request,
                                                                 @PathVariable("accessGroupCode") String accessGroupCode,
                                                                 @PathVariable("token") String token) {
        try {
            ResponseEntity<Map<String, Object>> result = accountNumberService.generateAccountNo(request);
            return ResponseEntity.ok(result.getBody());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", -1);
        response.put("message", "An unexpected error occurred: " + e.getMessage());
        log.error("Error: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Health check endpoint to verify the service is up.
     * Shows both system IP and client IP.
     */
    @GetMapping("/api/ping")
    public Map<String, Object> ping(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        String clientIp = getClientIp(request);
        String systemIp = getSystemIp();

        response.put("status", "OK");
        response.put("message", "Customer setup service is alive ✅");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("clientIp", clientIp);
        response.put("systemIp", systemIp);

        log.info("Ping request: clientIp={}, systemIp={}", clientIp, systemIp);

        return response;
    }

    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "X-Real-IP"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }

    private String getSystemIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
