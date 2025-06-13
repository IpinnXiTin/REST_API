package com.ipin.rest_api.repository;

import com.ipin.rest_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String userName);
}
