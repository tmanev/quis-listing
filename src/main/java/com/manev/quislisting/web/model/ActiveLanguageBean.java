package com.manev.quislisting.web.model;

import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.qlml.LanguageTranslation;

public class ActiveLanguageBean {
    private Language language;
    private LanguageTranslation languageTranslation;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public LanguageTranslation getLanguageTranslation() {
        return languageTranslation;
    }

    public void setLanguageTranslation(LanguageTranslation languageTranslation) {
        this.languageTranslation = languageTranslation;
    }
}
