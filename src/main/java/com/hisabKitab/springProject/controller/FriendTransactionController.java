package com.hisabKitab.springProject.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.TransactionDetailsDto;
import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.service.TransactionService;
import com.hisabKitab.springProject.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class FriendTransactionController {

    @Autowired
	private UserService userService;
    
    @Autowired
    private TransactionService transactionService;
    
    
    @PostMapping("/friendTransactions")
    public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction){
    	
    	Transaction savedTransaction = transactionService.saveTransaction(transaction);
    	
    	return ResponseEntity.ok(savedTransaction);
    	
    }
    
    @PutMapping("/updatefriendTransactions")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction){
    	
    	Transaction updatedTransaction = transactionService.updateTransaction(transaction);
    	
    	return ResponseEntity.ok(updatedTransaction);
    	
    }
    
    @GetMapping("/getAllTransactionWithFriend")
    public ResponseEntity<List<TransactionDetailsDto>> getAllTransactionWithFriend(@RequestParam("userId") Long userId, @RequestParam("friendId") Long friendId){
    	var transactions = transactionService.getAllTransactionWithFriend(userId,friendId);
    	
    	List<TransactionDetailsDto> td = new ArrayList<>();
    	var lastClosingBalance = 0.0D;
    	
    	for(Transaction t:transactions) {
    		if(t.getFromUserId()==userId) {
    			lastClosingBalance += t.getAmount();
    			
    			td.add(new TransactionDetailsDto(t,lastClosingBalance));
    		} else {
    			lastClosingBalance -= t.getAmount();
    			td.add(new TransactionDetailsDto(t,lastClosingBalance));
    		}
    		
    	}
    	Collections.reverse(td);
    	
    	
    	
    	return ResponseEntity.ok(td);
    }
    
    

}
