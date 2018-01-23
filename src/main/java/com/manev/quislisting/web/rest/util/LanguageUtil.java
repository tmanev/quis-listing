package com.manev.quislisting.web.rest.util;

import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class LanguageUtil {

    private LanguageUtil() {
        // private constructor
    }

    public static String getLanguageCode(HttpServletRequest request, LocaleResolver localeResolver) {
        Locale locale = localeResolver.resolveLocale(request);
        return locale.getLanguage();
    }

}
