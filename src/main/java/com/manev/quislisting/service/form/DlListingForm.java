package com.manev.quislisting.service.form;

import com.manev.quislisting.domain.post.discriminator.DlListing;

import java.math.BigDecimal;
import java.util.List;

public class DlListingForm {

    private Long id;
    private String title;
    private String content;
    private List<DlListingFieldForm> dlListingFields;
    private List<DlLocationForm> dlLocations;
    private List<DlCategoryForm> dlCategories;
    private DlFeaturedAttachmentForm featuredAttachment;
    private DlListing.Status status;
    private BigDecimal price;
    private String priceCurrency;
    private String contactInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<DlListingFieldForm> getDlListingFields() {
        return dlListingFields;
    }

    public void setDlListingFields(List<DlListingFieldForm> dlListingFields) {
        this.dlListingFields = dlListingFields;
    }

    public List<DlLocationForm> getDlLocations() {
        return dlLocations;
    }

    public void setDlLocations(List<DlLocationForm> dlLocations) {
        this.dlLocations = dlLocations;
    }

    public List<DlCategoryForm> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(List<DlCategoryForm> dlCategories) {
        this.dlCategories = dlCategories;
    }

    public DlFeaturedAttachmentForm getFeaturedAttachment() {
        return featuredAttachment;
    }

    public void setFeaturedAttachment(DlFeaturedAttachmentForm featuredAttachment) {
        this.featuredAttachment = featuredAttachment;
    }

    public DlListing.Status getStatus() {
        return status;
    }

    public void setStatus(DlListing.Status status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
