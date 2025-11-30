package com.hisabKitab.springProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.FriendRequestEntity;

@Service
public class FriendRequestNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendFriendRequestNotification(FriendRequestEntity data) {
        // Broadcast to /topic/friend-requests/{userId}
        messagingTemplate.convertAndSend("/topic/friend-requests/" + data.getSender().getUserId(), data);
        messagingTemplate.convertAndSend("/topic/friend-requests/" + data.getReceiver().getUserId(), data);
    }
}
