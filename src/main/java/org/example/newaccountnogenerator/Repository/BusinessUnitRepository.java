package org.example.newaccountnogenerator.Primary.Repository;

import org.example.newaccountnogenerator.Primary.Entity.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, String> {

    boolean existsByBuid(String buid);
}
