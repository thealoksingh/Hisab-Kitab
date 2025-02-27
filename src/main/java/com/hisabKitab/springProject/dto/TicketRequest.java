package com.hisabKitab.springProject.dto;

public class TicketRequest {


    private String title;
    private String description;
	public TicketRequest() {
		// TODO Auto-generated constructor stub
	}
	public TicketRequest( String title, String description) {
		
		this.title = title;
		this.description = description;
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
		return "TicketRequest [title=" + title + ", description=" + description + "]";
	}

    
}

