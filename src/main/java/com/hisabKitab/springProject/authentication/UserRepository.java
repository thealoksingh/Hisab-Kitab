package com.hisabKitab.springProject.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    // Find a user by email and password for login
    UserEntity findByEmailAndPassword(String email, String password);

    // Find a user by email to check if the user already exists
    UserEntity findByEmail(String email);
}
