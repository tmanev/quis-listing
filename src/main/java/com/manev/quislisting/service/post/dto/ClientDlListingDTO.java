package com.manev.quislisting.service.post.dto;

import javax.validation.constraints.NotNull;

public class ClientDlListingDTO {

    private Long id;
    @NotNull
    private String title;
    @NotNull
    private Long categoryId;

    public ClientDlListingDTO() {
        // default constructor
    }

    public ClientDlListingDTO(Long id, String title, Long categoryId) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
