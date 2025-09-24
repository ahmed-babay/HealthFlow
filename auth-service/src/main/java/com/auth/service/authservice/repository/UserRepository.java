package com.auth.service.authservice.repository;

import com.auth.service.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Find user by username for login
    Optional<User> findByUsername(String username);

    // Find user by email for login
    Optional<User> findByEmail(String email);

    // Check if username already exists
    boolean existsByUsername(String username);

    // Check if email already exists  
    boolean existsByEmail(String email);

    // Find user by username OR email (flexible login)
    Optional<User> findByUsernameOrEmail(String username, String email);

    // Find only active users
    Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);
}

