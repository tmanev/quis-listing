package com.manev.quislisting.web.rest.filter;

import java.util.List;

public class DlListingSearchFilter {

    private String text;
    private String categoryId;
    private String cityId;
    private String stateId;
    private String countryId;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
