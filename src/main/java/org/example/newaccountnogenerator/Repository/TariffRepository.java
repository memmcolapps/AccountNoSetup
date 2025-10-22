package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Tariff,Integer> {
    boolean existsByTariffId(int id);
}
