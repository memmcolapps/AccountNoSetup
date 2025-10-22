package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Entity.CustomerNew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerNewRepository extends JpaRepository<CustomerNew, Integer> {
    CustomerNew findByAccountNo(String accountNo);
}
