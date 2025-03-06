package com.hisabKitab.springProject.dto;

public class LoginResponseDto {
	private Long userId;
	private String fullName;
	private String contactNo;
	private String accessToken;
	private String refreshToken;
	private String colorHexValue;
	
	public LoginResponseDto() {
	}
	public LoginResponseDto(Long userId, String fullName, String contactNo, String accessToken, String refreshToken, String colorHexValue) {
		this.userId = userId;
		this.fullName = fullName;
		this.contactNo = contactNo;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.colorHexValue = colorHexValue;
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

	public String getColorHexValue() {
		return colorHexValue;
	}
	public void setColorHexValue(String colorHexValue) {
		this.colorHexValue = colorHexValue;
	}
	@Override
	public String toString() {
		return "LoginResponseDto [userId=" + userId + ", fullName=" + fullName + ", contactNo=" + contactNo
				+ ", accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", colorHexValue=" + colorHexValue
				+ "]";
	}
	
	
}
