package com.manev.quislisting.service.dto;

public class DlContentFieldItemDTO {

    private Long id;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DlContentFieldItemDTO id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DlContentFieldItemDTO value(String value) {
        this.value = value;
        return this;
    }
}
