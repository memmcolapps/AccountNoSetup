package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Model.UndertakingBookNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UndertakingBookNumberRepository extends JpaRepository<UndertakingBookNumber, String> {

    boolean existsByBookNumber(String bookNumber);
}
