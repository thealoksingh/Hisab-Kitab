package com.hisabKitab.springProject.controller;



import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.UserRepository;
import com.hisabKitab.springProject.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class AdminController {
	  @Autowired
		private UserService userService;
	  @Autowired
	  private UserRepository userRepository;
	  
	  @DeleteMapping("/user/{userId}") 
	    public ResponseEntity<String> deleteUserById(@PathVariable("userId") Long userId){
	    	userService.deleteUserById(userId);
	    	
	    	return ResponseEntity.ok("User Deleted Succesfully");
	    	
	    }
	  
	  @GetMapping("/getAllUser")
	  public ResponseEntity<List<UserEntity>> getAllUsers(){
		  var users = userService.getAllUser();
		  
		  return ResponseEntity.ok(users);
	  }
	  
	 

	  

}
