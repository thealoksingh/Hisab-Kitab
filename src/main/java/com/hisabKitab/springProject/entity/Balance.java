package com.hisabKitab.springProject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastTransactionDate;

    @Column(name = "net_balance", nullable = false)
    private double netBalance;

    public Balance(Long id, Long userId, Long friendId, LocalDateTime lastTransactionDate, double netBalance) {
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

    public LocalDateTime getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(double netBalance) {
        this.netBalance = netBalance;
    }

	@Override
	public String toString() {
		return "Balance [id=" + id + ", userId=" + userId + ", friendId=" + friendId + ", lastTransactionDate="
				+ lastTransactionDate + ", netBalance=" + netBalance + "]";
	}
}
