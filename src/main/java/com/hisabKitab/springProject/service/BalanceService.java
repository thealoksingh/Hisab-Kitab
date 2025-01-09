package com.hisabKitab.springProject.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
	        userLastClosingBalance.setLastTransactionDate(LocalDateTime.now());
	        balanceRepository.save(userLastClosingBalance); // Save the updated balance
	    } else {
	        balanceRepository.save(new Balance(null, transaction.getFromUserId(), transaction.getToUserId(), LocalDateTime.now(), transaction.getAmount()));
	    }

	    // Update the balance for the friend
	    var friendLastClosingBalance = balanceRepository.findByFriendIdAndUserId(transaction.getFromUserId(), transaction.getToUserId());
	    if (friendLastClosingBalance != null) {
	        friendLastClosingBalance.setNetBalance(friendLastClosingBalance.getNetBalance() - transaction.getAmount());
	        friendLastClosingBalance.setLastTransactionDate(LocalDateTime.now());
	        balanceRepository.save(friendLastClosingBalance); // Save the updated balance
	    } else {
	        balanceRepository.save(new Balance(null, transaction.getToUserId(), transaction.getFromUserId(), LocalDateTime.now(), -transaction.getAmount()));
	    }
	    
	   
	}
	
	public Double getNetBalanceDetail(Long userId, Long friendId) {
		var isBalanceDetailExist = balanceRepository.existsByUserIdAndFriendId(userId,friendId);
		
		return isBalanceDetailExist ? balanceRepository.findByUserIdAndFriendId(userId,friendId).getNetBalance():0D;
	}
	
	public LocalDateTime getLastTransactionDateDetail(Long userId, Long friendId) {
		var isBalanceDetailExist = balanceRepository.existsByUserIdAndFriendId(userId,friendId);
		
		return isBalanceDetailExist ? balanceRepository.findByUserIdAndFriendId(userId,friendId).getLastTransactionDate():null;
	}
	
	public void updateBalance(Long fromUserId, Long toUserId, double amount) {
	    // Adjust balance for the sender
	    Balance fromBalance = balanceRepository.findByUserIdAndFriendId(fromUserId, toUserId);
	    if (fromBalance != null) {
	        fromBalance.setNetBalance(fromBalance.getNetBalance() - amount);
	        fromBalance.setLastTransactionDate(LocalDateTime.now()); // Update to reflect no recent transaction
	        balanceRepository.save(fromBalance);
	    }

	    // Adjust balance for the receiver
	    Balance toBalance = balanceRepository.findByFriendIdAndUserId(fromUserId, toUserId);
	    if (toBalance != null) {
	        toBalance.setNetBalance(toBalance.getNetBalance() + amount);
	        toBalance.setLastTransactionDate(LocalDateTime.now());
	        balanceRepository.save(toBalance);
	    }
	}


}
