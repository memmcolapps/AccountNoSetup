package org.example.newaccountnogenerator.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;
import java.util.UUID;


@Entity
@Table(name = "CustomerAccountNoGenerated")
public class CustomerAccountNoGenerated {

    @Column(name = "bookNo", length = 8)
    private String bookNo;

    private String serialNo;

    @Id
    private String accountNo;

    private Date dateGenerated;
    private boolean status;

    @Column(name = "bUID", length = 3)
    private String bUID;

    private UUID rowguid;


    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setDateGenerated(Date dateGenerated) {
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

}
