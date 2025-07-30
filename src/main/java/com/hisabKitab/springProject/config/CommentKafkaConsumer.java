package com.hisabKitab.springProject.config;

import com.hisabKitab.springProject.dto.CommentResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class CommentKafkaConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topicPattern = "transaction-comments-.*", groupId = "hisab-comment-group")
    public void listen(CommentResponseDto comment,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        Long transactionId = comment.getTransactionId();
        String destination = "/topic/transaction/" + transactionId;

        messagingTemplate.convertAndSend(destination, comment);
    }
}

