package com.manev.quislisting.domain;

public final class TranslationBuilder {
    private Long id;
    private TranslationGroup translationGroup;
    private String languageCode;
    private String sourceLanguageCode;

    private TranslationBuilder() {
    }

    public static TranslationBuilder aTranslation() {
        return new TranslationBuilder();
    }

    public TranslationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TranslationBuilder withTranslationGroup(TranslationGroup translationGroup) {
        this.translationGroup = translationGroup;
        return this;
    }

    public TranslationBuilder withLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public TranslationBuilder withSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
        return this;
    }

    public Translation build() {
        Translation translation = new Translation();
        translation.setId(id);
        translation.setTranslationGroup(translationGroup);
        translation.setLanguageCode(languageCode);
        translation.setSourceLanguageCode(sourceLanguageCode);
        return translation;
    }
}
