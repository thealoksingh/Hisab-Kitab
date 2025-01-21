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
	    return new FriendRequestResponse(FriendRequestStatus.REQUEST_SENT, savedRequest);
	}


	public FriendRequestEntity acceptRequest(Long requestId) {
		FriendRequestEntity request = friendRequestRepository.findById(requestId)
				.orElseThrow(() -> new RuntimeException("Request not found"));
		request.setStatus("ACCEPTED");
		userService.addFriend(request.getSender(), request.getReceiver());
		deleteRequest(requestId);
		return request;
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
