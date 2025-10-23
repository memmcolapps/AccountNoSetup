package org.example.newaccountnogenerator.repository;

import org.example.newaccountnogenerator.model.CustomerNew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerNewRepository extends JpaRepository<CustomerNew, Integer> {
    CustomerNew findByAccountNo(String accountNo);
}
