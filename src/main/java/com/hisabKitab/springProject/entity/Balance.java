package com.hisabKitab.springProject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "balances")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "friend_id", nullable = false)
    private Long friendId;

    @Column(name = "last_transaction_date")
    private LocalDate lastTransactionDate;

    @Column(name = "net_balance", nullable = false)
    private double netBalance;

    public Balance(Long id, Long userId, Long friendId, LocalDate lastTransactionDate, double netBalance) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.lastTransactionDate = lastTransactionDate;
        this.netBalance = netBalance;
    }

    public Balance() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public LocalDate getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDate lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(double netBalance) {
        this.netBalance = netBalance;
    }
}
