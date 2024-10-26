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
    public UserEntity login(String email, String password) {
      UserEntity user = userRepository.findByEmail(email);

        if (user != null) {
             if(user.getPassword().equals(password)){
            	 return user;
             } 
        }
        return null;
    }

    // Method to sign up a new user
    public String signup(SignUpUserDto newUser) {
       UserEntity existingUser = userRepository.findByEmail(newUser.getEmail());
        
        if (existingUser != null) {
        
            return "User already exists with this email!";
        }

        userRepository.save(new UserEntity(newUser.getFullName(),newUser.getEmail(),newUser.getPassword(),newUser.getRole(),newUser.getContactNo()));
        return "User registered successfully!";
    }
    

	public UserEntity checkUserExistByContactNumber(Long userId, String contactNo) {
		var friend =  userRepository.findByContactNo(contactNo);
		
		if (friend != null) {
			var user = userRepository.findById(userId).get();
			user.getFriends().add(friend);
			friend.getFriends().add(user);
			userRepository.save(user);
			userRepository.save(friend);
			return friend;
			
		}
		
		return null;
		
	}

	public Set<UserEntity> getAllFriendList(Long userId) {
		var user = userRepository.findById(userId).orElse(null);
		return user.getFriends();
	}
}
