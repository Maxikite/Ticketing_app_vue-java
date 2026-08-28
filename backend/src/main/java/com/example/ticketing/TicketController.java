package com.example.ticketing;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class TicketController {
    private final TicketRepository repository;

    public TicketController(TicketRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Ticket> getTickets() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Ticket::getCreatedAt).reversed())
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket createTicket(@Valid @RequestBody Ticket ticket) {
        ticket.setPriority(ticket.getPriority() == null ? "MEDIUM" : ticket.getPriority().toUpperCase());
        return repository.save(ticket);
    }

    @PutMapping("/{id}")
    public Optional<Ticket> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket changes) {
        return repository.findById(id).map(ticket -> {
            ticket.setTitle(changes.getTitle());
            ticket.setDescription(changes.getDescription());
            ticket.setRequester(changes.getRequester());
            ticket.setPriority(changes.getPriority() == null ? "MEDIUM" : changes.getPriority().toUpperCase());
            return repository.save(ticket);
        });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
