package com.hisabKitab.springProject.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.CommonResponseDto;
import com.hisabKitab.springProject.dto.CreateTransactionRequestDto;
import com.hisabKitab.springProject.dto.TransactionDetailsDto;
import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.exception.UnAuthorizedException;
import com.hisabKitab.springProject.service.TransactionService;
import com.hisabKitab.springProject.service.UserService;
import com.hisabKitab.springProject.utils.ResponseBuilder;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class FriendTransactionController {

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/friendTransactions")
	public ResponseEntity<CommonResponseDto<Transaction>> saveTransaction(@RequestBody CreateTransactionRequestDto transactionDto) throws UnAuthorizedException {
		var user = userService.getUserFromToken();
		var transaction = new Transaction();
		transaction.setAmount(transactionDto.getAmount());
		transaction.setFromUserId(transactionDto.getFromUserId());
		transaction.setToUserId(transactionDto.getToUserId());
		transaction.setCreatedBy(user.getUserId());
		transaction.setTransDate(transactionDto.getTransDate());
		transaction.setDescription(transactionDto.getDescription());
		
		Transaction savedTransaction = transactionService.saveTransaction(user,transaction);

		return ResponseBuilder.success(HttpStatus.CREATED, "New Transaction added.", savedTransaction);

	}

	@PutMapping("/updatefriendTransactions")
	public ResponseEntity<CommonResponseDto<Transaction>> updateTransaction(@RequestBody Transaction transaction) throws UnAuthorizedException {
		
		var user = userService.getUserFromToken();
		Transaction updatedTransaction = transactionService.updateTransaction(user,transaction);

		return ResponseBuilder.success(HttpStatus.OK, "Transaction updated successfully", updatedTransaction);

	}

	@GetMapping("/getAllTransactionWithFriend")
	public ResponseEntity<CommonResponseDto<List<TransactionDetailsDto>>> getAllTransactionWithFriend(
			@RequestParam("friendId") Long friendId) {
		var user = userService.getUserFromToken();

		var transactions = transactionService.getAllTransactionWithFriend(user.getUserId(), friendId);

		List<TransactionDetailsDto> td = new ArrayList<>();
		var lastClosingBalance = 0.0D;

		for (Transaction t : transactions) {
			if (t.getFromUserId() == user.getUserId()) {
				lastClosingBalance += t.getAmount();

				td.add(new TransactionDetailsDto(t, lastClosingBalance));
			} else {
				lastClosingBalance -= t.getAmount();
				td.add(new TransactionDetailsDto(t, lastClosingBalance));
			}

		}
		Collections.reverse(td);

		return ResponseBuilder.success(HttpStatus.OK, "Transaction retrieved successfully", td);
	}

	@DeleteMapping("/transaction/{transactionId}")
	public ResponseEntity<CommonResponseDto<String>> deleteTransaction(@PathVariable Long transactionId) {
		var user = userService.getUserFromToken();
		transactionService.deleteTransaction(user,transactionId);
		return ResponseBuilder.success(HttpStatus.OK, "Transaction and related data deleted successfully.", null);
	}

	@GetMapping("/transaction/{transactionId}")
	public ResponseEntity<CommonResponseDto<Transaction>> getTransactionById(@PathVariable Long transactionId) {
		var user = userService.getUserFromToken();
		var transaction = transactionService.getTransactionById(user, transactionId);
		return ResponseBuilder.success(HttpStatus.OK, "Transaction retrieved successfully.", transaction);
	}

}
