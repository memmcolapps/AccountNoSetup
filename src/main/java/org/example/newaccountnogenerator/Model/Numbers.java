package org.example.newaccountnogenerator.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;



@Entity
public class Numbers {
    @Id
    private int number;

    public Numbers() {}
}
