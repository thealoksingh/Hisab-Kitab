package com.hisabKitab.springProject.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.dto.CommentRequestDto;
import com.hisabKitab.springProject.dto.CommentResponseDto;
import com.hisabKitab.springProject.entity.Transaction;
import com.hisabKitab.springProject.entity.TransactionComment;
import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.TransactionCommentsRepository;
import com.hisabKitab.springProject.repository.TransactionRepository;
import com.hisabKitab.springProject.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService {
	
	@Autowired
	private TransactionCommentsRepository transactionCommentsRepository;
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;
	
	  public TransactionComment saveComment(CommentRequestDto commentRequest) {
	        // Fetch the user and transaction from the repositories
	        UserEntity user = userRepository.findById(commentRequest.getUserId())
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        Transaction transaction = transactionRepository.findById(commentRequest.getTransactionId())
	                .orElseThrow(() -> new RuntimeException("Transaction not found"));

	        // Create a new TransactionComments entity
	        TransactionComment comment = new TransactionComment();
	        comment.setUser(user);
	        comment.setTransaction(transaction);
	        comment.setComment(commentRequest.getComment());
	        comment.setCommentTime(LocalDateTime.now()); // Set current time

	        // Save the comment
	        return transactionCommentsRepository.save(comment);
	    }
	
	  public  List<CommentResponseDto> getCommentsByTransactionId(Long transactionId) {
		  
		  List<CommentResponseDto> commentsList = new ArrayList<>();
		  
		  
	        var comments =  transactionCommentsRepository.findByTransaction_TransId(transactionId);
	        if(comments != null) {
	        	
	        	
	        	for(TransactionComment tc:comments) {
	        		commentsList.add(new CommentResponseDto(tc.getCommentId(), tc.getUser().getUserId(), tc.getUser().getColorHexValue() ,tc.getUser().getFullName(), tc.getComment(), tc.getCommentTime()));
	        	}
	        	
	        	return commentsList;
	        }
	        
	        return null;
	    }
	  
	  public TransactionComment getCommentById(Long commentId) {
	        return transactionCommentsRepository.findById(commentId)
	                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));
	    }

	public void deleteById(Long commentId) {
		var comment = getCommentById(commentId);
		transactionCommentsRepository.deleteById(commentId);
		
	}

}
