package org.example.newaccountnogenerator.repository;

import jakarta.transaction.Transactional;
import org.example.newaccountnogenerator.model.CustomerAccountNoGenerated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountNoRepository extends JpaRepository<CustomerAccountNoGenerated, String> {

    @Query("SELECT c.serialNo FROM CustomerAccountNoGenerated c WHERE c.bookNo = ?1 ORDER BY c.serialNo DESC")
    List<String> findTopSerialByBookNo(String bookNo);

    @Query("SELECT a.accountNo FROM CustomerAccountNoGenerated a WHERE a.status = false AND a.bUID = :buid AND a.utid = :utid")
    List<String> findAccountNoStatusZeroByBuidAndUtid(String buid, String utid);

    boolean existsByAccountNoAndBUID(String accountNo,String buid);

    @Transactional
    @Modifying
    @Query("UPDATE CustomerAccountNoGenerated c SET c.status = true WHERE c.accountNo = :accountNo AND c.bUID = :buid")
    int updateStatusToTrueByAccountNoAndBuid(@Param("accountNo") String accountNo, @Param("buid") String buid);


}
