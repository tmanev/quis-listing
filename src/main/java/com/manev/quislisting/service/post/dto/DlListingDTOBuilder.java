package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public final class DlListingDTOBuilder {
    private String expirationDate;
    private DlListing.Status status;
    private String views;
    private List<DlCategoryDTO> dlCategories;
    private List<DlLocationDTO> dlLocations;
    private Long id;
    private String title;
    private List<DlListingFieldDTO> dlListingFieldDTOS;
    private String content;
    private String name;
    private List<AttachmentDTO> attachments;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private UserDTO author;
    private String languageCode;
    private String sourceLanguageCode;
    private Long translationGroupId;
    private List<TranslationDTO> translations;

    private DlListingDTOBuilder() {
    }

    public static DlListingDTOBuilder aDlListingDTO() {
        return new DlListingDTOBuilder();
    }

    public DlListingDTOBuilder withExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public DlListingDTOBuilder withStatus(DlListing.Status status) {
        this.status = status;
        return this;
    }

    public DlListingDTOBuilder withViews(String views) {
        this.views = views;
        return this;
    }

    public DlListingDTOBuilder withDlCategories(List<DlCategoryDTO> dlCategories) {
        this.dlCategories = dlCategories;
        return this;
    }

    public DlListingDTOBuilder addDlCategory(DlCategoryDTO dlCategoryDTO) {
        if (this.dlCategories == null) {
            this.dlCategories = new ArrayList<>();
        }
        this.dlCategories.add(dlCategoryDTO);
        return this;
    }

    public DlListingDTOBuilder withDlLocations(List<DlLocationDTO> dlLocations) {
        this.dlLocations = dlLocations;
        return this;
    }

    public DlListingDTOBuilder addDlLocation(DlLocationDTO dlLocationDTO) {
        if (this.dlLocations == null) {
            this.dlLocations = new ArrayList<>();
        }
        this.dlLocations.add(dlLocationDTO);
        return this;
    }

    public DlListingDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlListingDTOBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public DlListingDTOBuilder withDlListingFields(List<DlListingFieldDTO> dlListingFieldDTOS) {
        this.dlListingFieldDTOS = dlListingFieldDTOS;
        return this;
    }

    public DlListingDTOBuilder addDlListingField(DlListingFieldDTO dlListingFieldDTO) {
        if (this.dlListingFieldDTOS == null) {
            this.dlListingFieldDTOS = new ArrayList<>();
        }
        this.dlListingFieldDTOS.add(dlListingFieldDTO);
        return this;
    }

    public DlListingDTOBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public DlListingDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DlListingDTOBuilder withAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
        return this;
    }

    public DlListingDTOBuilder withCreated(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public DlListingDTOBuilder withModified(ZonedDateTime modified) {
        this.modified = modified;
        return this;
    }

    public DlListingDTOBuilder withAuthor(UserDTO author) {
        this.author = author;
        return this;
    }

    public DlListingDTOBuilder withLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public DlListingDTOBuilder withSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
        return this;
    }

    public DlListingDTOBuilder withTranslationGroupId(Long translationGroupId) {
        this.translationGroupId = translationGroupId;
        return this;
    }

    public DlListingDTOBuilder withTranslations(List<TranslationDTO> translations) {
        this.translations = translations;
        return this;
    }

    public DlListingDTOBuilder addTranslation(TranslationDTO translationDTO) {
        if (this.translations == null) {
            translations = new ArrayList<>();
        }
        translations.add(translationDTO);
        return this;
    }

    public DlListingDTO build() {
        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setExpirationDate(expirationDate);
        dlListingDTO.setStatus(status);
        dlListingDTO.setViews(views);
        dlListingDTO.setDlCategories(dlCategories);
        dlListingDTO.setDlLocations(dlLocations);
        dlListingDTO.setId(id);
        dlListingDTO.setTitle(title);
        dlListingDTO.setDlListingFields(dlListingFieldDTOS);
        dlListingDTO.setContent(content);
        dlListingDTO.setName(name);
        dlListingDTO.setAttachments(attachments);
        dlListingDTO.setCreated(created);
        dlListingDTO.setModified(modified);
        dlListingDTO.setAuthor(author);
        dlListingDTO.setLanguageCode(languageCode);
        dlListingDTO.setSourceLanguageCode(sourceLanguageCode);
        dlListingDTO.setTranslationGroupId(translationGroupId);
        dlListingDTO.setTranslations(translations);
        return dlListingDTO;
    }
}
