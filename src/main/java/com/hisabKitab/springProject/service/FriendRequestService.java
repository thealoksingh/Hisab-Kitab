package com.hisabKitab.springProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.FriendRequestEntity;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.FriendRequestRepository;

@Service
public class FriendRequestService {

	@Autowired
	private FriendRequestRepository friendRequestRepository;

	@Autowired
	private UserService userService;

	public FriendRequestEntity sendRequest(UserEntity sender, UserEntity receiver) {

		for (UserEntity f : sender.getFriends()) {
			if (f.getUserId() == receiver.getUserId()) {
				return null;
			}
		}
		FriendRequestEntity friendRequest = new FriendRequestEntity();
		friendRequest.setSender(sender);
		friendRequest.setReceiver(receiver);
		friendRequest.setStatus("PENDING");
		return friendRequestRepository.save(friendRequest);
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
