package com.example.customerticketingportal.Dto;

import com.example.customerticketingportal.Model.Enums.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.objenesis.SpringObjenesis;

public class TicketCreateRequest {
    @NotNull
    private Long orgId;
    @NotNull
    private Long requesterId;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @NotBlank
    private String body;
    @NotBlank
    private String subject;
    private Long assigneeId;

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
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Long getAssigneeId() {
        return assigneeId;
    }
    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
    public String getSubject() { return subject; }
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
