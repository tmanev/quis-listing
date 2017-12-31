package com.manev.quislisting.service.taxonomy.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TranslatedTermDTO {

    private Long id;
    private String name;
    private Long translationGroupId;
    private String langKey;

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

    public Long getTranslationGroupId() {
        return translationGroupId;
    }

    public void setTranslationGroupId(Long translationGroupId) {
        this.translationGroupId = translationGroupId;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TranslatedTermDTO that = (TranslatedTermDTO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(translationGroupId, that.translationGroupId)
                .append(langKey, that.langKey)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(translationGroupId)
                .append(langKey)
                .toHashCode();
    }
}
