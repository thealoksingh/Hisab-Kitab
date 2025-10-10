package com.hisabKitab.springProject.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.hisabKitab.springProject.HisabKitabApplication;
import com.hisabKitab.springProject.dto.CommentRequestDto;
import com.hisabKitab.springProject.dto.CommentResponseDto;
import com.hisabKitab.springProject.dto.CommonResponseDto;
import com.hisabKitab.springProject.entity.TransactionComment;
import com.hisabKitab.springProject.service.CommentService;
import com.hisabKitab.springProject.service.UserService;
import com.hisabKitab.springProject.utils.ResponseBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import java.util.Collections;
import java.time.Duration;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class TransactionCommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;

	@Autowired
	private KafkaTemplate<String, CommentResponseDto> kafkaTemplate;
	
	@Autowired
	private AdminClient adminClient;

	private static final Logger logger = LoggerFactory.getLogger(HisabKitabApplication.class);

	@PostMapping("/transaction/comment/save")
	public ResponseEntity<CommonResponseDto<CommentResponseDto>> saveComment(
			@RequestBody CommentRequestDto commentRequest) {
		var user = userService.getUserFromToken();
		var newComment = commentService.saveComment(user, commentRequest);

		if (newComment != null) {
			// Send to Kafka asynchronously using existing topic with transaction ID as key
			CompletableFuture.runAsync(() -> {
				try {
					String topic = "transaction-comments";
					String key = "trans-" + commentRequest.getTransactionId();
					logger.info("Sending to Kafka topic: {} with key: {}", topic, key);
					
					kafkaTemplate.send(topic, key, newComment);
					logger.info("Comment sent successfully to Kafka");
				} catch (Exception e) {
					logger.error("Kafka send failed: {}", e.getMessage());
				}
			});
			return ResponseBuilder.success(HttpStatus.CREATED, "Comment saved successfully", newComment);
		}

		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Failed to save comment");
	}

	@GetMapping("/transaction/getAllComments")
	public ResponseEntity<CommonResponseDto<List<CommentResponseDto>>> getAllTransactionComments(
			@RequestParam("transId") long transId) {

		// var transactransactionService.findTransactionById(transId);
		var user = userService.getUserFromToken();
		var comments = commentService.getCommentsByTransactionId(user.getUserId(), transId);

		if (comments != null) {
			return ResponseBuilder.success(HttpStatus.OK, "Comments retrieved successfully", comments);
		}
		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Failed to retrieve comments");
	}

	@DeleteMapping("/transaction/comment/{commentId}")
	public ResponseEntity<CommonResponseDto<String>> deleteCommentById(@PathVariable("commentId") Long commentId) {
		var user = userService.getUserFromToken();
		commentService.deleteById(user.getUserId(), commentId);

		return ResponseBuilder.success(HttpStatus.OK, "Comment Deleted Successfully", null);

	}

}
