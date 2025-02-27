package com.hisabKitab.springProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.FriendRequestResponse;
import com.hisabKitab.springProject.entity.FriendRequestEntity;
import com.hisabKitab.springProject.service.FriendRequestService;
import com.hisabKitab.springProject.service.UserService;

@RestController
@RequestMapping("/user/friend-request")
@CrossOrigin(origins = "*")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;
    
    @Autowired
    private UserService userService;
    

    @PostMapping("/send")
    public ResponseEntity<String> sendRequest( @RequestParam String recieverContactNo) {
        var sender =  userService.getUserFromToken();
    	
    	var reciever = userService.findUserByContactNo(recieverContactNo);
    	if(sender==null || reciever==null) {
    		return ResponseEntity.badRequest().body("User not exist");
    	}

        FriendRequestResponse response = friendRequestService.sendRequest(sender, reciever);

        switch (response.getStatus()) {
            case SELF_REQUEST_NOT_ALLOWED:
            	System.out.println("self request error");
                return ResponseEntity.badRequest().body("You cannot send a friend request to yourself.");
            case ALREADY_FRIENDS:
                return ResponseEntity.badRequest().body("You are already friends.");
            case REQUEST_ALREADY_SENT:
                return ResponseEntity.badRequest().body("Friend request already sent.");
            case REQUEST_SENT:
                return ResponseEntity.ok("Friend request sent successfully!");
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptRequest(@PathVariable Long requestId) {
    	
    	var user = userService.getUserFromToken();
    	var request = friendRequestService.acceptRequest(user.getUserId(), requestId);
    	if(request==null) {
    		return ResponseEntity.badRequest().body("Request not exist");
    	}
        return ResponseEntity.ok().body("Friend request accepted");
    }

    @DeleteMapping("/unsend/{requestId}")
    public ResponseEntity<String> unsendRequest(@PathVariable Long requestId) {
    	var user = userService.getUserFromToken();
    	
        friendRequestService.unsendRequest(user.getUserId(), requestId);
       
        return ResponseEntity.ok("Friend request unsent successfully");
    }

    @DeleteMapping("/reject/{requestId}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long requestId) {
    	var user = userService.getUserFromToken();

        friendRequestService.deleteRequest(user.getUserId(),requestId);
        return ResponseEntity.ok("Friend request deleted successfully");
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestEntity>> getAllPendingRequests() {
        var user =  userService.getUserFromToken();
        return ResponseEntity.ok(friendRequestService.getAllPendingRequests(user.getUserId()));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<FriendRequestEntity>> getAllSentRequests() {
        var user =  userService.getUserFromToken();
        return ResponseEntity.ok(friendRequestService.getAllSentRequests(user.getUserId()));
    }
}
