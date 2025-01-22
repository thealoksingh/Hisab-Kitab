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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hisabKitab.springProject.dto.TicketRequest;
import com.hisabKitab.springProject.entity.Ticket;
import com.hisabKitab.springProject.service.TicketService;

@RestController
@RequestMapping("/user/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest ticketRequest) {
        Ticket createdTicket = ticketService.createTicket(
                ticketRequest.getTitle(),
                ticketRequest.getDescription(),
                ticketRequest.getUserId()
        );
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUserId(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long ticketId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String description) {

        Ticket updatedTicket = ticketService.updateTicket(ticketId, status, description);
        return ResponseEntity.ok(updatedTicket);
    }
    
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long ticketId){
    	 Ticket deletedTicket = ticketService.deleteTicket(ticketId);
    	 
    	 if(deletedTicket != null) {
    		 return new ResponseEntity<String>("Ticket deleted successfully", HttpStatus.ACCEPTED);
    	 } return new ResponseEntity<String>("Invalid ticket id entered", HttpStatus.BAD_REQUEST);
    	
    }
    
}

