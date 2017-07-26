package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;

public final class DlCategoryDTOBuilder {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private String description;
    private Long count;
    private String languageCode;
    private String sourceLanguageCode;
    private Long translationGroupId;

    private DlCategoryDTOBuilder() {
    }

    public static DlCategoryDTOBuilder aDlCategoryDTO() {
        return new DlCategoryDTOBuilder();
    }

    public DlCategoryDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlCategoryDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DlCategoryDTOBuilder withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public DlCategoryDTOBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public DlCategoryDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DlCategoryDTOBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public DlCategoryDTOBuilder withLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public DlCategoryDTO build() {
        DlCategoryDTO dlCategoryDTO = new DlCategoryDTO();
        dlCategoryDTO.setId(id);
        dlCategoryDTO.setName(name);
        dlCategoryDTO.setSlug(slug);
        dlCategoryDTO.setParentId(parentId);
        dlCategoryDTO.setDescription(description);
        dlCategoryDTO.setCount(count);
        dlCategoryDTO.setLanguageCode(languageCode);
        dlCategoryDTO.setSourceLanguageCode(sourceLanguageCode);
        dlCategoryDTO.setTranslationGroupId(translationGroupId);
        return dlCategoryDTO;
    }

    public DlCategoryDTOBuilder withSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
        return this;
    }

    public DlCategoryDTOBuilder withTranslationGroupId(Long translationGroupId) {
        this.translationGroupId = translationGroupId;
        return this;
    }
}
