package com.hisabKitab.springProject.controller;

import java.util.HashMap;
import java.lang.String;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hisabKitab.springProject.dto.GetFriendListDto;
import com.hisabKitab.springProject.dto.SignUpUserDto;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.service.EmailNotificationService;
import com.hisabKitab.springProject.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
	private UserService userService;
    
    @Autowired
    private EmailNotificationService emailNotificationService;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestParam String email, @RequestParam String password) {
    	
    	System.out.println("login api called");
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
    
    @PostMapping("/sendInvite")
    public ResponseEntity<String> sendInviteEmail(@RequestParam("email") String recipientEmail,@RequestParam("senderName") String senderName){
    	if(emailNotificationService.sendInviteNotification(recipientEmail, senderName)) {
    		return ResponseEntity.ok("Invite Sent Successfully");
    	} return ResponseEntity.badRequest().body("Invite failed");
    }
    
    @PostMapping("/sendOTP")
    public ResponseEntity<String> sendOTPMail(@RequestParam("email") String recipientEmail){
    	var otp = emailNotificationService.sendOtpNotification(recipientEmail);
    	if(otp != null) {
    		return ResponseEntity.ok(otp);
    	} return ResponseEntity.badRequest().body(null);
    }
    
    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriend(userId, friendId);
        return ResponseEntity.ok("Friend removed successfully.");
    }
    

   
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @RequestParam("email") String email, 
            @RequestParam("newPassword") String newPassword) {
        try {
            var response = userService.updatePassword(email, newPassword);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the password.");
        }
    }

    
    @GetMapping("/getAllFriendList/{userId}")
    public ResponseEntity<GetFriendListDto> getAllFriends(@PathVariable("userId") Long userId){
    	var friendList = userService.getAllFriendList(userId);
    	
    	var gfl = userService.getAllFriendListWithDetails(userId, friendList);
    	
//    	GetFriendListDto gfl = new GetFriendListDto();
    	
    	if(friendList == null) {
    		gfl.setMessage("User not Existed by Id = "+userId);
    		return ResponseEntity.status(400).body(gfl);
    	} else if(friendList.isEmpty()) {
    		gfl.setMessage("No friends are there in the List");
//    		gfl.setFriendList(friendList);
    		return ResponseEntity.status(400).body(gfl);
    	}
    	gfl.setMessage("Friend List founded");
//    	gfl.setFriendList(friendList);
    	return ResponseEntity.ok(gfl);
    }
    
    
    
   
}
