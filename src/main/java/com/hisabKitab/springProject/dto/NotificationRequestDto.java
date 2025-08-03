package com.hisabKitab.springProject.dto;


import com.hisabKitab.springProject.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationRequestDto {

    private static final Logger log = LoggerFactory.getLogger(NotificationRequestDto.class);
    private long userId ;
    private String title ;
    private String description ;
    private String status ;

    public NotificationRequestDto(String title, String description, String status, long userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.userId = userId;
    }

    public NotificationRequestDto() {
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NotificationRequestDto{" +
                "userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
