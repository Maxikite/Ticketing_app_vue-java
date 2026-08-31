package com.example.ticketing.ticket.service;

import com.example.ticketing.ticket.model.Ticket;
import com.example.ticketing.ticket.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketServiceTest {

    @Test
    void shouldReturnTicketsSortedByNewestFirst() {
        TicketRepository repository = Mockito.mock(TicketRepository.class);
        Ticket first = new Ticket();
        first.setTitle("Ancien");

        Ticket second = new Ticket();
        second.setTitle("Nouveau");

        Mockito.when(repository.findAll()).thenReturn(List.of(first, second));

        TicketService service = new TicketService(repository);
        List<Ticket> tickets = service.getAllTickets();

        assertEquals(2, tickets.size());
    }

    @Test
    void shouldUpdateTicketStatus() {
        TicketRepository repository = Mockito.mock(TicketRepository.class);
        Ticket existing = new Ticket();
        existing.setId(1L);
        existing.setTitle("Ancien titre");
        existing.setDescription("Ancienne desc");
        existing.setRequester("Alice");
        existing.setAssignee("Alice Martin");
        existing.setPriority("MEDIUM");
        existing.setStatus("OPEN");

        Ticket changes = new Ticket();
        changes.setTitle("Nouveau titre");
        changes.setDescription("Nouvelle desc");
        changes.setRequester("Bob");
        changes.setAssignee("Lucas Bernard");
        changes.setPriority("HIGH");
        changes.setStatus("IN_PROGRESS");

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(repository.save(existing)).thenReturn(existing);

        TicketService service = new TicketService(repository);
        Optional<Ticket> updated = service.updateTicket(1L, changes);

        assertTrue(updated.isPresent());
        assertEquals("IN_PROGRESS", updated.get().getStatus());
        assertEquals("HIGH", updated.get().getPriority());
        assertEquals("Lucas Bernard", updated.get().getAssignee());
    }
}
