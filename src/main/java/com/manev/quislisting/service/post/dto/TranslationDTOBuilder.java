package com.manev.quislisting.service.post.dto;

public final class TranslationDTOBuilder {
    private Long id;
    private String languageCode;
    private String sourceLanguageCode;

    private TranslationDTOBuilder() {
    }

    public static TranslationDTOBuilder aTranslationDTO() {
        return new TranslationDTOBuilder();
    }

    public TranslationDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TranslationDTOBuilder withLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public TranslationDTOBuilder withSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
        return this;
    }

    public TranslationDTO build() {
        TranslationDTO translationDTO = new TranslationDTO();
        translationDTO.setId(id);
        translationDTO.setLanguageCode(languageCode);
        translationDTO.setSourceLanguageCode(sourceLanguageCode);
        return translationDTO;
    }
}
