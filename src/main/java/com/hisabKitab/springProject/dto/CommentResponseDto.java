package com.hisabKitab.springProject.dto;

import java.time.LocalDateTime;

public class CommentResponseDto {
	
	private String userFullName;
	private String comments;
	private LocalDateTime commentTime;
	public CommentResponseDto() {
		// TODO Auto-generated constructor stub
	}
	public CommentResponseDto(String userFullName, String comments, LocalDateTime commentTime) {
		this.userFullName = userFullName;
		this.comments = comments;
		this.commentTime = commentTime;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public LocalDateTime getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(LocalDateTime commentTime) {
		this.commentTime = commentTime;
	}
	@Override
	public String toString() {
		return "CommentResponseDto [userFullName=" + userFullName + ", comments=" + comments + ", commentTime="
				+ commentTime + "]";
	}
	
	

}
