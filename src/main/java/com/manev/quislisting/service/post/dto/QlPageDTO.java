package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.domain.post.discriminator.QlPage;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class QlPageDTO {

    private Long id;
    private String title;
    private String content;
    private String name;
    private String expirationDate;
    private QlPage.Status status;
    private String views;
    private Author author;
    private ZonedDateTime created;
    private ZonedDateTime modified;
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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public QlPage.Status getStatus() {
        return status;
    }

    public void setStatus(QlPage.Status status) {
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


    public Long getTranslationGroupId() {
        return translationGroupId;
    }

    public void setTranslationGroupId(Long translationGroupId) {
        this.translationGroupId = translationGroupId;
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
