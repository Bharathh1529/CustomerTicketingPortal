package com.example.customerticketingportal.Service;

import com.example.customerticketingportal.Model.Enums.TicketStatus;
import com.example.customerticketingportal.Repository.TicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SlaMonitorService {

    private final TicketRepository ticketRepo;

    public SlaMonitorService(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @Scheduled(fixedRate = 60000)
    public void checkSlaBreaches() {

        List<TicketStatus> activeStatuses = List.of(
                TicketStatus.NEW,
                TicketStatus.OPEN,
                TicketStatus.ASSIGNED,
                TicketStatus.RESOLVED
        );

        var tickets = ticketRepo.findByStatusIn(activeStatuses);
        var now = LocalDateTime.now();

        for (var ticket : tickets) {

            boolean changed = false;

            if (ticket.getFirstResponseDueAt() != null
                    && ticket.getFirstRespondedAt() == null
                    && now.isAfter(ticket.getFirstResponseDueAt())
                    && !ticket.isFirstResponseBreached()) {

                ticket.setFirstResponseBreached(true);
                ticket.setEscalationLevel(1);
                changed = true;
            }

            if (ticket.getResolutionDueAt() != null
                    && ticket.getResolvedAt() == null
                    && now.isAfter(ticket.getResolutionDueAt())
                    && !ticket.isResolutionBreached()) {

                ticket.setResolutionBreached(true);
                ticket.setEscalationLevel(2);
                changed = true;
            }

            if (changed) {
                ticket.setUpdatedAt(now);
                ticketRepo.save(ticket);
            }
        }
    }
}