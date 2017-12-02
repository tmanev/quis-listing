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
            return searchString(languageCode, qlString);
        }
        return null;
    }

    static String getTranslatedString(DlContentField dlContentField, String languageCode) {
        if (languageCode != null) {
            QlString qlString = dlContentField.getQlString();
            return searchString(languageCode, qlString);
        }
        return null;
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
