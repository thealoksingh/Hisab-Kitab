package com.hisabKitab.springProject.dto;

import java.util.List;
import java.util.Set;


public class GetFriendListDto {
	
	private String message;
	private List<UsersFriendEntityDto> friendList;
	private Integer friendRequestCount;
	
	public GetFriendListDto() {
		// TODO Auto-generated constructor 
	}
	public GetFriendListDto(String message, List<UsersFriendEntityDto> friendList) {
		this.message = message;
		this.friendList = friendList;
	}
	
	
	public GetFriendListDto(String message, List<UsersFriendEntityDto> friendList, Integer friendRequestCount) {
		this.message = message;
		this.friendList = friendList;
		this.friendRequestCount = friendRequestCount;
	}
	
	
	public Integer getFriendRequestCount() {
		return friendRequestCount;
	}
	public void setFriendRequestCount(Integer friendRequestCount) {
		this.friendRequestCount = friendRequestCount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<UsersFriendEntityDto> getFriendList() {
		return friendList;
	}
	public void setFriendList(List<UsersFriendEntityDto> friendList) {
		this.friendList = friendList;
	}
	@Override
	public String toString() {
		return "GetFriendListDto [message=" + message + ", friendList=" + friendList + "]";
	}
	

}
