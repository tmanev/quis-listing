package com.manev.quislisting.web.rest.filter;

public class DlContentFieldFilter {
    private Long id;
    private String value;
    private String selectedValue;

    public DlContentFieldFilter() {
        // default constructor
    }

    public DlContentFieldFilter(Long id, String value, String selectedValue) {
        this.id = id;
        this.value = value;
        this.selectedValue = selectedValue;
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

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
}
