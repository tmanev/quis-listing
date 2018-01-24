package com.manev.quislisting.web.rest.filter;

import java.util.List;

public class DlListingSearchFilter {

    private String text;
    private String categoryId;
    private String locationId;
    private List<DlContentFieldFilter> contentFields;
    private String languageCode;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public List<DlContentFieldFilter> getContentFields() {
        return contentFields;
    }

    public void setContentFields(List<DlContentFieldFilter> contentFields) {
        this.contentFields = contentFields;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
