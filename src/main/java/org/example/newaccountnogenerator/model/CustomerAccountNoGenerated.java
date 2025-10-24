package org.example.newaccountnogenerator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "CustomerAccountNoGenerated")
public class CustomerAccountNoGenerated {

    @Column(name = "bookNo", length = 8)
    private String bookNo;

    private String serialNo;

    @Id
    @Column(name = "AccountNo")
    private String accountNo;

    private LocalDateTime dateGenerated;
    private boolean status;

    @Column(name = "bUID", length = 3)
    private String bUID;

    private UUID rowguid;

    @Column(name = "Utid")
    private String utid;

    private String dssId;
    private String assetId;

    public String getBookNo() {
        return bookNo;
    }
    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public LocalDateTime getDateGenerated() {
        return dateGenerated;
    }

    public boolean isStatus() {
        return status;
    }

    public String getbUID() {
        return bUID;
    }

    public UUID getRowguid() {
        return rowguid;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setDateGenerated(LocalDateTime dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setbUID(String bUID) {
        this.bUID = bUID;
    }

    public void setRowguid(UUID rowguid) {
        this.rowguid = rowguid;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getUtid() {
        return utid;
    }

    public void setUtid(String utid) {
        this.utid = utid;
    }

    public String getDssId() {
        return dssId;
    }

    public void setDssId(String dssId) {
        this.dssId = dssId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
}
