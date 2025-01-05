package com.hisabKitab.springProject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

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
    
    

    public Transaction() {
		
	}

    
	public Transaction(Long transId, Long fromUserId, Long toUserId, Double amount, LocalDate transDate,
			String description) {
		this.transId = transId;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.amount = amount;
		this.transDate = transDate;
		this.description = description;
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
}
