package com.hisabKitab.springProject.dto;

import java.time.LocalDateTime;

import com.hisabKitab.springProject.entity.UserEntity;
						
public class UsersFriendEntityDto {
	
	private UserEntity userEntity;
	
	private Double closingBalance;
	
	 private LocalDateTime lastTransactionDate;

	public UsersFriendEntityDto() {
	}

	public UsersFriendEntityDto(UserEntity userEntity, Double closingBalance, LocalDateTime lastTransactionDate) {
		this.userEntity = userEntity;
		this.closingBalance = closingBalance;
		this.lastTransactionDate = lastTransactionDate;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public Double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public LocalDateTime getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	@Override
	public String toString() {
		return "UsersFriendEntityDto [userEntity=" + userEntity + ", closingBalance=" + closingBalance
				+ ", lastTransactionDate=" + lastTransactionDate + "]";
	}
	 
	 

}
