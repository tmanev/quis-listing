package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.domain.StaticPage;

import java.util.ArrayList;
import java.util.List;

public class StaticPageDTO {

    private Long id;
    private String title;
    private String content;
    private String name;

    private StaticPage.Status status;

    private String languageCode;
    private String sourceLanguageCode;

    private Long translationGroupId;
    private List<TranslationDTO> translations;

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

    public StaticPage.Status getStatus() {
        return status;
    }

    public void setStatus(StaticPage.Status status) {
        this.status = status;
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

    public void addTranslationDTO(TranslationDTO translationDTO) {
        if (translations == null) {
            translations = new ArrayList<>();
        }
        translations.add(translationDTO);
    }
}
