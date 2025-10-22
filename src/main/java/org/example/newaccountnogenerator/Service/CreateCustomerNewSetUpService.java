package org.example.newaccountnogenerator.Service;

import org.example.newaccountnogenerator.Entity.AuditLog;
import org.example.newaccountnogenerator.Entity.CustomerAccountNoGenerated;
import org.example.newaccountnogenerator.Entity.CustomerNew;
import org.example.newaccountnogenerator.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CreateCustomerNewSetUpService {

    private static final Logger logger = LoggerFactory.getLogger(CreateCustomerNewSetUpService.class);

    private final CustomerNewRepository customerNewRepository;
    private final AccountNoRepository accountNoRepository;
    private final BusinessUnitRepository businessUnitRepository;
    private final UndertakingRepository undertakingRepository;
    private final DistributionSubstationRepository distributionSubstationRepository;
    private final TariffRepository tariffRepository;
    private final AuditLogRepository auditLogRepository;

    public CreateCustomerNewSetUpService(CustomerNewRepository customerNewRepository,
                                         AccountNoRepository accountNoRepository,
                                         BusinessUnitRepository businessUnitRepository,
                                         UndertakingRepository undertakingRepository,
                                         DistributionSubstationRepository distributionSubstationRepository,
                                         TariffRepository tariffRepository,
                                         AuditLogRepository auditLogRepository) {
        this.customerNewRepository = customerNewRepository;
        this.accountNoRepository = accountNoRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.undertakingRepository = undertakingRepository;
        this.distributionSubstationRepository = distributionSubstationRepository;
        this.tariffRepository = tariffRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public ResponseEntity<Map<String, Object>> createCustomerNewSetUp(CustomerNew customerNew) {
        Map<String, Object> response = new HashMap<>();

        try {
            String accountNumber = customerNew.getAccountNo();
            String buid = customerNew.getBuid();
            String dssid = customerNew.getDistributionId();
            int tariffId = customerNew.getTariffId();
            String operatorName = customerNew.getOperatorName();
            LocalDateTime timestamp = LocalDateTime.now();
            String utid = accountNumber.substring(0,5);
            String bookNumber = accountNumber.substring(0,8);
            String transId = accountNumber.substring(0,5);

            // === Check if Customer Already Exists ===
            CustomerNew existingCustomer = customerNewRepository.findByAccountNo(accountNumber);
            boolean accountExists = accountNoRepository.existsByAccountNoAndBUID(accountNumber, buid);

            if (existingCustomer != null) {
                if (accountExists) {
                    accountNoRepository.updateStatusToTrueByAccountNoAndBuid(accountNumber, buid);
                }
                return buildError("Account number is already assigned to an existing customer.", HttpStatus.CONFLICT);
            }

            // === Validate Referential Integrity ===
            undertakingRepository.findUndertakingByBuidAndUtid(buid, utid)
                    .orElseThrow(() -> new RuntimeException("Undertaking ID not found for the provided BUID."));

            if (!accountExists) {
                return buildError("The provided account number does not exist or was not generated for the specified business unit.", HttpStatus.BAD_REQUEST);
            }

            if (!distributionSubstationRepository.existsByDistributionID(dssid)) {
                return buildError("Invalid Distribution ID.", HttpStatus.BAD_REQUEST);
            }

            if (!businessUnitRepository.existsByBuid(buid)) {
                return buildError("Invalid Business Unit.", HttpStatus.BAD_REQUEST);
            }

            if (!tariffRepository.existsByTariffId(tariffId)) {
                return buildError("Invalid Tariff ID.", HttpStatus.BAD_REQUEST);
            }

            // === Populate and Save Customer Record ===
            customerNew.setStatusCode("A");
            customerNew.setAdc(50);
            customerNew.setStoredAverage(BigDecimal.valueOf(50.0));
            customerNew.setBulk(false);
            customerNew.setCapmi(false);
            customerNew.setConfirmed(true);
            customerNew.setBackBalance(BigDecimal.ZERO);
            customerNew.setOperatorEdit(operatorName);
            customerNew.setOperatorEdits(operatorName);
            customerNew.setVat(true);
            customerNew.setApplicationDate(timestamp);
            customerNew.setSetUpDate(timestamp);
            customerNew.setConnectDate(timestamp);
            customerNew.setNewSetupDate(timestamp);
            customerNew.setTransId(transId);
            customerNew.setUtid(utid);
            customerNew.setAddress1(customerNew.getServiceAddress1());
            customerNew.setAddress2(customerNew.getServiceAddress2());
            customerNew.setCity(customerNew.getServiceAddressCity());
            customerNew.setState(customerNew.getServiceAddressState());
            customerNew.setBookNumber(bookNumber);
            customerNew.setNewSetUpStatus(false);
            customerNew.setCustomerId(UUID.randomUUID());
            customerNew.setRowGuid(UUID.randomUUID());

            customerNewRepository.save(customerNew);
            accountNoRepository.updateStatusToTrueByAccountNoAndBuid(accountNumber, buid);

            // === Record Audit Trail ===
            AuditLog auditLog = new AuditLog();
            auditLog.setLogId(UUID.randomUUID());
            auditLog.setModule(8);
            auditLog.setTableName(CustomerNew.class.getSimpleName());
            auditLog.setKeyValue(accountNumber);
            auditLog.setFieldName("AccountNo");
            auditLog.setChangeFrom("");
            auditLog.setChangeTo("");
            auditLog.setOperator(operatorName);
            auditLog.setBuid(buid);
            auditLog.setRowguid(UUID.randomUUID());
            auditLogRepository.save(auditLog);

            // === Success Response ===
            response.put("code", 200);
            response.put("message", "Customer registered successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error creating new customer setup: {}", e.getMessage(), e);
            return buildDetailedError("Error creating customer new setup", e);
        }
    }

    // === Reusable Error Builders ===
    private ResponseEntity<Map<String, Object>> buildError(String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", status.value());
        error.put("message", message);
        error.put("timestamp", LocalDate.now().toString());
        error.put("status", status.value());
        return ResponseEntity.status(status).body(error);
    }

    private ResponseEntity<Map<String, Object>> buildDetailedError(String title, Exception e) {
        Map<String, Object> error = new HashMap<>();
        Map<String, Object> details = new HashMap<>();

        details.put("timestamp", LocalDate.now().toString());
        details.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        details.put("error", title);
        details.put("message", e.getMessage());
        details.put("path", "/customer/new-setup/service/create");

        error.put("code", 1003);
        error.put("errorDetails", details);
        return ResponseEntity.internalServerError().body(error);
    }
}
