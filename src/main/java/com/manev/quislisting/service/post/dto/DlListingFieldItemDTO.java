package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.service.model.QlStringTranslationModel;

import java.util.List;

public class DlListingFieldItemDTO {
    private Long id;
    private String value;
    private List<QlStringTranslationModel> translatedValues;

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

    public List<QlStringTranslationModel> getTranslatedValues() {
        return translatedValues;
    }

    public void setTranslatedValues(List<QlStringTranslationModel> translatedValues) {
        this.translatedValues = translatedValues;
    }
}
