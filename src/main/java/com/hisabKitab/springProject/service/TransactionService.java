package com.hisabKitab.springProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.Balance;
import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.repository.BalanceRepository;
import com.hisabKitab.springProject.repository.TransactionRepository;

import jakarta.transaction.Transactional;

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
	
	@Transactional
    public void deleteTransaction(Long transactionId) {
        // Fetch transaction
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + transactionId));

        Long fromUserId = transaction.getFromUserId();
        Long toUserId = transaction.getToUserId();
        double amount = transaction.getAmount();

        // Update balance for both users
        balanceService.updateBalance(fromUserId, toUserId, amount);

        // Delete the transaction (cascade removes comments as well)
        transactionRepository.delete(transaction);
    }

  

}
