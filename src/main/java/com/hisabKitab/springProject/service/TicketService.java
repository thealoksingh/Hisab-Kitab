package com.hisabKitab.springProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.Ticket;
import com.hisabKitab.springProject.repository.TicketRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket createTicket(String title, String description, Long userId) {
        Ticket ticket = new Ticket();
        ticket.setIssueTitle(title);
        ticket.setDescription(description);
        ticket.setUserId(userId);
        ticket.setStatus("OPEN"); // Default status
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
    	return ticketRepository.findByUserIdAndStatusNot(userId, "DELETED BY USER");
    }

    public Ticket updateTicket(Long userId, Long ticketId, String status, String description) {
        Ticket ticket = ticketRepository.findByTicketIdAndUserId(ticketId,userId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (status != null) {
            ticket.setStatus(status);
        }
        if (description != null && description.trim().equals("")) {
            ticket.setDescription(description);
        }
        return ticketRepository.save(ticket);
    }

	
	public Ticket deleteTicket(Long userId, Long ticketId) {
		 Ticket ticket = ticketRepository.findByTicketIdAndUserId(ticketId,userId)
	                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
		
			ticket.setStatus("DELETED BY USER");
			ticketRepository.save(ticket);
			return ticket;
	
	}
}

