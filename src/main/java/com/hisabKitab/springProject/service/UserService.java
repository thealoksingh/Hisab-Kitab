package com.hisabKitab.springProject.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.dto.SignUpUserDto;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Method to log in user by checking email and password
    public boolean login(String email, String password) {
        // Retrieve the user from the database
    	UserEntity user = userRepository.findByEmail(email);

        // Check if user exists and if the password matches
        if (user != null) {
            // Here you would normally hash the password and compare
            return user.getPassword().equals(password); // Simplified for demonstration
        }
        return false;
    }

    // Method to sign up a new user
    public String signup(SignUpUserDto newUser) {
       UserEntity existingUser = userRepository.findByEmail(newUser.getEmail());
        
        if (existingUser != null) {
        
            return "User already exists with this email!";
        }

        userRepository.save(new UserEntity(newUser.getFullName(),newUser.getEmail(),newUser.getPassword(),newUser.getContactNo()));
        return "User registered successfully!";
    }

	public UserEntity checkUserExistByContactNumber(Long userId, String contactNo) {
		UserEntity friend =  userRepository.findByContactNo(contactNo);
		
		if (friend != null) {
			var user = userRepository.findById(userId).get();
			user.getFriends().add(friend);
			friend.getFriends().add(user);
			userRepository.save(user);
			userRepository.save(friend);
			return friend;
			
		}
		
		return friend;  
		
	}

	public Set<UserEntity> getAllFriendList(Long userId) {
		var user = userRepository.findById(userId).orElse(null);
	
		return (user != null) ? user.getFriends(): null;
	}
}
