package com.ipin.rest_api.repository;

import com.ipin.rest_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    boolean existsByName(String roleName);
    Optional<Role> findByName(String roleName);
    void deleteByName(String roleName);
}
