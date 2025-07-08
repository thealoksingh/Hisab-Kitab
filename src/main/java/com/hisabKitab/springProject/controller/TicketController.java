package com.hisabKitab.springProject.controller;

import java.util.List;

import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.service.UserService;
import com.hisabKitab.springProject.utils.ResponseBuilder;

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
import com.hisabKitab.springProject.dto.TicketRequest;
import com.hisabKitab.springProject.entity.Ticket;
import com.hisabKitab.springProject.service.TicketService;

@RestController
@RequestMapping("/user/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

	@Autowired
	private UserService userService;

	@Autowired
	private TicketService ticketService;

	@PostMapping
	public ResponseEntity<CommonResponseDto<Ticket>> createTicket(@RequestBody TicketRequest ticketRequest) {
		var user = userService.getUserFromToken();
		Ticket createdTicket = ticketService.createTicket(ticketRequest.getTitle(), ticketRequest.getDescription(),
				user.getUserId());
		return ResponseBuilder.success(HttpStatus.CREATED, "Ticket created successfully", createdTicket);
	}

	@GetMapping("/all")
	public ResponseEntity<CommonResponseDto<List<Ticket>>> getTicketsByUserId() {
		UserEntity user = userService.getUserFromToken();
		List<Ticket> tickets = ticketService.getTicketsByUserId(user.getUserId());
		return ResponseBuilder.success(HttpStatus.OK, "Tickets retrieved successfully", tickets);
	}

	@PutMapping("/{ticketId}")
	public ResponseEntity<CommonResponseDto<Ticket>> updateTicket(@PathVariable Long ticketId,
			@RequestParam(required = false) String status, @RequestParam(required = false) String description) {
		UserEntity user = userService.getUserFromToken();
		Ticket updatedTicket = ticketService.updateTicket(user.getUserId(), ticketId, status, description);
		return ResponseBuilder.success(HttpStatus.OK, "Ticket updated successfully", updatedTicket);
	}

	@DeleteMapping("/{ticketId}")
	public ResponseEntity<CommonResponseDto<String>> deleteTicket(@PathVariable Long ticketId) {

		UserEntity user = userService.getUserFromToken();

		var ticket = ticketService.deleteTicket(user.getUserId(),ticketId);

		if (ticket != null) {
			return ResponseBuilder.success(HttpStatus.OK, "Ticket deleted successfully", null);
		}
		return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, "Ticket with id = " + ticketId + " not exists");
	}
}
