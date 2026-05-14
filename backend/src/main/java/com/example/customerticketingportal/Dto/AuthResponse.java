package com.example.customerticketingportal.Dto;

import com.example.customerticketingportal.Model.Enums.Role;

public class AuthResponse {
    private Long id;
    private Long orgId;
    private String token;
    private String role;
    private String name;
    public AuthResponse() {   // ✅ required for Spring
    }
    public AuthResponse(Long id, Long orgId, String token, String role, String name) {
        this.id = id;
        this.orgId = orgId;
        this.token = token;
        this.role = role;
        this.name = name;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() { return name; }
    public String getRole() { return role; }
    public Long getId() { return id;}
    public Long getOrgId() { return orgId; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }
}