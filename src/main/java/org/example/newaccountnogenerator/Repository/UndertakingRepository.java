package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Model.Undertaking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UndertakingRepository extends JpaRepository<Undertaking, String> {

    Optional<Undertaking> findUndertakingByBuidAndUtid(String buid, String utid);
}
