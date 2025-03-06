package com.hisabKitab.springProject.dto;

public class CommentRequestDto {

    private Long transactionId; // ID of the transaction
    private String comment; // The actual comment text
    private String commentTime;

    public CommentRequestDto(Long transactionId, String comment, String commentTime) {
        this.transactionId = transactionId;
        this.comment = comment;
        this.commentTime = commentTime;
    }

    // Getters and Setters
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

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "CommentRequestDto [transactionId=" + transactionId + ", comment=" + comment + ", commentTime="
                + commentTime + "]";
    }

    
}
