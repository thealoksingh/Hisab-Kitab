package com.hisabKitab.springProject.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository authenticationRepository;

    // Method to log in user by checking email and password
    public boolean login(String email, String password) {
        // Retrieve the user from the database
    	UserEntity user = authenticationRepository.findByEmail(email);

        // Check if user exists and if the password matches
        if (user != null) {
            // Here you would normally hash the password and compare
            return user.getPassword().equals(password); // Simplified for demonstration
        }
        return false;
    }

    // Method to sign up a new user
    public String signup(UserEntity newUser) {
       UserEntity existingUser = authenticationRepository.findByEmail(newUser.getEmail());
        
        if (existingUser != null) {
        
            return "User already exists with this email!";
        }

        authenticationRepository.save(newUser);
        return "User registered successfully!";
    }
}
