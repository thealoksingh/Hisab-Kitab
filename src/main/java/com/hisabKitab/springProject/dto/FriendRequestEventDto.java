package com.hisabKitab.springProject.dto;


public class FriendRequestEventDto {
    private Long requestId;
    private Long senderId;
    private Long receiverId;
    private String status;

    public FriendRequestEventDto() {
    }
    public FriendRequestEventDto(Long requestId, Long senderId, Long receiverId, String status) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }
    public Long getRequestId() {
        return requestId;
    }
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendRequestEventDto{" +
                "requestId=" + requestId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", status='" + status + '\'' +
                '}';
    }
}
