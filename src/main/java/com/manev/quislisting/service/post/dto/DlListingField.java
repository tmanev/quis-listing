package com.manev.quislisting.service.post.dto;

public class DlListingField {

    private String fieldId;
    private String value;

    public DlListingField() {
    }

    public DlListingField(String fieldId, String value) {
        this.fieldId = fieldId;
        this.value = value;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
