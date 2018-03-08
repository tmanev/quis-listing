package com.manev.quislisting.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.service.post.dto.serializer.TimestampDeserializer;
import com.manev.quislisting.service.post.dto.serializer.TimestampSerializer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DlListingBaseModel {

    private Long id;
    private String title;
    private String name;
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp created;
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp modified;
    private String languageCode;
    private String sourceLanguageCode;
    private AttachmentModel featuredAttachment;
    private DlListing.Status status;
    private BigDecimal price;
    private String priceCurrency;
    private List<DlLocationModel> dlLocations;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(final Timestamp modified) {
        this.modified = modified;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(final Timestamp created) {
        this.created = created;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(final String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(final String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public AttachmentModel getFeaturedAttachment() {
        return featuredAttachment;
    }

    public void setFeaturedAttachment(final AttachmentModel featuredAttachment) {
        this.featuredAttachment = featuredAttachment;
    }

    public DlListing.Status getStatus() {
        return status;
    }

    public void setStatus(final DlListing.Status status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(final String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public List<DlLocationModel> getDlLocations() {
        return dlLocations;
    }

    public void setDlLocations(final List<DlLocationModel> dlLocations) {
        this.dlLocations = dlLocations;
    }

    public void addDlLocation(final DlLocationModel dlLocationModel) {
        if (this.dlLocations == null) {
            this.dlLocations = new ArrayList<>();
        }
        this.dlLocations.add(dlLocationModel);
    }
}
