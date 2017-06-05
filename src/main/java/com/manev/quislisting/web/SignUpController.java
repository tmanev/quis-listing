package com.manev.quislisting.web;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.post.PageRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping("/sign-up")
public class SignUpController extends BaseController {

    private PageRepository pageRepository;
    private MessageSource messageSource;

    public SignUpController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository, LanguageRepository languageRepository, LanguageTranslationRepository languageTranslationRepository, LocaleResolver localeResolver, PageRepository pageRepository, MessageSource messageSource) {
        super(navMenuRepository, qlConfigRepository, languageRepository, languageTranslationRepository, localeResolver);
        this.pageRepository = pageRepository;
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);

        messageSource.getMessage("page.signup.title", null, locale);
        model.addAttribute("title", "Sign Up");
        model.addAttribute("view", "client/signup");

        QlConfig siteNameConfig = qlConfigRepository.findOneByKey("site-name");
        if (siteNameConfig!=null) {
            model.addAttribute("siteName", siteNameConfig.getValue());
        }

        // request to load terms and conditions page
        QlConfig termsAndConditionsPageIdConfig = qlConfigRepository.findOneByKey("terms-and-conditions-page-id");
        if (termsAndConditionsPageIdConfig != null) {
            String language = locale.getLanguage();

            model.addAttribute("termsAndConditionsPage", retrieveTermsAndConditionsPage(language,
                    termsAndConditionsPageIdConfig.getValue()));
        }

        return "client/index";
    }

    private QlPage retrieveTermsAndConditionsPage(String languageCode, String pageId) {
        QlPage qlPage;

        qlPage = pageRepository.findOne(Long.valueOf(pageId));
        Translation translation = qlPage.getTranslation();
        if (!translation.getLanguageCode().equals(languageCode)) {
            Translation translationForLanguage = translationExists(languageCode, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                qlPage = pageRepository.findOneByTranslation(translationForLanguage);
            }
        }

        return qlPage;
    }

    private Translation translationExists(String languageCode, Set<Translation> translationList) {
        for (Translation translation : translationList) {
            if (translation.getLanguageCode().equals(languageCode)) {
                return translation;
            }
        }
        return null;
    }
}
