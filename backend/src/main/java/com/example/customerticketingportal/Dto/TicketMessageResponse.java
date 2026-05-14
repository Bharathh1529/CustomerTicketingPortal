package com.example.customerticketingportal.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class TicketMessageResponse {
    private Long id;
    private Long authorUserId;
    @NotBlank
    private String body;
    private boolean internal;
    private boolean waitOnCustomer;
    private LocalDateTime createdAt;
    private String from;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getAuthorUserId() {
        return authorUserId;
    }
    public void setAuthorUserId(Long authorUserId) {
        this.authorUserId = authorUserId;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public boolean isInternal() {
        return internal;
    }
    public void setInternal(boolean internal) {
        this.internal = internal;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public boolean isWaitOnCustomer() {
        return waitOnCustomer;
    }
    public void setWaitOnCustomer(boolean waitOnCustomer) {
        this.waitOnCustomer = waitOnCustomer;
    }
}
