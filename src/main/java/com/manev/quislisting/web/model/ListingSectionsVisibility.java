package com.manev.quislisting.web.model;

public class ListingSectionsVisibility {
    private Boolean details = false;
    private Boolean features = false;
    private Boolean contact = false;

    public Boolean getDetails() {
        return details;
    }

    public void setDetails(Boolean details) {
        this.details = details;
    }

    public Boolean getFeatures() {
        return features;
    }

    public void setFeatures(Boolean features) {
        this.features = features;
    }

    public Boolean getContact() {
        return contact;
    }

    public void setContact(Boolean contact) {
        this.contact = contact;
    }
}
