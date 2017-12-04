package com.manev.quislisting.web.rest.post;

import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class LanguageUtil {

    public static String getLanguageCode(HttpServletRequest request, LocaleResolver localeResolver) {
        Locale locale = localeResolver.resolveLocale(request);
        return locale.getLanguage();
    }

}
