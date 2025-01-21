package com.hisabKitab.springProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> sendRequest(@RequestParam Long senderId, @RequestParam String recieverContactNo) {
    	var sender = userService.findUserById(senderId);
    	var reciever = userService.findUserByContactNo(recieverContactNo);
    	if(sender==null || reciever==null) {
    		return ResponseEntity.badRequest().body("User not exist");
    	}

        FriendRequestResponse response = friendRequestService.sendRequest(sender, reciever);

        switch (response.getStatus()) {
            case SELF_REQUEST_NOT_ALLOWED:
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
