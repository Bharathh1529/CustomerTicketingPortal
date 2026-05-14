package com.example.customerticketingportal.Dto;

import jakarta.validation.constraints.NotBlank;

public class KbArticleSearchResponse {
    private Long KbId;
    @NotBlank
    private String title;
    private String snippet;
    private String category;

    public Long getKbId() {
        return KbId;
    }
    public void setKbId(Long kbId) {
        KbId = kbId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSnippet() {
        return snippet;
    }
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
