package org.example.newaccountnogenerator.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class BusinessUnit {

    @Id
    @Column(name = "BUID", nullable = false)
    private String buid;

    @Column(name = "ZoneID", nullable = false)
    private String zoneId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Address")
    private String address;

    @Column(name = "State")
    private String state;

    @Column(name = "ContactPerson")
    private String contactPerson;

    @Column(name = "Telephone")
    private String telephone;

    @Column(name = "Mobile")
    private String mobile;

    @Column(name = "EMail")
    private String email;

    @Column(name = "Website")
    private String website;

    private String bankCode;
    private String refCode;
    private String buCode;
    private UUID rowGuid;
}
