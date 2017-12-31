package com.manev.quislisting.service.model;

import com.manev.quislisting.domain.DlContentField;

public class DlContentFieldInput {
    private DlContentField dlContentField;
    private String value;
    private String selectionValue;

    public DlContentFieldInput() {
        // default constructor
    }

    public DlContentFieldInput(DlContentField dlContentField, String value) {
        this.dlContentField = dlContentField;
        this.value = value;
    }

    public DlContentFieldInput(DlContentField dlContentField, String value, String selectionValue) {
        this.dlContentField = dlContentField;
        this.value = value;
        this.selectionValue = selectionValue;
    }

    public DlContentField getDlContentField() {
        return dlContentField;
    }

    public void setDlContentField(DlContentField dlContentField) {
        this.dlContentField = dlContentField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSelectionValue() {
        return selectionValue;
    }

    public void setSelectionValue(String selectionValue) {
        this.selectionValue = selectionValue;
    }
}
