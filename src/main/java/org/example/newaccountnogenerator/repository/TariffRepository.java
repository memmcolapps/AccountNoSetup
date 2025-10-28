package org.example.newaccountnogenerator.repository;

import org.example.newaccountnogenerator.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TariffRepository extends JpaRepository<Tariff,Integer> {
    boolean existsByTariffId(int id);

    @Query("SELECT t.accountType FROM Tariff t WHERE t.tariffId = :id")
    String getAccountTypeByTariffId(@Param("id") int id);
}
