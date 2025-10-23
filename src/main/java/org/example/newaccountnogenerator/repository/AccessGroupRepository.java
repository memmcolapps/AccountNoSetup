package org.example.newaccountnogenerator.repository;

import org.example.newaccountnogenerator.model.AccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessGroupRepository extends JpaRepository<AccessGroup, Integer> {

    boolean existsByAccessGroupCodeAndTokenAndActiveTrue(String accessGroupCode, String token);

    Optional<AccessGroup> findByAccessGroupCodeAndToken(String accessGroupCode, String token);
}
