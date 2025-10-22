package org.example.newaccountnogenerator.Primary.Repository;

import org.example.newaccountnogenerator.Primary.Entity.Undertaking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UndertakingRepository extends JpaRepository<Undertaking, String> {

    Optional<Undertaking> findUndertakingByBuidAndUtid(String buid, String utid);
}
