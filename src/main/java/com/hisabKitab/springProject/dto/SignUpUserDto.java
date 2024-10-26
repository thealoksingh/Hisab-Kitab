package com.hisabKitab.springProject.dto;

public class SignUpUserDto {
	
	private String fullName;
	private String email;
	private String password;
	private String contactNo;
	private String role; // Add this field
	
	public SignUpUserDto() {
	}

	public SignUpUserDto(String fullName, String email, String password, String contactNo, String role) {
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.contactNo = contactNo;
		this.role = role; // Initialize the role
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

	public String getRole() {
		return role; // Getter for role
	}

	public void setRole(String role) {
		this.role = role; // Setter for role
	}

	@Override
	public String toString() {
		return "SignUpUserDto [fullName=" + fullName + ", email=" + email + ", password=" + password 
			+ ", contactNo=" + contactNo + ", role=" + role + "]"; // Include role in toString
	}
}
