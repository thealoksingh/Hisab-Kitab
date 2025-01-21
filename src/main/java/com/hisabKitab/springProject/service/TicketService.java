package com.hisabKitab.springProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisabKitab.springProject.entity.Ticket;
import com.hisabKitab.springProject.repository.TicketRepository;

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
        return ticketRepository.findByUserId(userId);
    }

    public Ticket updateTicket(Long ticketId, String status, String description) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (status != null) {
            ticket.setStatus(status);
        }
        if (description != null) {
            ticket.setDescription(description);
        }
        return ticketRepository.save(ticket);
    }
}

