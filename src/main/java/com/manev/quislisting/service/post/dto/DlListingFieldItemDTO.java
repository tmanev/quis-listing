package com.manev.quislisting.service.post.dto;

public class DlListingFieldItemDTO {
    private Long id;
    private String value;
    private String translatedValue;

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

    public String getTranslatedValue() {
        return translatedValue;
    }

    public void setTranslatedValue(String translatedValue) {
        this.translatedValue = translatedValue;
    }

    public DlListingFieldItemDTO translatedValue(String translatedValue) {
        this.translatedValue = translatedValue;
        return this;
    }
}
