package com.hisabKitab.springProject.repository;


import com.hisabKitab.springProject.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository <Notification,Long> {


    List<Notification> findByUser_UserIdAndSeen(long userId, boolean b);

    List<Notification> findByUser_UserId(long userId);
}
