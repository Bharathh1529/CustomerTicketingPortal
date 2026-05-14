package com.example.customerticketingportal.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class SLA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is empty")
    private String name;

    private int firstResponseMins;
    private int resolutoinMins;
    private boolean active;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getFirstResponseMins() {
        return firstResponseMins;
    }
    public void setFirstResponseMins(int firstResponseMins) {
        this.firstResponseMins = firstResponseMins;
    }
    public int getResolutoinMins() {
        return resolutoinMins;
    }
    public void setResolutoinMins(int resolutoinMins) {
        this.resolutoinMins = resolutoinMins;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
