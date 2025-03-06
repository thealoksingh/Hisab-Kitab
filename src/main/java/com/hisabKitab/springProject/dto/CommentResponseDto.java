package com.hisabKitab.springProject.dto;

import java.time.LocalDateTime;

public class CommentResponseDto {
	
	private Long commentId;
	private Long userId;
	private String colorHexValue;
	private String userFullName;
	private String comments;
	private LocalDateTime commentTime;
	public CommentResponseDto() {
	}
	public CommentResponseDto(String userFullName, String comments, LocalDateTime commentTime) {
		this.userFullName = userFullName;
		this.comments = comments;
		this.commentTime = commentTime;
	}
	
	
	public CommentResponseDto(Long commentId, Long userId, String colorHexValue, String userFullName, String comments,
			LocalDateTime commentTime) {
		this.commentId = commentId;
		this.userId = userId;
		this.colorHexValue = colorHexValue;
		this.userFullName = userFullName;
		this.comments = comments;
		this.commentTime = commentTime;
	}
	public Long getCommentId() {
		return commentId;
	}
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getColorHexValue() {
		return colorHexValue;
	}
	public void setColorHexValue(String colorHexValue) {
		this.colorHexValue = colorHexValue;
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
