package com.manev.quislisting.service.model;

import java.util.ArrayList;
import java.util.List;

public class DlListingModel extends DlListingBaseModel {

    private String content;
    private String contactInfo;

    private List<DlListingFieldModel> dlListingFields;

    private List<String> dlCategories;

    private List<AttachmentModel> attachments;

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(final String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<DlListingFieldModel> getDlListingFields() {
        return dlListingFields;
    }

    public void setDlListingFields(final List<DlListingFieldModel> dlListingFields) {
        this.dlListingFields = dlListingFields;
    }

    public List<String> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(final List<String> dlCategories) {
        this.dlCategories = dlCategories;
    }

    public void addDlCategory(final String category) {
        if (this.dlCategories == null) {
            this.dlCategories = new ArrayList<>();
        }
        this.dlCategories.add(category);
    }

    public List<AttachmentModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(final List<AttachmentModel> attachments) {
        this.attachments = attachments;
    }
}
