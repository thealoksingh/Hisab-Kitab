package com.hisabKitab.springProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.FriendRequestEntity;

@Service
public class FriendRequestEventProducer {

    @Autowired
private KafkaTemplate<String, FriendRequestEntity> friendRequestKafkaTemplate;

    public void sendFriendRequestEvent(FriendRequestEntity data) {
        friendRequestKafkaTemplate.send("friend-request-topic", data);
    }
}
