package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Model.DistributionSubstation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface DistributionSubstationRepository extends JpaRepository<DistributionSubstation, String> {

    Optional<DistributionSubstation> findByDistributionIdAndBuid(String distributionId, String buid);

    Optional<DistributionSubstation> findByFeederIDAndBuid(String feederID, String buid);
}
