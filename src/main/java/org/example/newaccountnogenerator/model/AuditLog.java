package org.example.newaccountnogenerator.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class AuditLog {

    @Id
    @Column(name = "LogID", nullable = false)
    private UUID logId;

    @Column(name = "Module", nullable = false)
    private int module;

    @Column(name = "TableName", nullable = false)
    private String tableName;

    @Column(name = "KeyValue", nullable = false)
    private String keyValue;

    @Column(name = "FieldName", nullable = false)
    private String fieldName;

    @Column(name = "ChangeFrom", nullable = false)
    private String changeFrom;

    @Column(name = "ChangeTo", nullable = false)
    private String changeTo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DateTime", nullable = false)
    private Date dateTime;

    @Column(name = "Operator", nullable = false)
    private String operator;

    @Column(name = "BUID", nullable = false)
    private String buid;

    private UUID rowguid;

    public AuditLog() {}

    @PrePersist
    protected void onCreate() {
        this.dateTime = new Date();
    }

    public UUID getLogId() {
        return logId;
    }

    public void setLogId(UUID logId) {
        this.logId = logId;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getChangeFrom() {
        return changeFrom;
    }

    public void setChangeFrom(String changeFrom) {
        this.changeFrom = changeFrom;
    }

    public String getChangeTo() {
        return changeTo;
    }

    public void setChangeTo(String changeTo) {
        this.changeTo = changeTo;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getBuid() {
        return buid;
    }

    public void setBuid(String buid) {
        this.buid = buid;
    }

    public UUID getRowguid() {
        return rowguid;
    }

    public void setRowguid(UUID rowguid) {
        this.rowguid = rowguid;
    }
}
