package org.example.newaccountnogenerator.repository;

import org.example.newaccountnogenerator.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Tariff,Integer> {
    boolean existsByTariffId(int id);
}
