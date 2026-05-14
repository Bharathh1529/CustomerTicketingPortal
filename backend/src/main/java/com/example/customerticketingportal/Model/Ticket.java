package com.example.customerticketingportal.Model;
import com.example.customerticketingportal.Model.Enums.Priority;
import com.example.customerticketingportal.Model.Enums.TicketStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Ticket {
    @Id
    @Column(name = "ticket_id", nullable = false, unique = true, length = 20)
    private String id;
    @PrePersist
    private void generateTicketId() {
        if (this.id == null) {
            this.id = "INC" + String.format("%06d",
                    new java.util.Random().nextInt(999999));
        }
    }
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @Column(nullable = false, length = 255)
    private String subject;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "requesterId", referencedColumnName = "id")
    private User requester;
    @ManyToOne
    @JoinColumn(name = "assigneeId", referencedColumnName = "id")
    private User assignee;
    @ManyToOne
    @JoinColumn(name = "orgId", referencedColumnName = "id")
    private Organization organization;
    @ManyToOne
    @JoinColumn(name = "slaId", referencedColumnName = "id")
    private SLA sla;

    private LocalDateTime firstResponseDueAt;
    private LocalDateTime resolutionDueAt;

    private LocalDateTime firstRespondedAt;
    private LocalDateTime resolvedAt;

    private boolean firstResponseBreached;
    private boolean resolutionBreached;

    private Integer escalationLevel = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public SLA getSla() {
        return sla;
    }

    public void setSla(SLA sla) {
        this.sla = sla;
    }

    public LocalDateTime getFirstResponseDueAt() {
        return firstResponseDueAt;
    }

    public void setFirstResponseDueAt(LocalDateTime firstResponseDueAt) {
        this.firstResponseDueAt = firstResponseDueAt;
    }

    public LocalDateTime getResolutionDueAt() {
        return resolutionDueAt;
    }

    public void setResolutionDueAt(LocalDateTime resolutionDueAt) {
        this.resolutionDueAt = resolutionDueAt;
    }

    public LocalDateTime getFirstRespondedAt() {
        return firstRespondedAt;
    }

    public void setFirstRespondedAt(LocalDateTime firstRespondedAt) {
        this.firstRespondedAt = firstRespondedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public boolean isFirstResponseBreached() {
        return firstResponseBreached;
    }

    public void setFirstResponseBreached(boolean firstResponseBreached) {
        this.firstResponseBreached = firstResponseBreached;
    }

    public boolean isResolutionBreached() {
        return resolutionBreached;
    }

    public void setResolutionBreached(boolean resolutionBreached) {
        this.resolutionBreached = resolutionBreached;
    }

    public Integer getEscalationLevel() {
        return escalationLevel;
    }

    public void setEscalationLevel(Integer escalationLevel) {
        this.escalationLevel = escalationLevel;
    }
}