package com.manev.quislisting.service.taxonomy.dto;

import com.manev.quislisting.service.post.dto.TranslationDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class TermTaxonomyDTO {
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

    private int depthLevel;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public Long getTranslationGroupId() {
        return translationGroupId;
    }

    public void setTranslationGroupId(Long translationGroupId) {
        this.translationGroupId = translationGroupId;
    }

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public List<TranslationDTO> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationDTO> translations) {
        this.translations = translations;
    }

    public void addTranslationDTO(TranslationDTO translationDTO) {
        if (translations == null) {
            translations = new ArrayList<>();
        }
        translations.add(translationDTO);
    }
}
