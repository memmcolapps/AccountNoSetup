package org.example.newaccountnogenerator.repository;

import org.example.newaccountnogenerator.model.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, String> {

    boolean existsByBuid(String buid);
}
