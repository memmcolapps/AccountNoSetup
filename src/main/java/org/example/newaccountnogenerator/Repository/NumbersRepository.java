package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Entity.Numbers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NumbersRepository extends JpaRepository<Numbers, Integer> {

    @Query("SELECT n.num FROM Numbers n WHERE n.num - 1 NOT IN "  +
            "(SELECT CAST(c.serialNo AS int) FROM CustomerAccountNoGenerated c WHERE c.bookNo = ?1)")
    List<Integer> findAvailableNumbers(String bookNo);
}
