package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Entity.UndertakingBookNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UndertakingBookNumberRepository extends JpaRepository<UndertakingBookNumber, String> {

    boolean existsByBookNumber(String bookNumber);
}
