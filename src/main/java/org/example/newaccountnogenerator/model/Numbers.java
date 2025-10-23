package org.example.newaccountnogenerator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;



@Entity
public class Numbers {
    @Id
    private int num;

    public Numbers() {}
}
