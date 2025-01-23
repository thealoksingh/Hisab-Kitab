package com.hisabKitab.springProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class FriendRequestCountService {
	
	@Autowired
	@Lazy
	private FriendRequestService friendRequestService;
	

	
	public Integer friendRequestCount(Long userId) {
		
		return  friendRequestService.getAllPendingRequests(userId).size() + friendRequestService.getAllSentRequests(userId).size();
		 
	}
	
	

}
