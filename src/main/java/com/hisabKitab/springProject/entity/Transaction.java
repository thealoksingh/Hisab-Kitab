package com.hisabKitab.springProject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transID")
    private Long transId;

    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;

    @Column(name = "to_user_id", nullable = false)
    private Long toUserId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "trans_date", nullable = false)
    private LocalDate transDate;

    @Column(name = "description")
    private String description;


    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TransactionComment> comments;

    public Transaction() {
    }

    public Transaction(Long transId, Long fromUserId, Long toUserId, Double amount, LocalDate transDate,
                       String description, Long createdBy) {
        this.transId = transId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.transDate = transDate;
        this.description = description;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDate transDate) {
        this.transDate = transDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public List<TransactionComment> getComments() {
        return comments;
    }

    public void setComments(List<TransactionComment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Transaction [transId=" + transId + ", fromUserId=" + fromUserId + ", toUserId=" + toUserId + ", amount="
                + amount + ", transDate=" + transDate + ", description=" + description + ", createdBy=" + createdBy
                + ", comments=" + comments + "]";
    }
    

}
