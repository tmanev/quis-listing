package com.manev.quislisting.service.model;

import com.manev.quislisting.service.post.dto.AttachmentDTO;

import java.util.ArrayList;
import java.util.List;

public class DlListingModel extends DlListingBaseModel {

    private String content;

    private List<DlListingFieldModel> dlListingFields;

    private List<DlLocationModel> dlLocations;
    private List<String> dlCategories;

    private List<AttachmentDTO> attachments;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<DlListingFieldModel> getDlListingFields() {
        return dlListingFields;
    }

    public void setDlListingFields(List<DlListingFieldModel> dlListingFields) {
        this.dlListingFields = dlListingFields;
    }

    public List<DlLocationModel> getDlLocations() {
        return dlLocations;
    }

    public void setDlLocations(List<DlLocationModel> dlLocations) {
        this.dlLocations = dlLocations;
    }

    public void addDlLocation(DlLocationModel dlLocationModel) {
        if (this.dlLocations == null) {
            this.dlLocations = new ArrayList<>();
        }
        this.dlLocations.add(dlLocationModel);
    }

    public List<String> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(List<String> dlCategories) {
        this.dlCategories = dlCategories;
    }

    public void addDlCategory(String category) {
        if (this.dlCategories == null) {
            this.dlCategories = new ArrayList<>();
        }
        this.dlCategories.add(category);
    }

    public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }
}
