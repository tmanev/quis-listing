package com.manev.quislisting.service.post.dto;

public class DlListingFieldItemDTO {
    private Long id;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DlListingFieldItemDTO id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DlListingFieldItemDTO value(String value) {
        this.value = value;
        return this;
    }

}
