package com.example.customerticketingportal.Dto;

import jakarta.validation.constraints.NotBlank;

public class KbArticleCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    private String category;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String categrory) {
        this.category = categrory;
    }
}
