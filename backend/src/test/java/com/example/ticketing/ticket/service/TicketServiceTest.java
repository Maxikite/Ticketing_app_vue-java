package com.example.ticketing.ticket.service;

import com.example.ticketing.ticket.model.Ticket;
import com.example.ticketing.ticket.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
