package com.hisabKitab.springProject.dto;

import com.hisabKitab.springProject.entity.FriendRequestEntity;

public class FriendRequestResponse {
    private FriendRequestStatus status;
    private FriendRequestEntity friendRequest;

    public FriendRequestResponse(FriendRequestStatus status, FriendRequestEntity friendRequest) {
        this.status = status;
        this.friendRequest = friendRequest;
    }

    // Getters and setters
    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    public FriendRequestEntity getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequestEntity friendRequest) {
        this.friendRequest = friendRequest;
    }
}
