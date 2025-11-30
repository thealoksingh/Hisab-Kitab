package com.hisabKitab.springProject.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.CommonResponseDto;
import com.hisabKitab.springProject.dto.FriendRequestResponse;
import com.hisabKitab.springProject.entity.FriendRequestEntity;
import com.hisabKitab.springProject.service.FriendRequestService;
import com.hisabKitab.springProject.service.UserService;
import com.hisabKitab.springProject.utils.ResponseBuilder;

@RestController
@RequestMapping("/user/friend-request")
@CrossOrigin(origins = "*")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(FriendRequestController.class);
    

    @PostMapping("/send")
    public ResponseEntity<CommonResponseDto<FriendRequestResponse>> sendRequest( @RequestParam String recieverContactNo) {
        var sender =  userService.getUserFromToken();
    	
    	var reciever = userService.findUserByContactNo(recieverContactNo);
    	if(sender==null || reciever==null) {
            return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "User not exist");

    	}

        FriendRequestResponse response = friendRequestService.sendRequest(sender, reciever);

        switch (response.getStatus()) {
            case SELF_REQUEST_NOT_ALLOWED:
            	System.out.println("self request error");
                return ResponseBuilder.failure(HttpStatus.BAD_REQUEST,"You cannot send a friend request to yourself.");
            case ALREADY_FRIENDS:
                return ResponseBuilder.failure(HttpStatus.BAD_REQUEST,"You are already friends.");
            case REQUEST_ALREADY_SENT:
                return ResponseBuilder.failure(HttpStatus.BAD_REQUEST,"Friend request already sent.");
            case REQUEST_SENT:
                // Send WebSocket notification to receiver
                try {
                    String destination = "/topic/friend-requests/" + reciever.getUserId();
                    messagingTemplate.convertAndSend(destination, response);
                    logger.info("Friend request sent via WebSocket to: {}", destination);
                } catch (Exception e) {
                    logger.error("WebSocket send failed: {}", e.getMessage(), e);
                }
                return ResponseBuilder.success(HttpStatus.OK,"Friend request sent successfully!", response);
            default:
                return ResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred.");
        }
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<CommonResponseDto<FriendRequestEntity>> acceptRequest(@PathVariable Long requestId) {

    	var user = userService.getUserFromToken();
    	var request = friendRequestService.acceptRequest(user.getUserId(), requestId);
    	if(request==null) {
    		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST,"Request not exist");
    	}
    	
    	// Send WebSocket notification to sender
    	try {
    		String destination = "/topic/friend-requests/" + request.getSender().getUserId();
    		messagingTemplate.convertAndSend(destination, request);
    		logger.info("Friend request acceptance sent via WebSocket to: {}", destination);
    	} catch (Exception e) {
    		logger.error("WebSocket send failed: {}", e.getMessage(), e);
    	}
    	
        return ResponseBuilder.success(HttpStatus.OK,"Friend request accepted", request);
    }

    @DeleteMapping("/unsend/{requestId}")
    public ResponseEntity<CommonResponseDto<String>> unsendRequest(@PathVariable Long requestId) {
    	var user = userService.getUserFromToken();
    	
        friendRequestService.unsendRequest(user.getUserId(), requestId);
       
        return ResponseBuilder.success(HttpStatus.OK,"Friend request unsent successfully", null);
    }

    @DeleteMapping("/reject/{requestId}")
    public ResponseEntity<CommonResponseDto<String>> deleteRequest(@PathVariable Long requestId) {
    	var user = userService.getUserFromToken();

        friendRequestService.deleteRequest(user.getUserId(),requestId);
        return ResponseBuilder.success(HttpStatus.OK,"Friend request deleted successfully", null);
    }

    @GetMapping("/pending")
    public ResponseEntity<CommonResponseDto<List<FriendRequestEntity>>> getAllPendingRequests() {
        var user =  userService.getUserFromToken();
        return ResponseBuilder.success(HttpStatus.OK,"Pending friend requests retrieved successfully", friendRequestService.getAllPendingRequests(user.getUserId()));
    }

    @GetMapping("/sent")
    public ResponseEntity<CommonResponseDto<List<FriendRequestEntity>>> getAllSentRequests() {
        var user =  userService.getUserFromToken();
        return ResponseBuilder.success(HttpStatus.OK,"Sent friend requests retrieved successfully", friendRequestService.getAllSentRequests(user.getUserId()));
    }
}
