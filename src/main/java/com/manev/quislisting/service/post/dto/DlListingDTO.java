package com.manev.quislisting.service.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.service.post.dto.serializer.ZonedDateTimeDeserializer;
import com.manev.quislisting.service.post.dto.serializer.ZonedDateTimeSerializer;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DlListingDTO {

    private Long id;
    private String title;
    private String content;
    private String name;
    private String expirationDate;
    private DlListing.Status status;
    private String views;
    private Author author;
    private List<DlCategoryDTO> dlCategories;
    private List<DlLocationDTO> dlLocations;
    private List<DlListingField> dlListingFields;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime created;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime modified;
    private String languageCode;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public DlListing.Status getStatus() {
        return status;
    }

    public void setStatus(DlListing.Status status) {
        this.status = status;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<DlLocationDTO> getDlLocations() {
        return dlLocations;
    }

    public void setDlLocations(List<DlLocationDTO> dlLocations) {
        this.dlLocations = dlLocations;
    }

    public List<DlCategoryDTO> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(List<DlCategoryDTO> dlCategories) {
        this.dlCategories = dlCategories;
    }

    public void addDlCategoryDto(DlCategoryDTO dlCategoryDTO) {
        if (dlCategories == null) {
            dlCategories = new ArrayList<>();
        }
        dlCategories.add(dlCategoryDTO);
    }

    public void addDlLocationDto(DlLocationDTO dlLocationDTO) {
        if (dlLocations == null) {
            dlLocations = new ArrayList<>();
        }
        dlLocations.add(dlLocationDTO);
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public void setModified(ZonedDateTime modified) {
        this.modified = modified;
    }


    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public List<DlListingField> getDlListingFields() {
        return dlListingFields;
    }

    public void setDlListingFields(List<DlListingField> dlListingFields) {
        this.dlListingFields = dlListingFields;
    }

    public void addDlListingField(DlListingField dlListingField) {
        if (dlListingFields == null) {
            dlListingFields = new ArrayList<>();
        }
        dlListingFields.add(dlListingField);
    }
}
