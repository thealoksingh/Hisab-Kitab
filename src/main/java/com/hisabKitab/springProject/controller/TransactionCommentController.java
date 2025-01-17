package com.hisabKitab.springProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.CommentRequestDto;
import com.hisabKitab.springProject.dto.CommentResponseDto;
import com.hisabKitab.springProject.entity.TransactionComment;
import com.hisabKitab.springProject.service.CommentService;
import com.hisabKitab.springProject.service.TransactionService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class TransactionCommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private TransactionService transactionService;
	
	@PostMapping("/transaction/comment/save")
	public ResponseEntity<TransactionComment> saveComment(@RequestBody CommentRequestDto commentRequest) {
        var newComment =  commentService.saveComment(commentRequest);
        
        if(newComment!=null) {
        	return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
        } return ResponseEntity.badRequest().body(null);
    }
	
	@GetMapping("/transaction/getAllComments")
	public ResponseEntity<List<CommentResponseDto>> getAllTransactionComments(@RequestParam("transId") long transId){
		
//			var transactransactionService.findTransactionById(transId);
		
		var comments = commentService.getCommentsByTransactionId(transId);
		
		if(comments != null) {
			return ResponseEntity.ok(comments);
		} return ResponseEntity.badRequest().body(null);
	}

}
