package org.example.newaccountnogenerator.repository;

import org.example.newaccountnogenerator.model.UndertakingBookNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UndertakingBookNumberRepository extends JpaRepository<UndertakingBookNumber, String> {

    boolean existsByBookNumber(String bookNumber);
}
