package org.example.newaccountnogenerator.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "AccessGroup")
public class AccessGroup {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "AccessGroup")
    private String accessGroup;

    @Column(name = "Undertaking")
    private String undertaking;

    @Column(name = "Active")
    private Boolean active;

    @Column(name = "AccessGroupCode")
    private String accessGroupCode;

    @Column(name = "PaymentType")
    private Integer paymentType;

    @Column(name = "RequestIp")
    private String requestIp;

    @Column(name = "escrowbalance", precision = 18, scale = 2)
    private BigDecimal escrowBalance;

    @Column(name = "token")
    private String token;

    @Column(name = "datetokengenerated")
    private LocalDateTime dateTokenGenerated;

    @Column(name = "RequestIP2")
    private String requestIp2;

    @Column(name = "rowguid")
    private UUID rowGuid;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccessGroup() {
        return accessGroup;
    }

    public void setAccessGroup(String accessGroup) {
        this.accessGroup = accessGroup;
    }

    public String getUndertaking() {
        return undertaking;
    }

    public void setUndertaking(String undertaking) {
        this.undertaking = undertaking;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAccessGroupCode() {
        return accessGroupCode;
    }

    public void setAccessGroupCode(String accessGroupCode) {
        this.accessGroupCode = accessGroupCode;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public BigDecimal getEscrowBalance() {
        return escrowBalance;
    }

    public void setEscrowBalance(BigDecimal escrowBalance) {
        this.escrowBalance = escrowBalance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDateTokenGenerated() {
        return dateTokenGenerated;
    }

    public void setDateTokenGenerated(LocalDateTime dateTokenGenerated) {
        this.dateTokenGenerated = dateTokenGenerated;
    }

    public String getRequestIp2() {
        return requestIp2;
    }

    public void setRequestIp2(String requestIp2) {
        this.requestIp2 = requestIp2;
    }

    public UUID getRowGuid() {
        return rowGuid;
    }

    public void setRowGuid(UUID rowGuid) {
        this.rowGuid = rowGuid;
    }
}

