package com.hisabKitab.springProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.CommentRequestDto;
import com.hisabKitab.springProject.dto.CommentResponseDto;
import com.hisabKitab.springProject.entity.TransactionComment;
import com.hisabKitab.springProject.service.CommentService;
import com.hisabKitab.springProject.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class TransactionCommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private UserService userService;
	

	
	@PostMapping("/transaction/comment/save")
	public ResponseEntity<TransactionComment> saveComment(@RequestBody CommentRequestDto commentRequest) {
		var user = userService.getUserFromToken();
        var newComment =  commentService.saveComment(user, commentRequest);
        
        if(newComment!=null) {
        	return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
        } return ResponseEntity.badRequest().body(null);
    }
	
	@GetMapping("/transaction/getAllComments")
	public ResponseEntity<List<CommentResponseDto>> getAllTransactionComments(@RequestParam("transId") long transId){
		
//		var transactransactionService.findTransactionById(transId);
		var user = userService.getUserFromToken();
		var comments = commentService.getCommentsByTransactionId(user.getUserId(),transId);
		
		if(comments != null) {
			return ResponseEntity.ok(comments);
		} return ResponseEntity.badRequest().body(null);
	}
	
	@DeleteMapping("/transaction/comment/{commentId}")
	public ResponseEntity<String> deleteCommentById(@PathVariable("commentId")Long commentId){
		var user = userService.getUserFromToken();
		commentService.deleteById(user.getUserId(), commentId);
		
		return ResponseEntity.ok("Comment Deleted Successfully");
		
	}

}
