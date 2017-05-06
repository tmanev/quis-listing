package com.manev.quislisting.service.post.dto;

public class DlListingField {

    private Long id;
    private String value;

    public DlListingField() {
    }

    public DlListingField(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
