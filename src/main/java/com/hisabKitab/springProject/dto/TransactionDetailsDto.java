package com.hisabKitab.springProject.dto;

import com.hisabKitab.springProject.entity.Transaction;

public class TransactionDetailsDto {
	
	private Transaction transaction;
	
	private double lastClosingBalance;
	
	public TransactionDetailsDto() {
	}

	public TransactionDetailsDto(Transaction transaction, double lastClosingBalance) {
		this.transaction = transaction;
		this.lastClosingBalance = lastClosingBalance;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public double getLastClosingBalance() {
		return lastClosingBalance;
	}

	public void setLastClosingBalance(double lastClosingBalance) {
		this.lastClosingBalance = lastClosingBalance;
	}

	@Override
	public String toString() {
		return "TransactionDetailsDto [transaction=" + transaction + ", lastClosingBalance=" + lastClosingBalance + "]";
	}
	
	

}
