package com.example.customerticketingportal.Service;

import com.example.customerticketingportal.Dto.*;
import com.example.customerticketingportal.Exception.BadRequestException;
import com.example.customerticketingportal.Exception.NotFoundException;
import com.example.customerticketingportal.Model.Enums.Priority;
import com.example.customerticketingportal.Model.Enums.Role;
import com.example.customerticketingportal.Model.Enums.TicketStatus;
import com.example.customerticketingportal.Model.Ticket;
import com.example.customerticketingportal.Model.TicketMessage;
import com.example.customerticketingportal.Model.User;
import com.example.customerticketingportal.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {
    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final OrganizationRepository orgRepo;
    private final SlaRepository slaRepo;
    private final TicketMessageRepository messageRepo;

    public TicketService(TicketRepository ticketRepo, UserRepository userRepo, OrganizationRepository orgRepo, SlaRepository slaRepo, TicketMessageRepository messageRepo) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.orgRepo = orgRepo;
        this.slaRepo = slaRepo;
        this.messageRepo = messageRepo;
    }
    @Transactional
    public TicketResponse createTicket(TicketCreateRequest req){
        var org = orgRepo.findById(req.getOrgId()).orElseThrow(
                () -> new NotFoundException("Organization not found: "+ req.getOrgId())
        );
        var requester = userRepo.findById(req.getRequesterId())
                .orElseThrow(() -> new NotFoundException("Requester not found: " + req.getRequesterId()));

        User assignee = null;
        if(req.getAssigneeId() != null){
            assignee = userRepo.findById(req.getAssigneeId()).orElseThrow(
                    () -> new NotFoundException("Assignee not found: "+ req.getAssigneeId())
            );
        }

        var t = new Ticket();
        t.setOrganization(org);
        t.setRequester(requester);
        t.setAssignee(assignee);
        t.setSubject(req.getSubject());
        t.setPriority(
                req.getPriority() != null ?
                        req.getPriority() :  Priority.MEDIUM
        );
        t.setStatus(TicketStatus.NEW);
        var now = LocalDateTime.now();
        t.setCreatedAt(now);
        t.setUpdatedAt(now);

        var activeSlas = slaRepo.findByActive(true);
        if(!activeSlas.isEmpty()){
            t.setSla(activeSlas.get(0));
        }
        t = ticketRepo.save(t);

        var msg = new TicketMessage();
        msg.setTicket(t);
        msg.setAuthor(requester);
        msg.setBody(req.getBody());
        msg.setInternal(false);
        msg.setCreatedAt(LocalDateTime.now());
        messageRepo.save(msg);
        t.setStatus(TicketStatus.OPEN);
        t.setUpdatedAt(LocalDateTime.now());
        t = ticketRepo.save(t);
        if (t.getSla() != null) {
            t.setFirstResponseDueAt(
                    t.getCreatedAt().plusMinutes(t.getSla().getFirstResponseMins())
            );
            t.setResolutionDueAt(
                    t.getCreatedAt().plusMinutes(t.getSla().getResolutoinMins())
            );
        }

        t.setFirstResponseBreached(false);
        t.setResolutionBreached(false);
        t.setEscalationLevel(0);

        t = ticketRepo.save(t);

        return mapToResponse(t, t.getFirstResponseDueAt(), t.getResolutionDueAt());
    }
    public TicketResponse getTicketById(String id){
        var t = ticketRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Ticket Not found: "+ id)
        );

        LocalDateTime frDue = null, resDue = null;
        if(t.getSla() != null){
            frDue = t.getCreatedAt().plusMinutes(t.getSla().getFirstResponseMins());
            resDue =t.getCreatedAt().plusMinutes(t.getSla().getFirstResponseMins());
        }

        return mapToResponse(t, frDue, resDue);
    }

    @Transactional
    public TicketResponse updateTicket(String ticketId, TicketPatchRequest req){
        var t = ticketRepo.findById(ticketId).orElseThrow(
                () -> new NotFoundException("Ticket Not found: " + ticketId)
        );

        if (t.getStatus() == TicketStatus.CLOSED) {
            throw new BadRequestException("Ticket is closed and cannot be updated.");
        }

        if(req.getAssigneeId() != null){
            var assignee = userRepo.findById(req.getAssigneeId()).orElseThrow(
                    () -> new NotFoundException("Assignee Not found: " + req.getAssigneeId())
            );
            t.setAssignee(assignee);
        }
        if (req.getStatus() != null) {
            t.setStatus(req.getStatus());

            if (req.getStatus() == TicketStatus.RESOLVED && t.getResolvedAt() == null) {
                t.setResolvedAt(LocalDateTime.now());
            }
        }


        if (req.getPriority() != null) {
            t.setPriority(req.getPriority());
        }

        t.setUpdatedAt(LocalDateTime.now());
        t = ticketRepo.save(t);

        LocalDateTime frDue = null, resDue = null;
        if (t.getSla() != null) {
            frDue = t.getCreatedAt().plusMinutes(t.getSla().getFirstResponseMins());
            resDue = t.getCreatedAt().plusMinutes(t.getSla().getResolutoinMins());
        }
        return mapToResponse(t, frDue, resDue);
    }
    @Transactional
    public TicketMessageResponse addMessage(String ticketId, TicketMessageCreateRequest req){
        var t = ticketRepo.findById(ticketId).
                orElseThrow(
                        () -> new NotFoundException("Ticket not found: "+ticketId)
                );

        if (t.getStatus() == TicketStatus.CLOSED) {
            throw new BadRequestException("Cannot add messages to a closed ticket.");
        }
            var user = userRepo.findById(req.getAuthorUserId()).
                orElseThrow(
                        () -> new NotFoundException("User not found: "+ticketId)
                );
        var msg = new TicketMessage();
        msg.setTicket(t);
        msg.setAuthor(user);
        msg.setBody(req.getBody());
        msg.setInternal(req.isInternal());
        msg.setWaitOnCustomer(req.isWaitOnCustomer());
        msg.setCreatedAt(LocalDateTime.now());
        messageRepo.save(msg);

        if (!req.isInternal()
                && user.getRole() == Role.AGENT
                && t.getFirstRespondedAt() == null) {
            t.setFirstRespondedAt(LocalDateTime.now());
        }

        if (!req.isInternal()) {
            if (req.isWaitOnCustomer()) {
                t.setStatus(TicketStatus.ASSIGNED);
            } else {
                t.setStatus(TicketStatus.OPEN);
            }

            t.setUpdatedAt(LocalDateTime.now());
            ticketRepo.save(t);
        }

        return mapToMessageResponse(msg);
    }
    @Transactional
    public TicketResponse closeTicket(String ticketId, Long agentId){
        var ticket = ticketRepo.findById(ticketId).orElseThrow(
                () -> new NotFoundException("Ticket not found: "+ticketId)
        );

        var agent = userRepo.findById(agentId).orElseThrow(
                () -> new NotFoundException("User not found: "+ agentId)
        );

        if(agent.getRole() != Role.AGENT && agent.getRole() != Role.SUPERVISOR){
            throw new BadRequestException("Only supervisor or agent can close tickets");
        }
        if (ticket.getStatus() == TicketStatus.CLOSED){
            throw new BadRequestException("Ticket is already is closed");
        }
        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setResolvedAt(LocalDateTime.now());
        ticketRepo.save(ticket);

        LocalDateTime frDue = null, resDue = null;
        if(ticket.getSla() != null){
            frDue = ticket.getCreatedAt().plusMinutes(ticket.getSla().getFirstResponseMins());
            resDue = ticket.getCreatedAt().plusMinutes(ticket.getSla().getResolutoinMins());
        }
        return mapToResponse(ticket, frDue, resDue);
    }
    public List <TicketResponse> getTicketsByRequester(Long requesterId) {
        var tickets = ticketRepo.findByRequesterId(requesterId);
        return tickets.stream().map(t -> {
            LocalDateTime frDue = null;
            LocalDateTime resDue = null;

            if (t.getSla() != null) {
                frDue = t.getCreatedAt().plusMinutes(t.getSla().getFirstResponseMins());
                resDue = t.getCreatedAt().plusMinutes(t.getSla().getResolutoinMins());
            }

            return mapToResponse(t, frDue, resDue);
        }).toList();
    }
    private TicketResponse mapToResponse(Ticket t, LocalDateTime frDue, LocalDateTime resDue) {
        var resp = new TicketResponse();
        resp.setId(t.getId());
        resp.setOrgId(t.getOrganization().getId());
        resp.setRequesterId(t.getRequester().getId());

        resp.setAssigneeId(t.getAssignee() != null ?
                t.getAssignee().getId() : null);
        resp.setPriority(t.getPriority());
        resp.setStatus(t.getStatus());
        resp.setSubject(t.getSubject());
        resp.setRequesterId(t.getRequester().getId());
        resp.setCreatedAt(t.getCreatedAt());
        resp.setUpdatedAt(t.getUpdatedAt());
        resp.setFirstResponseDueAt(t.getFirstResponseDueAt());
        resp.setResolutionDueAt(t.getResolutionDueAt());
        resp.setFirstRespondedAt(t.getFirstRespondedAt());
        resp.setResolvedAt(t.getResolvedAt());
        resp.setFirstResponseBreached(t.isFirstResponseBreached());
        resp.setResolutionBreached(t.isResolutionBreached());
        resp.setEscalationLevel(t.getEscalationLevel());

        var messages = messageRepo.findByTicketIdOrderByCreatedAtAsc(t.getId())
                .stream()
                .map(this::mapToMessageResponse)
                .toList();

        resp.setMessage(messages);
        return resp;
    }
    private TicketMessageResponse mapToMessageResponse(TicketMessage m  ) {
        var r = new TicketMessageResponse();
        r.setId(m.getId());
        r.setAuthorUserId(m.getAuthor().getId());
        r.setBody(m.getBody());
        r.setInternal(m.isInternal());
        r.setWaitOnCustomer(m.isWaitOnCustomer());
        r.setCreatedAt(m.getCreatedAt());
        r.setFrom(m.getAuthor().getRole().toString());
        return r;
    }

    public List<TicketResponse> getTicketAssignedToAgent(Long agentId) {

        var tickets = ticketRepo.findByAssigneeId(agentId);

        return tickets.stream().map(t -> {
            LocalDateTime frDue = null;
            LocalDateTime resDue = null;

            if (t.getSla() != null) {
                frDue = t.getCreatedAt().plusMinutes(t.getSla().getFirstResponseMins());
                resDue = t.getCreatedAt().plusMinutes(t.getSla().getResolutoinMins());
            }

            return mapToResponse(t, frDue, resDue);
        }).toList();
    }
    public List<TicketResponse> getUnassignedTickets(){
        var tickets = ticketRepo.findByAssigneeIsNullOrderByCreatedAtDesc();
        return tickets.stream().map(( t -> {
            LocalDateTime frDue = null;
            LocalDateTime resDue = null;

            if (t.getSla() != null) {
                frDue = t.getCreatedAt().plusMinutes(t.getSla().getFirstResponseMins());
                resDue = t.getCreatedAt().plusMinutes(t.getSla().getResolutoinMins());
            }
        return mapToResponse(t, frDue, resDue);
        })).toList();
    }

    public TicketResponse assignToAgent(String ticketId, Long agentId) {
        var ticket = ticketRepo.findById(ticketId).orElseThrow(()
                -> new NotFoundException("Ticket now found"));

        var agent = userRepo.findById(agentId).orElseThrow(
                () -> new NotFoundException("Agent not found")
        );
        ticket.setAssignee(agent);
        ticket.setUpdatedAt(LocalDateTime.now());

        ticket = ticketRepo.save(ticket);

        return mapToResponse(ticket, null, null);
    }
    public Map<String, Long> getAgentDashboardStats(Long agentId){
        Map<String, Long> stats = new HashMap<>();
        stats.put("assigned", ticketRepo.countByAssigneeId(agentId));
        stats.put("pending", ticketRepo.countByStatusAndAssigneeId(TicketStatus.ASSIGNED, agentId));
        stats.put("unassigned", ticketRepo.countByAssigneeIsNull());
        stats.put("closedToday", ticketRepo.countByStatusAndUpdatedAtAfter(
                TicketStatus.CLOSED,
                LocalDateTime.now().minusDays(1)
        ));
        return stats;
    }
}
