package com.hisabKitab.springProject.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.Balance;
import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.repository.BalanceRepository;

@Service
public class BalanceService {
	
	@Autowired
	private BalanceRepository balanceRepository;
	
	public void saveBalanceDetails(Transaction transaction) {
		// Update the balance for the user
	    var userLastClosingBalance = balanceRepository.findByUserIdAndFriendId(transaction.getFromUserId(), transaction.getToUserId());
	    if (userLastClosingBalance != null) {
	        userLastClosingBalance.setNetBalance(userLastClosingBalance.getNetBalance() + transaction.getAmount());
	        userLastClosingBalance.setLastTransactionDate(LocalDate.now());
	        balanceRepository.save(userLastClosingBalance); // Save the updated balance
	    } else {
	        balanceRepository.save(new Balance(null, transaction.getFromUserId(), transaction.getToUserId(), LocalDate.now(), transaction.getAmount()));
	    }

	    // Update the balance for the friend
	    var friendLastClosingBalance = balanceRepository.findByFriendIdAndUserId(transaction.getFromUserId(), transaction.getToUserId());
	    if (friendLastClosingBalance != null) {
	        friendLastClosingBalance.setNetBalance(friendLastClosingBalance.getNetBalance() - transaction.getAmount());
	        friendLastClosingBalance.setLastTransactionDate(LocalDate.now());
	        balanceRepository.save(friendLastClosingBalance); // Save the updated balance
	    } else {
	        balanceRepository.save(new Balance(null, transaction.getToUserId(), transaction.getFromUserId(), LocalDate.now(), -transaction.getAmount()));
	    }
	    
	   
	}
	
	public Double getNetBalanceDetail(Long userId, Long friendId) {
		var isBalanceDetailExist = balanceRepository.existsByUserIdAndFriendId(userId,friendId);
		
		return isBalanceDetailExist ? balanceRepository.findByUserIdAndFriendId(userId,friendId).getNetBalance():0D;
	}
	
	public LocalDate getLastTransactionDateDetail(Long userId, Long friendId) {
		var isBalanceDetailExist = balanceRepository.existsByUserIdAndFriendId(userId,friendId);
		
		return isBalanceDetailExist ? balanceRepository.findByUserIdAndFriendId(userId,friendId).getLastTransactionDate():null;
	}

}
