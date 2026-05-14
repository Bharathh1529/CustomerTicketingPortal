package com.example.customerticketingportal.Dto;

import com.example.customerticketingportal.Model.Enums.Priority;
import com.example.customerticketingportal.Model.Enums.TicketStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.List;

public class TicketResponse {
    private String id;
    private Long orgId;
    private Long requesterId;
    private Long assigneeId;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private String subject;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime firstResponseDueAt;
    private LocalDateTime resolutionDueAt;

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

    private List<TicketMessageResponse> message;
    private LocalDateTime firstRespondedAt;
    private LocalDateTime resolvedAt;
    private boolean firstResponseBreached;
    private boolean resolutionBreached;
    private Integer escalationLevel;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Long getOrgId() {
        return orgId;
    }
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
    public Long getRequesterId() {
        return requesterId;
    }
    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }
    public Long getAssigneeId() {
        return assigneeId;
    }
    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
    public TicketStatus getStatus() {
        return status;
    }
    public void setStatus(TicketStatus status) {
        this.status = status;
    }
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
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
    public List<TicketMessageResponse> getMessage() {
        return message;
    }
    public void setMessage(List<TicketMessageResponse> message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
