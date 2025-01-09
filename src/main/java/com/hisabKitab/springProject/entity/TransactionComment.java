package com.hisabKitab.springProject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TransactionComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id") // Use snake_case for database naming consistency
    private long commentId;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false) // Foreign key to Transaction table
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to UserEntity table
    private UserEntity user;

    @Column(nullable = false)
    private String comment;

    @Column(name = "comment_time", nullable = false)
    private LocalDateTime commentTime;
    
   
    public TransactionComment() {
    	
	}

	public TransactionComment(long commentId, Transaction transaction, UserEntity user, String comment,
			LocalDateTime commentTime) {
		this.commentId = commentId;
		this.transaction = transaction;
		this.user = user;
		this.comment = comment;
		this.commentTime = commentTime;
	}



	// Getters and Setters
    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(LocalDateTime commentTime) {
        this.commentTime = commentTime;
    }
}

