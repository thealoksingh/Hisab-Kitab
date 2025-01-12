package com.hisabKitab.springProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hisabKitab.springProject.entity.FriendRequestEntity;
import com.hisabKitab.springProject.service.FriendRequestService;
import com.hisabKitab.springProject.service.UserService;

@RestController
@RequestMapping("/user/friend-request")
@CrossOrigin(origins = "http://localhost:3000")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;
    
    @Autowired
    private UserService userService;
    

    @PostMapping("/send")
    public ResponseEntity<String> sendRequest(@RequestParam Long senderId, @RequestParam String recieverContactNo) {
    	var sender = userService.findUserById(senderId);
    	var reciever = userService.findUserByContactNo(recieverContactNo);
    	if(sender==null || reciever==null) {
    		return ResponseEntity.badRequest().body("User not exist");
    	}
    	var request = friendRequestService.sendRequest(sender, reciever);
    	if(request==null) {
    		return ResponseEntity.badRequest().body("User already in Friend List");
    	}
    	return ResponseEntity.ok().body("Friend request Sent");
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptRequest(@PathVariable Long requestId) {
    	
    	var request = friendRequestService.acceptRequest(requestId);
    	if(request==null) {
    		return ResponseEntity.badRequest().body("Request not exist");
    	}
        return ResponseEntity.ok().body("Friend request accepted");
    }

    @DeleteMapping("/unsend/{requestId}")
    public ResponseEntity<String> unsendRequest(@PathVariable Long requestId) {
        var request = friendRequestService.unsendRequest(requestId);
        if(request==null) {
    		return ResponseEntity.badRequest().body("Request not exist");
    	}
        return ResponseEntity.ok("Friend request unsent successfully");
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long requestId) {
        friendRequestService.deleteRequest(requestId);
        return ResponseEntity.ok("Friend request deleted successfully");
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestEntity>> getAllPendingRequests(@RequestParam Long receiverId) {
        return ResponseEntity.ok(friendRequestService.getAllPendingRequests(receiverId));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<FriendRequestEntity>> getAllSentRequests(@RequestParam Long senderId) {
        return ResponseEntity.ok(friendRequestService.getAllSentRequests(senderId));
    }
}
