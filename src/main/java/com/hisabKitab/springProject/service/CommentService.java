package com.hisabKitab.springProject.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentService {
	
	@Autowired
	private TransactionCommentsRepository transactionCommentsRepository;
	
    @Autowired
    private TransactionRepository transactionRepository;
	
	  public TransactionComment saveComment(UserEntity user,CommentRequestDto commentRequest) {
	        // Fetch the user and transaction from the repositories
	       
	        Transaction transaction = transactionRepository.findById(commentRequest.getTransactionId())
	                .orElseThrow(() -> new RuntimeException("Transaction not found"));

	        // Create a new TransactionComments entity
	        TransactionComment comment = new TransactionComment();
	        comment.setUser(user);
	        comment.setTransaction(transaction);
	        comment.setComment(commentRequest.getComment());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss");
            comment.setCommentTime(LocalDateTime.parse(commentRequest.getCommentTime(), formatter)); // Set current time

	        // Save the comment
	        return transactionCommentsRepository.save(comment);
	    }
	
	  public  List<CommentResponseDto> getCommentsByTransactionId(Long userId,Long transactionId) {
		  
		  var transaction = transactionRepository.findByTransIdAndUserId(transactionId, userId).orElseThrow(()-> new EntityNotFoundException("Transaction not found."));
		  
		  List<CommentResponseDto> commentsList = new ArrayList<>();
		  
		  
	        var comments =  transactionCommentsRepository.findByTransaction_TransId(transaction.getTransId());
	        if(comments != null) {
	        	
	        	
	        	for(TransactionComment tc:comments) {
	        		commentsList.add(new CommentResponseDto(tc.getCommentId(), tc.getUser().getUserId(), tc.getUser().getColorHexValue() ,tc.getUser().getFullName(), tc.getComment(), tc.getCommentTime()));
	        	}
	        	
	        	return commentsList;
	        }
	        
	        return null;
	    }
	  
	  public TransactionComment getCommentByIdAndUserId( Long commentId,Long userId) {
	        return transactionCommentsRepository.findByCommentIdAndUser_UserId(commentId, userId)
	                .orElseThrow(() -> new EntityNotFoundException("Your Comment not found with ID: " + commentId));
	    }

	public void deleteById(Long userId, Long commentId) {
		var comment = getCommentByIdAndUserId(commentId, userId);
		transactionCommentsRepository.deleteById(comment.getCommentId());
		
	}

}
