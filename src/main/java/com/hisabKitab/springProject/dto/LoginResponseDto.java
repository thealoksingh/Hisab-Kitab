package com.hisabKitab.springProject.dto;

import com.hisabKitab.springProject.entity.UserEntity;

public class LoginResponseDto {

	private UserEntity user;
	
	private String accessToken;
	private String refreshToken;
	
	public LoginResponseDto() {
	}
	public LoginResponseDto(UserEntity user, String accessToken, String refreshToken) {
		this.user = user;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	

	public UserEntity getUser() {
		return user;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	@Override
	public String toString() {
		return "LoginResponseDto [user=" + user + ", accessToken=" + accessToken + ", refreshToken=" + refreshToken
				+ "]";
	}
}
