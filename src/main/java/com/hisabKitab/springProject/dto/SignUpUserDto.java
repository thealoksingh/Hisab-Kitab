package com.hisabKitab.springProject.dto;

public class SignUpUserDto {
	
	private String fullName;
	private String email;
	private String password;
	private String contactNo;
	
	
	public SignUpUserDto() {
	}


	public SignUpUserDto(String fullName, String email, String password, String contactNo) {
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.contactNo = contactNo;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getContactNo() {
		return contactNo;
	}


	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}


	@Override
	public String toString() {
		return "SignUpUserDto [fullName=" + fullName + ", email=" + email + ", password=" + password + ", contactNo="
				+ contactNo + "]";
	}
	
	
	
	

}
