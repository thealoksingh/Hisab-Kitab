package com.hisabKitab.springProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.repository.BalanceRepository;
import com.hisabKitab.springProject.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private BalanceRepository balanceRepository;
	
	@Autowired
	private BalanceService balanceService;
	
	public Transaction saveTransaction(Transaction transaction) {
		
		
    var savedTransaction = transactionRepository.save(transaction);
    
    
     balanceService.saveBalanceDetails(transaction);
    

    return savedTransaction;
}

	public Transaction updateTransaction(Transaction transaction) {
		
		return transactionRepository.save(transaction);
	}

	public List<Transaction> getAllTransactionWithFriend(Long userId, Long friendId) {
		return transactionRepository.findAllTransactionWithFriend(userId, friendId);
		
	}
	
	

}
