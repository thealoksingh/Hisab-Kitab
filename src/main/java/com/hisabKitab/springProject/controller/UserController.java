package com.hisabKitab.springProject.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hisabKitab.springProject.dto.GetFriendListDto;
import com.hisabKitab.springProject.dto.SignUpUserDto;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
	private UserService userService;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestParam String email, @RequestParam String password) {
        UserEntity user= userService.login(email, password);
        
//        Map<String, String> response = new HashMap<>();
        
        if ( user != null ) {
        	System.out.println(user.toString());
        	return ResponseEntity.status(HttpStatus.OK).body(user);
          
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        }
    }


    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpUserDto newUser) {
        String result = userService.signup(newUser);
        if (result.equals("User registered successfully!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(400).body(result);  // If user already exists
        }
    }
    
  
    
    @GetMapping("/addfriend/{userId}")
    public ResponseEntity<String> addFriend(@PathVariable("userId")Long userId, @RequestParam("contactNo") String contactNo){
    	
    	var friend = userService.checkUserExistByContactNumber(userId,contactNo);
    	
    	if (friend != null) {
    		return ResponseEntity.ok("Friend Added Successfully");
    	} return ResponseEntity.status(400).body("User not existed with the contact no = "+contactNo);  // If user not exists
    }
    
    @GetMapping("/getAllFriendList/{userId}")
    public ResponseEntity<GetFriendListDto> getAllFriends(@PathVariable("userId") Long userId){
    	var friendList = userService.getAllFriendList(userId);
    	
    	GetFriendListDto gfl = new GetFriendListDto();
    	
    	if(friendList == null) {
    		gfl.setMessage("User not Existed by Id = "+userId);
    		return ResponseEntity.status(400).body(gfl);
    	} else if(friendList.isEmpty()) {
    		gfl.setMessage("No friends are there in the List");
    		gfl.setFriendList(friendList);
    		return ResponseEntity.status(400).body(gfl);
    	}
    	gfl.setMessage("Friend List founded");
    	gfl.setFriendList(friendList);
    	return ResponseEntity.ok(gfl);
    }
}
