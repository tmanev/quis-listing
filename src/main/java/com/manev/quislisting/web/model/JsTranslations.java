package com.manev.quislisting.web.model;

import java.util.HashMap;
import java.util.Map;

public class JsTranslations {
    private Map<String, String> translations;

    public JsTranslations() {
        this.translations = new HashMap<>();
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public void addTranslation(String key, String value) {
        translations.put(key, value);
    }
}
