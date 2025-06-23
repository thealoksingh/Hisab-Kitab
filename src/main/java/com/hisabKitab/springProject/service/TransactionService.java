package com.hisabKitab.springProject.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.Balance;
import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.exception.UnAuthorizedException;
import com.hisabKitab.springProject.repository.BalanceRepository;
import com.hisabKitab.springProject.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private BalanceRepository balanceRepository;

	@Autowired
	private BalanceService balanceService;

	public Transaction saveTransaction(UserEntity user, Transaction transaction) throws UnAuthorizedException {

		if(user.getUserId() != transaction.getFromUserId() && user.getUserId() != transaction.getToUserId()) throw new UnAuthorizedException("Transaction details is not related to authenticated user");
		if(transaction.getCreatedBy() != transaction.getFromUserId() && transaction.getCreatedBy() != transaction.getToUserId()) throw new BadCredentialsException("Transaction Created by user is not involved in from or to user.");
		var savedTransaction = transactionRepository.save(transaction);

		balanceService.saveBalanceDetails(transaction);

		return savedTransaction;
		
	}

	public Transaction updateTransaction(UserEntity user, Transaction transaction) throws UnAuthorizedException {
		
		if(user.getUserId() != transaction.getFromUserId() && user.getUserId() != transaction.getToUserId()) throw new UnAuthorizedException("Transaction details is not related to authenticated user");
		if(transaction.getCreatedBy() != transaction.getFromUserId() && transaction.getCreatedBy() != transaction.getToUserId()) throw new BadCredentialsException("Transaction Created by user is not involved in from or to user.");

		var oldTransaction = transactionRepository.findById(transaction.getTransId());
		if(oldTransaction.isPresent()) {
			var netAmount = transaction.getAmount()-oldTransaction.get().getAmount();
			if(oldTransaction.get().getFromUserId() != transaction.getFromUserId()) {
				netAmount =  transaction.getAmount()+oldTransaction.get().getAmount();
				balanceService.updateBalance(transaction.getFromUserId(), transaction.getToUserId(),  (netAmount));
			}else {
				balanceService.updateBalance(transaction.getFromUserId(), transaction.getToUserId(),  (netAmount));
				
			}
		} else {
			throw new EntityNotFoundException("Transaction not found with ID: " + transaction.getTransId());
		}
		
		return transactionRepository.save(transaction);
	}


	public List<Transaction> getAllTransactionWithFriend(Long userId, Long friendId) {
		return transactionRepository.findAllTransactionWithFriend(userId, friendId);

	}
	
	@Transactional
    public void deleteTransaction(UserEntity user, Long transactionId) {
        // Fetch transaction
        Transaction transaction = transactionRepository.findByTransIdAndUserId(transactionId, user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User's Transaction not found with ID: " + transactionId));

        Long fromUserId = transaction.getFromUserId();
        Long toUserId = transaction.getToUserId();
        double amount = transaction.getAmount();

        // Update balance for both users
        balanceService.deleteBalance(fromUserId, toUserId, amount);

        // Delete the transaction (cascade removes comments as well)
        transactionRepository.delete(transaction);
    }
						
	 public List<Transaction> getTransactionsByDateRange(Long userId, Long friendId, LocalDate fromDate, LocalDate toDate) {
	        return transactionRepository.findTransactionsBetweenUsersAndDateRange(userId, friendId, fromDate, toDate);
	    }
	 public List<Balance> getTransactionsByUserId(Long userId) {
	        return (List<Balance>) balanceRepository.findByUserId(userId);
	    }

}
