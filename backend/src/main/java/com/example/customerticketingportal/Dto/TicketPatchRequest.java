package com.example.customerticketingportal.Dto;

import com.example.customerticketingportal.Model.Enums.Priority;
import com.example.customerticketingportal.Model.Enums.TicketStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class TicketPatchRequest {
    private Long assigneeId;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
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
}
