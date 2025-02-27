package com.hisabKitab.springProject.dto;

public class LoginResponseDto {
	private String fullName;
	private String contactNo;
	private String token;
	
	public LoginResponseDto() {
	}
	public LoginResponseDto(String fullName, String contactNo, String token) {
		this.fullName = fullName;
		this.contactNo = contactNo;
		this.token = token;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "LoginResponseDto [fullName=" + fullName + ", contactNo=" + contactNo + ", token=" + token + "]";
	}
	

}
