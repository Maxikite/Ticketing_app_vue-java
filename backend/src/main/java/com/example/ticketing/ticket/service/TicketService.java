package com.example.ticketing.ticket.service;

import com.example.ticketing.ticket.model.Ticket;
import com.example.ticketing.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public List<Ticket> getAllTickets() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Ticket::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    public Ticket createTicket(Ticket ticket) {
        if (ticket.getPriority() == null || ticket.getPriority().isBlank()) {
            ticket.setPriority("MEDIUM");
        }
        ticket.setPriority(ticket.getPriority().toUpperCase(Locale.ROOT));
        return repository.save(ticket);
    }

    public Optional<Ticket> updateTicket(Long id, Ticket changes) {
        return repository.findById(id).map(ticket -> {
            ticket.setTitle(changes.getTitle());
            ticket.setDescription(changes.getDescription());
            ticket.setRequester(changes.getRequester());
            ticket.setPriority(changes.getPriority() == null || changes.getPriority().isBlank()
                    ? "MEDIUM"
                    : changes.getPriority().toUpperCase(Locale.ROOT));
            return repository.save(ticket);
        });
    }

    public void deleteTicket(Long id) {
        repository.deleteById(id);
    }
}
