package com.example.customerticketingportal.Repository;
import com.example.customerticketingportal.Model.Enums.TicketStatus;

import com.example.customerticketingportal.Model.Enums.TicketStatus;
import com.example.customerticketingportal.Model.Organization;
import com.example.customerticketingportal.Model.Ticket;
import com.example.customerticketingportal.Model.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByRequester(User requester);
    List<Ticket> findByAssignee(User assignee);
    List<Ticket> findByOrganization(Organization organization);
    List<Ticket> findByStatusIn(List<TicketStatus> statuses);
    List<Ticket> findByPriority(String priority);
    List<Ticket> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Ticket> findByRequesterId(Long requesterId);
    List<Ticket> findByAssigneeIsNullOrderByCreatedAtDesc();

    // for unassigned tickets
    @Query("SELECT t FROM Ticket t JOIN FETCH t.requester WHERE t.assignee.id = :agentId ORDER BY t.updatedAt DESC")
    List<Ticket> findByAssigneeId(@Param("agentId") Long agentId);

    // for dashboard stats
    long countByAssigneeId(Long agentId);
    long countByStatusAndAssigneeId(TicketStatus status, Long agentId);
    long countByAssigneeIsNull();
    long countByStatusAndUpdatedAtAfter(TicketStatus status, LocalDateTime time);



}