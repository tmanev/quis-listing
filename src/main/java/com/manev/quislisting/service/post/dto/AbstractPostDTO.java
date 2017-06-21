package com.manev.quislisting.service.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.post.dto.serializer.ZonedDateTimeDeserializer;
import com.manev.quislisting.service.post.dto.serializer.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPostDTO {

    private Long id;
    private String title;
    private String content;
    private String name;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime created;
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime modified;

    private UserDTO author;

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
}
