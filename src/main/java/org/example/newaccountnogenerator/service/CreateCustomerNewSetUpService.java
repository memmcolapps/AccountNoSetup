package org.example.newaccountnogenerator.service;

import org.example.newaccountnogenerator.model.AuditLog;
import org.example.newaccountnogenerator.model.CustomerNew;
import org.example.newaccountnogenerator.model.Tariff;
import org.example.newaccountnogenerator.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final Map<String, JdbcTemplate> businessJdbcTemplates;

    public CreateCustomerNewSetUpService(CustomerNewRepository customerNewRepository,
                                         AccountNoRepository accountNoRepository,
                                         BusinessUnitRepository businessUnitRepository,
                                         UndertakingRepository undertakingRepository,
                                         DistributionSubstationRepository distributionSubstationRepository,
                                         TariffRepository tariffRepository,
                                         AuditLogRepository auditLogRepository,
                                         Map<String, JdbcTemplate> businessJdbcTemplates) {
        this.customerNewRepository = customerNewRepository;
        this.accountNoRepository = accountNoRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.undertakingRepository = undertakingRepository;
        this.distributionSubstationRepository = distributionSubstationRepository;
        this.tariffRepository = tariffRepository;
        this.auditLogRepository = auditLogRepository;
        this.businessJdbcTemplates = businessJdbcTemplates;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "consolidatedTxManager")
    public ResponseEntity<Map<String, Object>> createCustomerNewSetUp(CustomerNew customerNew) {
        Map<String, Object> response = new HashMap<>();
        try {
            String accountNumber = customerNew.getAccountNo();
            String buid = customerNew.getBuid();
            String dssid = customerNew.getDistributionID();
            int tariffId = customerNew.getTariffID();
            String operatorName = customerNew.getAccessGroup();
            LocalDateTime timestamp = LocalDateTime.now();
            String utid = accountNumber.substring(0, 5);
            String bookNumber = accountNumber.substring(0, 8);
            String transId = accountNumber.substring(0, 5);

            CustomerNew existingCustomer = customerNewRepository.findByAccountNo(accountNumber);
            boolean accountExists = accountNoRepository.existsByAccountNoAndBUID(accountNumber, buid);
            String accTypeDesc = tariffRepository.getAccountTypeByTariffId(tariffId);

            if (existingCustomer != null) {
                if (accountExists) {
                    accountNoRepository.updateStatusToTrueByAccountNoAndBuid(accountNumber, buid);
                }
                return buildError("Account number is already assigned to an existing customer.", HttpStatus.CONFLICT);
            }

            undertakingRepository.findUndertakingByBuidAndUtid(buid, utid)
                    .orElseThrow(() -> new RuntimeException("Undertaking ID not found for the provided BUID."));

            if (!accountExists)
                return buildError("The provided account number does not exist or was not generated for the specified business unit.", HttpStatus.BAD_REQUEST);
            if (!distributionSubstationRepository.existsByDistributionID(dssid))
                return buildError("Invalid Distribution ID.", HttpStatus.BAD_REQUEST);
            if (!businessUnitRepository.existsByBuid(buid))
                return buildError("Invalid Business Unit.", HttpStatus.BAD_REQUEST);
            if (!tariffRepository.existsByTariffId(tariffId))
                return buildError("Invalid Tariff ID.", HttpStatus.BAD_REQUEST);

            customerNew.setStatusCode("N");
            customerNew.setUseAdc(true);
            customerNew.setAdc(50);
            customerNew.setStoredAverage(BigDecimal.valueOf(50.0));
            customerNew.setBulk(false);
            customerNew.setCapmi(false);
            customerNew.setConfirmed(true);
            customerNew.setAcctTypeDesc(accTypeDesc);
            customerNew.setBackBalance(BigDecimal.ZERO);
            customerNew.setOperatorEdit(operatorName);
            customerNew.setOperatorEdits(operatorName);
            customerNew.setOperatorName(operatorName);
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
            customerNew.setCustomerId(UUID.randomUUID());
            customerNew.setRowGuid(UUID.randomUUID());

            CustomerNew saved = customerNewRepository.save(customerNew);
            accountNoRepository.updateStatusToTrueByAccountNoAndBuid(accountNumber, buid);

            AuditLog auditLog = new AuditLog();
            auditLog.setLogId(UUID.randomUUID());
            auditLog.setModule(4);
            auditLog.setTableName(CustomerNew.class.getSimpleName());
            auditLog.setKeyValue(accountNumber);
            auditLog.setFieldName("AccountNo");
            auditLog.setChangeFrom("");
            auditLog.setChangeTo("");
            auditLog.setOperator(operatorName);
            auditLog.setBuid(buid);
            auditLog.setRowguid(UUID.randomUUID());
            auditLogRepository.save(auditLog);

            String buId = saved.getBuid();
            JdbcTemplate targetJdbc = businessJdbcTemplates.get(buId);
            if (targetJdbc == null)
                throw new RuntimeException("No JDBC Template found for business unit: " + buId);

            String sql = """
                    INSERT INTO CustomerNew(
                        [AccountNo], [booknumber], [MeterNo], [Surname], [FirstName], [OtherNames], [Address1],
                        [Address2], [City], [State], [email], [ServiceAddress1], [ServiceAddress2], [ServiceAddressCity],
                        [ServiceAddressState], [TariffID], [ArrearsBalance], [Mobile],[AcctTypeDesc], [Vat], [ApplicationDate],
                        [GIScoordinate], [SetUpDate], [ConnectDate], [UTID], [BUID], [TransID], [OperatorName],
                        [StatusCode], [ADC], [StoredAverage],[UseADC], [IsBulk], [DistributionID], [NewsetupDate], [rowguid],
                        [IsCAPMI], [operatorEdits], [operatorEdit], [IsConfirmed], [ConfirmBy], [DateConfirm],
                        [BackBalance], [CustomerID]
                    )
                    VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)                                                                 
                    """;

            int rows = targetJdbc.update(sql,
                    saved.getAccountNo(),
                    saved.getBookNumber(),
                    saved.getMeterNo(),
                    saved.getSurname(),
                    saved.getFirstName(),
                    saved.getOtherNames(),
                    saved.getAddress1(),
                    saved.getAddress2(),
                    saved.getCity(),
                    saved.getState(),
                    saved.getEmail(),
                    saved.getServiceAddress1(),
                    saved.getServiceAddress2(),
                    saved.getServiceAddressCity(),
                    saved.getServiceAddressState(),
                    saved.getTariffID(),
                    saved.getArrears(),
                    saved.getMobile(),
                    saved.getAcctTypeDesc(),
                    saved.getVat(),
                    saved.getApplicationDate(),
                    saved.getGisCoordinate(),
                    saved.getSetUpDate(),
                    saved.getConnectDate(),
                    saved.getUtid(),
                    saved.getBuid(),
                    saved.getTransId(),
                    saved.getOperatorName(),
                    saved.getStatusCode(),
                    saved.getAdc(),
                    saved.getStoredAverage(),
                    saved.getUseAdc(),
                    saved.getBulk(),
                    saved.getDistributionID(),
                    saved.getNewSetupDate(),
                    saved.getRowGuid(),
                    saved.getCapmi(),
                    saved.getOperatorEdits(),
                    saved.getOperatorEdit(),
                    saved.getConfirmed(),
                    saved.getConfirmBy(),
                    saved.getDateConfirm(),
                    saved.getBackBalance(),
                    saved.getCustomerId()
            );

            if (rows <= 0)
                throw new RuntimeException("Replication to business unit failed, transaction will rollback.");


            // ✅ Now update CustomerAccountNoGenerated status
            String updateStatusSql = """
                    UPDATE CustomerAccountNoGenerated
                    SET status = 1
                    WHERE AccountNo = ? AND BUID = ?
                    """;

            int updateCount = targetJdbc.update(updateStatusSql, saved.getAccountNo(), saved.getBuid());
            if (updateCount <= 0) {
                throw new RuntimeException("Failed to update CustomerAccountNoGenerated status on business database.");
            }


            response.put("code", 200);
            response.put("message", "Customer registered successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error creating new customer setup: {}", e.getMessage(), e);
            return buildDetailedError("Error creating customer new setup", e);
        }
    }

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
