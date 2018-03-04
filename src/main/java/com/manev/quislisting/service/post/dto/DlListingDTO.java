package com.manev.quislisting.service.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.post.dto.serializer.TimestampDeserializer;
import com.manev.quislisting.service.post.dto.serializer.TimestampSerializer;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.TranslatedTermDTO;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Document(indexName = "dl_listing")
public class DlListingDTO {

    @Id
    private Long id;
    @NotEmpty(message = "dlListingDto.title.NotNull.message")
    private String title;
    private String content;
    private String name;
    private BigDecimal price;
    private String priceCurrency;
    private String contactInfo;

    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp created;

    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp modified;

    private UserDTO author;

    private String languageCode;
    private String sourceLanguageCode;

    private Long translationGroupId;
    private List<TranslationDTO> translations;
    private String expirationDate;

    @Field(type = FieldType.String)
    private DlListing.Status status;

    private Boolean approved;

    @NotEmpty
    @Field(type = FieldType.Nested)
    private List<DlCategoryDTO> dlCategories;

    @Field(type = FieldType.Nested)
    private List<TranslatedTermDTO> translatedCategories;

    @Field(type = FieldType.Nested)
    private List<DlLocationDTO> dlLocations;

    @Field(type = FieldType.Nested)
    private List<TranslatedTermDTO> translatedLocations;

    @Field(type = FieldType.Nested)
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

    public AttachmentDTO getFeaturedAttachment() {
        return featuredAttachment;
    }

    public void setFeaturedAttachment(AttachmentDTO featuredAttachment) {
        this.featuredAttachment = featuredAttachment;
    }

    public List<TranslatedTermDTO> getTranslatedCategories() {
        return translatedCategories;
    }

    public void setTranslatedCategories(List<TranslatedTermDTO> translatedCategories) {
        this.translatedCategories = translatedCategories;
    }

    public void addTranslatedCategory(TranslatedTermDTO translatedCategoryDTO) {
        if (translatedCategories == null) {
            translatedCategories = new ArrayList<>();
        }
        translatedCategories.add(translatedCategoryDTO);
    }

    public List<TranslatedTermDTO> getTranslatedLocations() {
        return translatedLocations;
    }

    public void setTranslatedLocations(List<TranslatedTermDTO> translatedLocations) {
        this.translatedLocations = translatedLocations;
    }

    public void addTranslatedLocation(TranslatedTermDTO translatedTermDTO) {
        if (translatedLocations == null) {
            translatedLocations = new ArrayList<>();
        }
        translatedLocations.add(translatedTermDTO);
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
