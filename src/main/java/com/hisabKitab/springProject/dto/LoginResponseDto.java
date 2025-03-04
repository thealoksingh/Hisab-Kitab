package com.hisabKitab.springProject.dto;

public class LoginResponseDto {
	private Long userId;
	private String fullName;
	private String contactNo;
	private String accessToken;
	private String refreshToken;
	
	public LoginResponseDto() {
	}
	public LoginResponseDto(Long userId, String fullName, String contactNo, String accessToken, String refreshToken) {
		this.userId = userId;
		this.fullName = fullName;
		this.contactNo = contactNo;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	@Override
	public String toString() {
		return "LoginResponseDto [fullName=" + fullName + ", contactNo=" + contactNo + ", accessToken=" + accessToken + ", refreshToken=" + refreshToken + "]";
	}
	

}
