package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Model.CustomerAccountNoGenerated;
import org.example.newaccountnogenerator.Model.Undertaking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

@Repository
public interface AccountNoRepository extends JpaRepository<CustomerAccountNoGenerated, String> {

    @Query("SELECT c.serialNo FROM CustomerAccountNoGenerated c WHERE c.bookNo = ?1 ORDER BY c.serialNo DESC")
    List<String> findTopSerialByBookNo(String bookNo);

    List<CustomerAccountNoGenerated> findAccountNoByStatusAndBUIDAndUtid(boolean status, String bUID, String Utid);

    Optional<CustomerAccountNoGenerated> findByAccountNo (String accountNo);

}
