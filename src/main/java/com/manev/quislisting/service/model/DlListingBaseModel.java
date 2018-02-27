package com.manev.quislisting.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.service.post.dto.serializer.TimestampDeserializer;
import com.manev.quislisting.service.post.dto.serializer.TimestampSerializer;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public AttachmentModel getFeaturedAttachment() {
        return featuredAttachment;
    }

    public void setFeaturedAttachment(AttachmentModel featuredAttachment) {
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
}
