package org.example.newaccountnogenerator.Service;

import org.example.newaccountnogenerator.DTO.GenerateRequest;
import org.example.newaccountnogenerator.Repository.AccountNoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UnusedAccountNoAvailableService {

    private final AccountNoRepository accountNoRepository;
    private static final Logger logger = LoggerFactory.getLogger(UnusedAccountNoAvailableService.class);

    public UnusedAccountNoAvailableService(AccountNoRepository accountNoRepository) {
        this.accountNoRepository = accountNoRepository;
    }

    public ResponseEntity<Map<String, Object>> getUnusedAccountNumbers(GenerateRequest generateRequest) {
        Map<String, Object> response = new HashMap<>();

        String buid = generateRequest.getBuid();
        String utid = generateRequest.getUtid();

        try {
            // === Input Validation ===
            if (buid == null || buid.trim().isEmpty() || utid == null || utid.trim().isEmpty()) {
                return buildError("Both BUID and UTID are required parameters.", HttpStatus.BAD_REQUEST);
            }

            // === Fetch Unused Account Numbers ===
            List<String> unusedAccounts = accountNoRepository.findAccountNoStatusZeroByBuidAndUtid(buid, utid);

            if (unusedAccounts.isEmpty()) {
                response.put("code", 404);
                response.put("message", "No available account numbers found for the provided Business Unit and Undertaking.");
                return ResponseEntity.ok(response);
            }

            // === Success ===
            response.put("code", 200);
            response.put("message", "Unused account numbers retrieved successfully.");
            response.put("accountNo", unusedAccounts);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching unused account numbers for BUID: {}, UTID: {}", buid, utid, e);
            return buildError("An error occurred while fetching account numbers: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // === Standardized Error Builder ===
    private ResponseEntity<Map<String, Object>> buildError(String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", status.value());
        error.put("message", message);
        error.put("timestamp", LocalDate.now().toString());
        error.put("status", status.value());
        return ResponseEntity.status(status).body(error);
    }
}
