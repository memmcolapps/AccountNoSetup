package org.example.newaccountnogenerator.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Tariff")
public class Tariff {

    @Id
    @Column(name = "TariffID", nullable = false)
    private Integer tariffId;

    @Column(name = "TariffCode", length = 5)
    private String tariffCode;

    @Column(name = "AccountType", length = 50)
    private String accountType;

    @Column(name = "Description", length = 50, nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AddedDate")
    private Date addedDate;

    @Column(name = "StoredAverage")
    private Integer storedAverage;

    @Column(name = "isMD")
    private Boolean isMD;

    @Column(name = "usageratio", precision = 5, scale = 1)
    private BigDecimal usageRatio;

    @Column(name = "rowguid", nullable = false)
    private UUID rowGuid;

    @Column(name = "NewTariffCode", length = 5)
    private String newTariffCode;

    // Constructors
    public Tariff() {}

    // Getters and Setters
    public Integer getTariffId() {
        return tariffId;
    }

    public void setTariffId(Integer tariffId) {
        this.tariffId = tariffId;
    }

    public String getTariffCode() {
        return tariffCode;
    }

    public void setTariffCode(String tariffCode) {
        this.tariffCode = tariffCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Integer getStoredAverage() {
        return storedAverage;
    }

    public void setStoredAverage(Integer storedAverage) {
        this.storedAverage = storedAverage;
    }

    public Boolean getIsMD() {
        return isMD;
    }

    public void setIsMD(Boolean isMD) {
        this.isMD = isMD;
    }

    public BigDecimal getUsageRatio() {
        return usageRatio;
    }

    public void setUsageRatio(BigDecimal usageRatio) {
        this.usageRatio = usageRatio;
    }

    public UUID getRowGuid() {
        return rowGuid;
    }

    public void setRowGuid(UUID rowGuid) {
        this.rowGuid = rowGuid;
    }

    public String getNewTariffCode() {
        return newTariffCode;
    }

    public void setNewTariffCode(String newTariffCode) {
        this.newTariffCode = newTariffCode;
    }
}
