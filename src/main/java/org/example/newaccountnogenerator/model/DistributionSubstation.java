package org.example.newaccountnogenerator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class DistributionSubstation {

    @Id
    @Column(name = "DistributionID")
    private String distributionID;

    @Column(name = "Name")
    private String name;

    @Column(name = "Capacity")
    private String capacity;

    @Column(name = "MDMeterSN")
    private String mdMeterSN;

    @Column(name = "CTRatio")
    private String ctRatio;

    @Column(name = "Multiplier")
    private Integer multiplier;

    @Column(name = "Location")
    private String location;

    @Column(name = "City")
    private String city;

    @Column(name = "State")
    private String state;

    @Column(name = "InjectionID")
    private String injectionID;

    @Column(name = "UTID")
    private String utid;

    @Column(name = "BUID")
    private String buid;

    @Column(name = "VoltageRatio")
    private String voltageRatio;

    @Column(name = "SubstationType")
    private String substationType;

    @Column(name = "Longitude")
    private Integer longitude;

    @Column(name = "Latitude")
    private Integer latitude;

    @Column(name = "NAC")
    private String nac;
    private Boolean status;

    @Column(name = "FeederID")
    private String feederID;


    @Column(name = "MarketerID")
    private UUID marketerID;

}
