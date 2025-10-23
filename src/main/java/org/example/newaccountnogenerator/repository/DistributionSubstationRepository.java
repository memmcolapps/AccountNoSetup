package org.example.newaccountnogenerator.repository;

import org.example.newaccountnogenerator.model.DistributionSubstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistributionSubstationRepository extends JpaRepository<DistributionSubstation, String> {

    Optional<DistributionSubstation> findByDistributionIDAndBuidOrUtid(String distributionID, String buid, String utid);

    Optional<DistributionSubstation> findByDistributionIDAndBuid(String distributionID, String buid);

    boolean existsByDistributionID(String dssId);
}
