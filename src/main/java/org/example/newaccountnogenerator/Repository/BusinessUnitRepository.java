package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Model.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, String> {

    boolean existsByBuid(String buid);
}
