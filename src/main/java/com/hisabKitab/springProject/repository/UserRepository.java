package com.hisabKitab.springProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hisabKitab.springProject.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    // Find a user by email and password for login
    UserEntity findByEmailAndPassword(String email, String password);

    // Find a user by email to check if the user already exists
    UserEntity findByEmail(String email);

    Optional<UserEntity> getByEmail(String email);

	UserEntity findByContactNo(String contactNo);

    UserEntity findByFullName(String fullName);
}
