package com.hisabKitab.springProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.dto.FriendRequestResponse;
import com.hisabKitab.springProject.dto.FriendRequestStatus;
import com.hisabKitab.springProject.entity.FriendRequestEntity;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.FriendRequestRepository;

@Service
public class FriendRequestService {

	@Autowired
	private FriendRequestRepository friendRequestRepository;
	
	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	private UserService userService;
	
	

	public FriendRequestResponse sendRequest(UserEntity sender, UserEntity receiver) {

	    if (sender.getUserId().equals(receiver.getUserId())) {
	        return new FriendRequestResponse(FriendRequestStatus.SELF_REQUEST_NOT_ALLOWED, null);
	    }

	    for (UserEntity friend : sender.getFriends()) {
	        if (friend.getUserId().equals(receiver.getUserId())) {
	            return new FriendRequestResponse(FriendRequestStatus.ALREADY_FRIENDS, null);
	        }
	    }

	    var existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
	    if (existingRequest != null) {
	        return new FriendRequestResponse(FriendRequestStatus.REQUEST_ALREADY_SENT, existingRequest);
	    }

	    FriendRequestEntity friendRequest = new FriendRequestEntity();
	    friendRequest.setSender(sender);
	    friendRequest.setReceiver(receiver);
	    friendRequest.setStatus("PENDING");

	    FriendRequestEntity savedRequest = friendRequestRepository.save(friendRequest);
	    String subjectText = sender.getFullName()+" has sent you a friend request.";
		String emailBodyMessage = String.format("""
        		        Hi %s,

        Great news! You received a friend request from %s.

		Accept the friend request on Hisab Kitab to build stronger connections and simplify expense management. 
		Collaborate effortlessly, stay organized, and make managing and sharing expenses a breeze.

        Log in now to start collaborating: https://hisab-kitab-business.netlify.app/

        Cheers,  
        The Hisab Kitab Team
        """,
				receiver.getFullName(), sender.getFullName());
		
	    
	    if(emailNotificationService.sendAndAcceptFriendRequestNotification(receiver.getEmail(),subjectText, emailBodyMessage )) {
	    	
	    	return new FriendRequestResponse(FriendRequestStatus.REQUEST_SENT, savedRequest);
	    }
	    return new FriendRequestResponse(FriendRequestStatus.REQUEST_NOT_SENT, null);
	    
	    
	    
	}

	public FriendRequestEntity acceptRequest(Long requestId) {
		FriendRequestEntity request = friendRequestRepository.findById(requestId)
				.orElseThrow(() -> new RuntimeException("Request not found"));
		
		var sender = request.getSender();
		var receiver = request.getReceiver();
		
		String subjectText = receiver.getFullName()+" has accepted your friend request.";
		String emailBodyMessage = String.format("""
        		        Hi %s,

        Great news! Your friend request to %s has been accepted.

        You are now connected on Hisab Kitab, making it even easier to manage and share your expenses together. 

        Log in now to start collaborating: https://hisab-kitab-business.netlify.app/

        Cheers,  
        The Hisab Kitab Team
        """,
				sender.getFullName(), receiver.getFullName());
		
	    
	    if(emailNotificationService.sendAndAcceptFriendRequestNotification(sender.getEmail(),subjectText, emailBodyMessage )) {
	    	request.setStatus("ACCEPTED");
			userService.addFriend(sender, receiver);
			deleteRequest(requestId);
	    	return  request;
	    }
		
		
		
		return null;
	}

	public FriendRequestEntity unsendRequest(Long requestId) {
		var request = friendRequestRepository.findById(requestId).orElse(null);
		if(request!=null) {
			 friendRequestRepository.deleteById(requestId);
		} return request;
		
	}

	public FriendRequestEntity deleteRequest(Long requestId) {
		return unsendRequest(requestId);
	}

	public List<FriendRequestEntity> getAllPendingRequests(Long receiverId) {
		var user = userService.findUserById(receiverId);
		return user != null ? user.getReceivedRequests() : null;
	}

	public List<FriendRequestEntity> getAllSentRequests(Long senderId) {
		var user = userService.findUserById(senderId);
		return user != null ? user.getSentRequests() : null;
	}
}
