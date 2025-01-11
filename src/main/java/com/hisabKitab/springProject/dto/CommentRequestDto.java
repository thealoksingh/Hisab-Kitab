package com.hisabKitab.springProject.dto;

public class CommentRequestDto {
    private Long userId; // ID of the user making the comment
    private Long transactionId; // ID of the transaction
    private String comment; // The actual comment text

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
