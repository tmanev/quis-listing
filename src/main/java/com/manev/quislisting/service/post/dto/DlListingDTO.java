package com.manev.quislisting.service.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.post.dto.serializer.ZonedDateTimeDeserializer;
import com.manev.quislisting.service.post.dto.serializer.ZonedDateTimeSerializer;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DlListingDTO {

    private Long id;
    @NotEmpty(message = "dlListingDto.title.NotNull.message")
    private String title;
    private String content;
    private String name;

//    @JsonSerialize(using = ZonedDateTimeSerializer.class)
//    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private Timestamp created;
//    @JsonSerialize(using = ZonedDateTimeSerializer.class)
//    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private Timestamp modified;

    private UserDTO author;

    private String languageCode;
    private String sourceLanguageCode;

    private Long translationGroupId;
    private List<TranslationDTO> translations;
    private String expirationDate;
    private DlListing.Status status;
    private Boolean approved;
    @NotEmpty
    private List<DlCategoryDTO> dlCategories;
    private List<DlLocationDTO> dlLocations;
    private List<DlListingFieldDTO> dlListingFields;
    private List<AttachmentDTO> attachments;
    private AttachmentDTO featuredAttachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
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

    public void addTranslationDTO(TranslationDTO translationDTO) {
        if (translations == null) {
            translations = new ArrayList<>();
        }
        translations.add(translationDTO);
    }

    public Long getTranslationGroupId() {
        return translationGroupId;
    }

    public void setTranslationGroupId(Long translationGroupId) {
        this.translationGroupId = translationGroupId;
    }

    public List<TranslationDTO> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationDTO> translations) {
        this.translations = translations;
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

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
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

    public List<DlListingFieldDTO> getDlListingFields() {
        return dlListingFields;
    }

    public void setDlListingFields(List<DlListingFieldDTO> dlListingFields) {
        this.dlListingFields = dlListingFields;
    }

    public void addDlListingField(DlListingFieldDTO dlListingFieldDTO) {
        if (dlListingFields == null) {
            dlListingFields = new ArrayList<>();
        }
        dlListingFields.add(dlListingFieldDTO);
    }

    public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public void addAttachmentDto(AttachmentDTO attachmentDTO) {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        attachments.add(attachmentDTO);
    }

    public AttachmentDTO getFeaturedAttachment() {
        return featuredAttachment;
    }

    public void setFeaturedAttachment(AttachmentDTO featuredAttachment) {
        this.featuredAttachment = featuredAttachment;
    }
}
