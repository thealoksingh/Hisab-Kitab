package com.hisabKitab.springProject.service;

import com.hisabKitab.springProject.dto.NotificationRequestDto;
import com.hisabKitab.springProject.entity.Notification;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private NotificationRepository notificationRepository ;

    @Autowired
    private UserService userService;

      @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public Notification save(NotificationRequestDto notificationData) {
        Notification notification = new Notification();
        notification.setDescription(notificationData.getDescription());
        notification.setTitle(notificationData.getTitle());
        notification.setUser(userService.findUserById(notificationData.getUserId()));

       Notification saved = notificationRepository.save(notification);

        // Broadcast to user via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + notificationData.getUserId(), saved);

        return saved;


    }

    public List<Notification> getFilteredNotification(long userId, String status) throws BadRequestException {
      UserEntity user =  userService.findUserById(userId);

      if(status.equalsIgnoreCase("seen")){
          return notificationRepository.findByUser_UserIdAndSeen(userId ,true);
      }else if(status.equalsIgnoreCase("unseen")){
          return notificationRepository.findByUser_UserIdAndSeen(userId ,false);
      }else if (status.equalsIgnoreCase("all")) {
          return notificationRepository.findByUser_UserId(userId );
        }
      else{
          throw new BadRequestException("Invalid Status Entered");
      }
    }


    public Notification findNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification with ID " + id + " not found"));
    }


    public Notification updateNotification(NotificationRequestDto notificationData, long id) {
    UserEntity user = userService.findUserById(notificationData.getUserId());
    Notification oldNotification = findNotificationById(id);

    if (notificationData.getDescription() != null && !notificationData.getDescription().trim().isEmpty()) {
        logger.info("description is not null");
        oldNotification.setDescription(notificationData.getDescription());
    }
    if (notificationData.getTitle() != null && !notificationData.getTitle().trim().isEmpty()) {
        logger.info("title is not null");
        oldNotification.setTitle(notificationData.getTitle());
    }
    if (notificationData.getStatus() != null && !notificationData.getStatus().trim().isEmpty()) {
        oldNotification.setSeen(notificationData.getStatus().equalsIgnoreCase("seen"));
    }

    logger.info(oldNotification + "");
    Notification updated = notificationRepository.save(oldNotification);

        // Broadcast update to user via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + notificationData.getUserId(), updated);

        return updated;
}

    public void deleteNotification(long id) {
        Notification oldNotification = findNotificationById(id);
        notificationRepository.deleteById(id);
    }

}
