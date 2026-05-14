package com.example.customerticketingportal.Repository;

import com.example.customerticketingportal.Model.Ticket;
import com.example.customerticketingportal.Model.TicketMessage;
import com.example.customerticketingportal.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TicketMessageRepository extends JpaRepository<TicketMessage, Long> {
    @Query("SELECT m FROM TicketMessage m WHERE m.ticket.id = :ticketId ORDER BY m.createdAt ASC")
    List<TicketMessage> findByTicketIdOrderByCreatedAtAsc(@Param("ticketId") String id);
    List<TicketMessage> findByAuthor(User author);
    List<TicketMessage> findByTicketAndIsInternal(Ticket ticket, boolean isInternal);
}
