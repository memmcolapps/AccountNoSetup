package org.example.newaccountnogenerator.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "CustomerNew")
public class CustomerNew {

    @Id
    @Column(name = "AccountNo", length = 20, nullable = false)
    private String accountNo;

    @Column(name = "booknumber", length = 10, nullable = false)
    private String bookNumber;

    @Column(name = "oldaccountnumber", length = 20)
    private String oldAccountNumber;

    @Column(name = "MeterNo", length = 20)
    private String meterNo = null;

    @Column(name = "Surname", length = 50, nullable = false)
    private String surname;

    @Column(name = "FirstName", length = 25)
    private String firstName;

    @Column(name = "OtherNames", length = 50)
    private String otherNames;

    @Column(name = "Address1", length = 200)
    private String address1;

    @Column(name = "Address2", length = 200)
    private String address2;

    @Column(name = "City", length = 50)
    private String city;

    @Column(name = "State", length = 50)
    private String state;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "ServiceAddress1", length = 200)
    private String serviceAddress1;

    @Column(name = "ServiceAddress2", length = 200)
    private String serviceAddress2;

    @Column(name = "ServiceAddressCity", length = 50)
    private String serviceAddressCity;

    @Column(name = "ServiceAddressState", length = 50)
    private String serviceAddressState;

    @Column(name = "TariffID", nullable = false)
    private Integer tariffID;

    @Column(name = "ArrearsBalance", precision = 18, scale = 2)
    private BigDecimal arrears = BigDecimal.ZERO;

    @Column(name = "Mobile", length = 20)
    private String mobile;

    @Column(name = "MethodOfIdentification", length = 10)
    private String methodOfIdentification;

    @Column(name = "AcctTypeDesc", length = 50)
    private String acctTypeDesc;

    @Column(name = "ScheduleBillNO")
    private Integer scheduleBillNo;

    @Column(name = "Vat")
    private Boolean vat;

    @Column(name = "ApplicationDate")
    private LocalDateTime applicationDate;

    @Column(name = "PlaceOfWork", length = 50)
    private String placeOfWork;

    @Column(name = "AddressOfOrganisation", length = 40)
    private String addressOfOrganisation;

    @Column(name = "GIScoordinate", length = 20)
    private String gisCoordinate;

    @Column(name = "GuarantorName", length = 50)
    private String guarantorName;

    @Column(name = "GuarantorAddress", length = 50)
    private String guarantorAddress;

    @Column(name = "OrganisationCode", length = 10)
    private String organisationCode;

    @Column(name = "institutionCode", length = 10)
    private String institutionCode;

    @Column(name = "SetUpDate")
    private LocalDateTime setUpDate;

    @Column(name = "ConnectDate")
    private LocalDateTime connectDate;

    @Column(name = "DistributionStation", length = 50)
    private String distributionStation;

    @Column(name = "InjectionStation", length = 10)
    private String injectionStation;

    @Column(name = "UpriserNo")
    private Integer upriserNo;

    @Column(name = "UTID", length = 10, nullable = false)
    private String utid;

    @Column(name = "BUID", length = 5, nullable = false)
    private String buid;

    @Column(name = "TransID", length = 10)
    private String transId;

    @Column(name = "OperatorName", length = 20)
    private String operatorName;

    @Column(name = "Password", length = 40)
    private String password;

    @Column(name = "StatusCode", length = 10, nullable = false)
    private String statusCode;

    @Column(name = "ADC")
    private Integer adc;

    @Column(name = "StoredAverage", precision = 10, scale = 2)
    private BigDecimal storedAverage;

    // Computed in DB: ConnectionType
    @Transient
    private String connectionType;

    @Column(name = "UseADC")
    private Boolean useAdc;

    @Column(name = "IsBulk")
    private Boolean isBulk;

    @Column(name = "DistributionID", length = 50)
    private String distributionID;

    @Column(name = "NewSetupDate")
    private LocalDateTime newSetupDate;

    @Column(name = "rowguid", nullable = false)
    private UUID rowGuid;

    @Column(name = "IsCAPMI")
    private Boolean isCapmi;

    @Column(name = "operatorEdits", length = 20)
    private String operatorEdits;

    @Column(name = "operatorEdit", length = 20)
    private String operatorEdit;

    @Column(name = "CAT", columnDefinition = "CHAR(5)")
    private String cat;

    @Column(name = "IsConfirmed")
    private Boolean isConfirmed;

    @Column(name = "ConfirmBy", length = 30)
    private String confirmBy;

    @Column(name = "DateConfirm")
    private LocalDateTime dateConfirm;

    @Column(name = "NAC", length = 30)
    private String nac;

    @Column(name = "BackBalance", precision = 18, scale = 2)
    private BigDecimal backBalance;

    @Column(name = "GIS", length = 30)
    private String gis;

    @Column(name = "CustomerID")
    private UUID customerId;

    @Transient
    private String accessGroup;

//    @Column(name = "AceCustomerID", length = 50)
//    private String aceCustomerId;
//
//    @Column(name = "CenterID", length = 50)
//    private String centerId;
//
//    @Column(name = "AssetID", length = 50)
//    private String assetId;
//
//    @Column(name = "UpriserID", length = 50)
//    private String upriserId;
//
//    @Column(name = "HasTenant")
//    private Boolean hasTenant;
//
//    @Column(name = "LandLordName", length = 20)
//    private String landLordName;
//
//    @Column(name = "LandLordMobileNo", length = 20)
//    private String landLordMobileNo;
//
//    @Column(name = "CIN", length = 20)
//    private String cin;
//
//    @Column(name = "IsPrepaid")
//    private Boolean isPrepaid;
//
//    @Column(name = "MeterOEM", length = 20)
//    private String meterOem;
//
//    @Column(name = "LTPoleID", length = 30)
//    private String ltPoleId;
//
//    @Column(name = "Gender", length = 10)
//    private String gender;
//
//    @Column(name = "MaritalStatus", length = 10)
//    private String maritalStatus;
//
//    @Column(name = "BusinessType", length = 20)
//    private String businessType;

    @Column(name = "NewSetUpStatus")
    private Boolean newSetUpStatus;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getOldAccountNumber() {
        return oldAccountNumber;
    }

    public void setOldAccountNumber(String oldAccountNumber) {
        this.oldAccountNumber = oldAccountNumber;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceAddress1() {
        return serviceAddress1;
    }

    public void setServiceAddress1(String serviceAddress1) {
        this.serviceAddress1 = serviceAddress1;
    }

    public String getServiceAddress2() {
        return serviceAddress2;
    }

    public void setServiceAddress2(String serviceAddress2) {
        this.serviceAddress2 = serviceAddress2;
    }

    public String getServiceAddressCity() {
        return serviceAddressCity;
    }

    public void setServiceAddressCity(String serviceAddressCity) {
        this.serviceAddressCity = serviceAddressCity;
    }

    public String getServiceAddressState() {
        return serviceAddressState;
    }

    public void setServiceAddressState(String serviceAddressState) {
        this.serviceAddressState = serviceAddressState;
    }

    public Integer getTariffID() {
        return tariffID;
    }

    public void setTariffID(Integer tariffID) {
        this.tariffID = tariffID;
    }

    public BigDecimal getArrears() {
        return arrears;
    }

    public void setArrears(BigDecimal arrearsBalance) {
        this.arrears = arrears;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMethodOfIdentification() {
        return methodOfIdentification;
    }

    public void setMethodOfIdentification(String methodOfIdentification) {
        this.methodOfIdentification = methodOfIdentification;
    }

    public String getAcctTypeDesc() {
        return acctTypeDesc;
    }

    public void setAcctTypeDesc(String acctTypeDesc) {
        this.acctTypeDesc = acctTypeDesc;
    }

    public Integer getScheduleBillNo() {
        return scheduleBillNo;
    }

    public void setScheduleBillNo(Integer scheduleBillNo) {
        this.scheduleBillNo = scheduleBillNo;
    }

    public Boolean getVat() {
        return vat;
    }

    public void setVat(Boolean vat) {
        this.vat = vat;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public String getAddressOfOrganisation() {
        return addressOfOrganisation;
    }

    public void setAddressOfOrganisation(String addressOfOrganisation) {
        this.addressOfOrganisation = addressOfOrganisation;
    }

    public String getGisCoordinate() {
        return gisCoordinate;
    }

    public void setGisCoordinate(String gisCoordinate) {
        this.gisCoordinate = gisCoordinate;
    }

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getGuarantorAddress() {
        return guarantorAddress;
    }

    public void setGuarantorAddress(String guarantorAddress) {
        this.guarantorAddress = guarantorAddress;
    }

    public String getOrganisationCode() {
        return organisationCode;
    }

    public void setOrganisationCode(String organisationCode) {
        this.organisationCode = organisationCode;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public LocalDateTime getSetUpDate() {
        return setUpDate;
    }

    public void setSetUpDate(LocalDateTime setUpDate) {
        this.setUpDate = setUpDate;
    }

    public LocalDateTime getConnectDate() {
        return connectDate;
    }

    public void setConnectDate(LocalDateTime connectDate) {
        this.connectDate = connectDate;
    }

    public String getDistributionStation() {
        return distributionStation;
    }

    public void setDistributionStation(String distributionStation) {
        this.distributionStation = distributionStation;
    }

    public String getInjectionStation() {
        return injectionStation;
    }

    public void setInjectionStation(String injectionStation) {
        this.injectionStation = injectionStation;
    }

    public Integer getUpriserNo() {
        return upriserNo;
    }

    public void setUpriserNo(Integer upriserNo) {
        this.upriserNo = upriserNo;
    }

    public String getUtid() {
        return utid;
    }

    public void setUtid(String utid) {
        this.utid = utid;
    }

    public String getBuid() {
        return buid;
    }

    public void setBuid(String buid) {
        this.buid = buid;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getAdc() {
        return adc;
    }

    public void setAdc(Integer adc) {
        this.adc = adc;
    }

    public BigDecimal getStoredAverage() {
        return storedAverage;
    }

    public void setStoredAverage(BigDecimal storedAverage) {
        this.storedAverage = storedAverage;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public Boolean getUseAdc() {
        return useAdc;
    }

    public void setUseAdc(Boolean useAdc) {
        this.useAdc = useAdc;
    }

    public Boolean getBulk() {
        return isBulk;
    }

    public void setBulk(Boolean bulk) {
        isBulk = bulk;
    }

    public String getDistributionID() {
        return distributionID;
    }

    public void setDistributionID(String distributionID) {
        this.distributionID = distributionID;
    }

    public LocalDateTime getNewSetupDate() {
        return newSetupDate;
    }

    public void setNewSetupDate(LocalDateTime newSetupDate) {
        this.newSetupDate = newSetupDate;
    }

    public UUID getRowGuid() {
        return rowGuid;
    }

    public void setRowGuid(UUID rowGuid) {
        this.rowGuid = rowGuid;
    }

    public Boolean getCapmi() {
        return isCapmi;
    }

    public void setCapmi(Boolean capmi) {
        isCapmi = capmi;
    }

    public String getOperatorEdits() {
        return operatorEdits;
    }

    public void setOperatorEdits(String operatorEdits) {
        this.operatorEdits = operatorEdits;
    }

    public String getOperatorEdit() {
        return operatorEdit;
    }

    public void setOperatorEdit(String operatorEdit) {
        this.operatorEdit = operatorEdit;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    public LocalDateTime getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(LocalDateTime dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public String getNac() {
        return nac;
    }

    public void setNac(String nac) {
        this.nac = nac;
    }

    public BigDecimal getBackBalance() {
        return backBalance;
    }

    public void setBackBalance(BigDecimal backBalance) {
        this.backBalance = backBalance;
    }

    public String getGis() {
        return gis;
    }

    public void setGis(String gis) {
        this.gis = gis;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getAccessGroup() {
        return accessGroup;
    }

    public void setAccessGroup(String accessGroup) {
        this.accessGroup = accessGroup;
    }

    //    public String getAceCustomerId() {
//        return aceCustomerId;
//    }
//
//    public void setAceCustomerId(String aceCustomerId) {
//        this.aceCustomerId = aceCustomerId;
//    }
//
//    public String getCenterId() {
//        return centerId;
//    }
//
//    public void setCenterId(String centerId) {
//        this.centerId = centerId;
//    }
//
//    public String getAssetId() {
//        return assetId;
//    }
//
//    public void setAssetId(String assetId) {
//        this.assetId = assetId;
//    }
//
//    public String getUpriserId() {
//        return upriserId;
//    }
//
//    public void setUpriserId(String upriserId) {
//        this.upriserId = upriserId;
//    }
//
//    public Boolean getHasTenant() {
//        return hasTenant;
//    }
//
//    public void setHasTenant(Boolean hasTenant) {
//        this.hasTenant = hasTenant;
//    }
//
//    public String getLandLordName() {
//        return landLordName;
//    }
//
//    public void setLandLordName(String landLordName) {
//        this.landLordName = landLordName;
//    }
//
//    public String getLandLordMobileNo() {
//        return landLordMobileNo;
//    }
//
//    public void setLandLordMobileNo(String landLordMobileNo) {
//        this.landLordMobileNo = landLordMobileNo;
//    }
//
//    public String getCin() {
//        return cin;
//    }
//
//    public void setCin(String cin) {
//        this.cin = cin;
//    }
//
//    public Boolean getPrepaid() {
//        return isPrepaid;
//    }
//
//    public void setPrepaid(Boolean prepaid) {
//        isPrepaid = prepaid;
//    }
//
//    public String getMeterOem() {
//        return meterOem;
//    }
//
//    public void setMeterOem(String meterOem) {
//        this.meterOem = meterOem;
//    }
//
//    public String getLtPoleId() {
//        return ltPoleId;
//    }
//
//    public void setLtPoleId(String ltPoleId) {
//        this.ltPoleId = ltPoleId;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getMaritalStatus() {
//        return maritalStatus;
//    }
//
//    public void setMaritalStatus(String maritalStatus) {
//        this.maritalStatus = maritalStatus;
//    }
//
//    public String getBusinessType() {
//        return businessType;
//    }
//
//    public void setBusinessType(String businessType) {
//        this.businessType = businessType;
//    }

    public Boolean isNewSetUpStatus() {
        return newSetUpStatus;
    }

    public void setNewSetUpStatus(Boolean newSetUpStatus) {
        this.newSetUpStatus = newSetUpStatus;
    }
}
