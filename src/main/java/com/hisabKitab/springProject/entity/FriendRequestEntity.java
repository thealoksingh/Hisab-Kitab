package com.hisabKitab.springProject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "friend_requests")
public class FriendRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false) // Foreign key to UserEntity
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false) // Foreign key to UserEntity
    private UserEntity receiver;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate = LocalDateTime.now();

    public FriendRequestEntity() {
    }

    public FriendRequestEntity(Long id, UserEntity sender, UserEntity receiver, String status, LocalDateTime requestDate) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.requestDate = requestDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "FriendRequestEntity [id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", status=" + status
                + ", requestDate=" + requestDate + "]";
    }
}
