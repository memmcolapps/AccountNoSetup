package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Model.CustomerAccountNoGenerated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountNoRepository extends JpaRepository<CustomerAccountNoGenerated, String> {

    @Query("SELECT c.serialNo FROM CustomerAccountNoGenerated c WHERE c.bookNo = ?1 ORDER BY c.serialNo DESC")
    List<String> findTopSerialByBookNo(String bookNo);

    List<CustomerAccountNoGenerated> findByBookNoAndStatus(String bookNo, boolean status);
}
