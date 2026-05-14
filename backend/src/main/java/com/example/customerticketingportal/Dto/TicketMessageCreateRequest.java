package com.example.customerticketingportal.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketMessageCreateRequest {

    @NotNull
    private Long authorUserId;
    @NotBlank
    private String body;
    private boolean internal;
    private boolean waitOnCustomer;

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
    public boolean isWaitOnCustomer() {
        return waitOnCustomer;
    }
    public void setWaitOnCustomer(boolean waitOnCustomer) {
        this.waitOnCustomer = waitOnCustomer;
    }
}
