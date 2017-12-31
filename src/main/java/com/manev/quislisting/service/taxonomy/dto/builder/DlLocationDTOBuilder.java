package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.post.dto.TranslationDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;

import java.util.List;

public final class DlLocationDTOBuilder {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private String description;
    private Long count;
    private String languageCode;
    private String sourceLanguageCode;
    private Long translationGroupId;
    private List<TranslationDTO> translations;
    private DlLocationDTO parent;

    private DlLocationDTOBuilder() {
    }

    public static DlLocationDTOBuilder aDlLocationDTO() {
        return new DlLocationDTOBuilder();
    }

    public DlLocationDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlLocationDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DlLocationDTOBuilder withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public DlLocationDTOBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public DlLocationDTOBuilder withParent(DlLocationDTO parent) {
        this.parent = parent;
        return this;
    }

    public DlLocationDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DlLocationDTOBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public DlLocationDTOBuilder withLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public DlLocationDTOBuilder withSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
        return this;
    }

    public DlLocationDTOBuilder withTranslationGroupId(Long translationGroupId) {
        this.translationGroupId = translationGroupId;
        return this;
    }

    public DlLocationDTOBuilder withTranslations(List<TranslationDTO> translations) {
        this.translations = translations;
        return this;
    }

    public DlLocationDTO build() {
        DlLocationDTO dlLocationDTO = new DlLocationDTO();
        dlLocationDTO.setId(id);
        dlLocationDTO.setName(name);
        dlLocationDTO.setSlug(slug);
        dlLocationDTO.setParentId(parentId);
        dlLocationDTO.setParent(parent);
        dlLocationDTO.setDescription(description);
        dlLocationDTO.setCount(count);
        dlLocationDTO.setLanguageCode(languageCode);
        dlLocationDTO.setSourceLanguageCode(sourceLanguageCode);
        dlLocationDTO.setTranslationGroupId(translationGroupId);
        dlLocationDTO.setTranslations(translations);
        return dlLocationDTO;
    }
}
