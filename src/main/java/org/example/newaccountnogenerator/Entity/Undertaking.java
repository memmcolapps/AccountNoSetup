package org.example.newaccountnogenerator.Primary.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Undertaking {

    @Id
    @Column(name = "UTID")
    private String utid;

    @Column(name = "BUID")
    private String buid;

    @Column(name = "ZoneID")
    private String zoneId;

    @Column(name = "Name")
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

    @Column(name = "Email")
    private String email;

    @Column(name = "Website")
    private String website;
    private UUID rowguid;
}
