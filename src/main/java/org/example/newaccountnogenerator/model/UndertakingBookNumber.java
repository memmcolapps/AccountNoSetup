package org.example.newaccountnogenerator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class UndertakingBookNumber {

    @Id
    @Column(name = "Booknumber", nullable = false)
    private String bookNumber;

    @Column(name = "UTID")
    private String utid;

    private String billingCycle;
    private BigDecimal energyUsed;
    private BigDecimal energyBilled;
    private BigDecimal billingEfficiency;
    private Integer numberOfCustomer;
    private Integer numberOfBilledCustomers;
    private Integer numberOfActiveCustomers;
    private UUID marketerId;
    private Boolean energyUsedEditable;

    @Transient
    private BigDecimal energyLeft;

    private Boolean isMD;
    private String buid;
    private String distributionId;
    private String transId;
    private UUID rowGuid;

    @Column(name = "Cycle")
    private Integer cycle;

    @Column(name = "BankName")
    private String bankName;

    @Column(name = "BankAcctNo")
    private String bankAcctNo;

    @Transient
    private Integer numberOfDirectCustomers;

    public UndertakingBookNumber() {
        this.energyUsedEditable = false;
        this.energyLeft = BigDecimal.ZERO;
        this.isMD = false;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public void setUtid(String utid) {
        this.utid = utid;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public void setEnergyUsed(BigDecimal energyUsed) {
        this.energyUsed = energyUsed;
    }

    public void setEnergyBilled(BigDecimal energyBilled) {
        this.energyBilled = energyBilled;
    }

    public void setBillingEfficiency(BigDecimal billingEfficiency) {
        this.billingEfficiency = billingEfficiency;
    }

    public void setNumberOfCustomer(Integer numberOfCustomer) {
        this.numberOfCustomer = numberOfCustomer;
    }

    public void setNumberOfBilledCustomers(Integer numberOfBilledCustomers) {
        this.numberOfBilledCustomers = numberOfBilledCustomers;
    }

    public void setNumberOfActiveCustomers(Integer numberOfActiveCustomers) {
        this.numberOfActiveCustomers = numberOfActiveCustomers;
    }

    public void setMarketerId(UUID marketerId) {
        this.marketerId = marketerId;
    }

    public void setEnergyUsedEditable(Boolean energyUsedEditable) {
        this.energyUsedEditable = energyUsedEditable;
    }

    public void setEnergyLeft(BigDecimal energyLeft) {
        this.energyLeft = energyLeft;
    }

    public void setMD(Boolean MD) {
        isMD = MD;
    }

    public void setBuid(String buid) {
        this.buid = buid;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public void setRowGuid(UUID rowGuid) {
        this.rowGuid = rowGuid;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }

    public void setNumberOfDirectCustomers(Integer numberOfDirectCustomers) {
        this.numberOfDirectCustomers = numberOfDirectCustomers;
    }
}
