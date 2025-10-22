package org.example.newaccountnogenerator.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;



@Entity
public class Numbers {
    @Id
    private int num;

    public Numbers() {}
}
