package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;

import java.util.Set;

public class TranslateUtil {

    private TranslateUtil() {
        // private constructor
    }

    public static String getTranslatedString(DlContentFieldItem dlContentFieldItem, String languageCode) {
        if (languageCode != null) {
            QlString qlString = dlContentFieldItem.getQlString();
            String translation = searchString(languageCode, qlString);
            if (translation != null) return translation;
        }
        return dlContentFieldItem.getValue();
    }

    public static String getTranslatedString(DlContentField dlContentField, String languageCode) {
        if (languageCode != null) {
            QlString qlString = dlContentField.getQlString();
            String translation = searchString(languageCode, qlString);
            if (translation != null) return translation;
        }
        return dlContentField.getName();
    }

    private static String searchString(String languageCode, QlString qlString) {
        Set<StringTranslation> stringTranslation = qlString.getStringTranslation();
        for (StringTranslation translation : stringTranslation) {
            if (translation.getLanguageCode().equals(languageCode)) {
                return translation.getValue();
            }
        }
        return null;
    }
}
