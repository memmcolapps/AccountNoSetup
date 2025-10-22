package org.example.newaccountnogenerator.Primary.Repository;

import org.example.newaccountnogenerator.Primary.Entity.DistributionSubstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistributionSubstationRepository extends JpaRepository<DistributionSubstation, String> {

    Optional<DistributionSubstation> findByDistributionIDAndBuidOrUtid(String distributionID, String buid, String utid);

//    Optional<DistributionSubstation> findByFeederIDAndBuid(String feederID, String buid);
}
