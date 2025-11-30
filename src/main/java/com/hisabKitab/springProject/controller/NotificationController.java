package com.hisabKitab.springProject.controller;


import com.hisabKitab.springProject.dto.CommonResponseDto;
import com.hisabKitab.springProject.dto.FriendRequestResponse;
import com.hisabKitab.springProject.dto.NotificationRequestDto;
import com.hisabKitab.springProject.entity.Notification;
import com.hisabKitab.springProject.service.NotificationService;

import com.hisabKitab.springProject.utils.ResponseBuilder;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping ("/user/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService ;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);


    @PostMapping
    public ResponseEntity<CommonResponseDto<Notification>> createNotification(@RequestBody NotificationRequestDto notificationData){
        Notification notification = notificationService.save(notificationData);
        
        // Send WebSocket notification
        try {
            String destination = "/topic/notifications/" + notification.getUser().getUserId();
            messagingTemplate.convertAndSend(destination, notification);
            logger.info("Notification sent via WebSocket to: {}", destination);
        } catch (Exception e) {
            logger.error("WebSocket send failed: {}", e.getMessage(), e);
        }
        
        return ResponseBuilder.success(HttpStatus.CREATED,"New Notification Created",notification);
    }
    @GetMapping("/filter")
    public ResponseEntity<CommonResponseDto<List<Notification>>> getFilteredNotification(@RequestParam long userId ,@RequestParam String status) throws BadRequestException {
        List<Notification> notifications = notificationService.getFilteredNotification(userId,status);
        return ResponseBuilder.success(HttpStatus.OK,"Notification Found Successfully",notifications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponseDto<Notification>> updateNotification (@RequestBody NotificationRequestDto notificationData ,@PathVariable long id) {
        Notification notification = notificationService.updateNotification(notificationData, id);
        
        // Send WebSocket notification for update
        try {
            String destination = "/topic/notifications/" + notification.getUser().getUserId();
            messagingTemplate.convertAndSend(destination, notification);
            logger.info("Notification update sent via WebSocket to: {}", destination);
        } catch (Exception e) {
            logger.error("WebSocket send failed: {}", e.getMessage(), e);
        }
        
        return ResponseBuilder.success(HttpStatus.OK, "Notification Updated Successfully", notification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto<Long>>  deleteNotification (@PathVariable long id){
         notificationService.deleteNotification(id);
        return ResponseBuilder.success(HttpStatus.OK, "Notification Deleted Successfully", id);
    }

}
