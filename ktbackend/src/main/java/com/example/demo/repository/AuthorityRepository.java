package com.example.demo.repository;

import com.example.demo.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);

    @Query(nativeQuery = true, value = "SELECT a.* FROM authority a LEFT JOIN role_authorities r ON a.name = r.authority_name WHERE role_name = :roleName")
    List<Authority> findAllByRoleName(@Param("roleName") String roleName);
}
