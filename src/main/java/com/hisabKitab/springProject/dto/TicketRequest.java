package com.hisabKitab.springProject.dto;

public class TicketRequest {

    private Long userId;
    private String title;
    private String description;
	public TicketRequest() {
		// TODO Auto-generated constructor stub
	}
	public TicketRequest(Long userId, String title, String description) {
		this.userId = userId;
		this.title = title;
		this.description = description;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "TicketRequest [userId=" + userId + ", title=" + title + ", description=" + description + "]";
	}

    
}

