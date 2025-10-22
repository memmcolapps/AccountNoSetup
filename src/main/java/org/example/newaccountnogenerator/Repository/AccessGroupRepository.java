package org.example.newaccountnogenerator.Repository;

import org.example.newaccountnogenerator.Entity.AccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessGroupRepository extends JpaRepository<AccessGroup, Integer> {

    boolean existsByAccessGroupCodeAndTokenAndActiveTrue(String accessGroupCode, String token);

    Optional<AccessGroup> findByAccessGroupCodeAndToken(String accessGroupCode, String token);
}
