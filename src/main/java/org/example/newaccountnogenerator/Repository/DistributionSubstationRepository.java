package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Entity.DistributionSubstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DistributionSubstationRepository extends JpaRepository<DistributionSubstation, String> {

    Optional<DistributionSubstation> findByDistributionIDAndBuidOrUtid(String distributionID, String buid, String utid);

    Optional<DistributionSubstation> findByDistributionIDAndBuid(String distributionID, String buid);

    boolean existsByDistributionID(String dssId);
}
