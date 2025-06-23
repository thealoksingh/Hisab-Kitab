package com.hisabKitab.springProject.dto;

import java.time.LocalDate;

public class CreateTransactionRequestDto {

    private Long fromUserId;

    private Long toUserId;

    private Double amount;

    private LocalDate transDate;

    private String description;


    public CreateTransactionRequestDto() {
    }

    public CreateTransactionRequestDto(Long fromUserId, Long toUserId, Double amount, LocalDate transDate,
            String description ) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.transDate = transDate;
        this.description = description;
       
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

  

    @Override
    public String toString() {
        return "CreateTransactionRequestDto [fromUserId=" + fromUserId + ", toUserId=" + toUserId + ", amount=" + amount
                + ", transDate=" + transDate + ", description=" + description +  "]";
    }


    

}