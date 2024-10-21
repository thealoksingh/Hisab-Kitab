package com.hisabKitab.springProject.dto;

import java.util.Set;

import com.hisabKitab.springProject.entity.UserEntity;

public class GetFriendListDto {
	
	private String message;
	private Set<UserEntity> friendList;
	public GetFriendListDto() {
		// TODO Auto-generated constructor stub
	}
	public GetFriendListDto(String message, Set<UserEntity> friendList) {
		this.message = message;
		this.friendList = friendList;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Set<UserEntity> getFriendList() {
		return friendList;
	}
	public void setFriendList(Set<UserEntity> friendList) {
		this.friendList = friendList;
	}
	@Override
	public String toString() {
		return "GetFriendListDto [message=" + message + ", friendList=" + friendList + "]";
	}
	

}
