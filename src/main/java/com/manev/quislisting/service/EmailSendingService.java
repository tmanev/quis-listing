package com.manev.quislisting.service;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.*;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.repository.StaticPageRepository;
import com.manev.quislisting.service.dto.ContactDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class EmailSendingService {

    private static final String ACTIVATE_PAGE = "activatePage";
    private static final String BASE_URL = "baseUrl";
    private static final String USER = "user";
    private static final String BASE_NAME = "baseName";
    private static final String SUBJECT = "subject";
    private static final String ACTIVATION_TEXT = "activationText";
    private final Logger log = LoggerFactory.getLogger(EmailSendingService.class);
    private final MessageSource messageSource;
    private final QuisListingProperties quisListingProperties;
    private final QlConfigService qlConfigService;
    private final EmailTemplateService emailTemplateService;
    private MailService mailService;
    private StaticPageRepository staticPageRepository;

    private TemplateEngineComponent templateEngineComponent;

    public EmailSendingService(MailService mailService, StaticPageRepository staticPageRepository,
                               MessageSource messageSource,
                               QuisListingProperties quisListingProperties, QlConfigService qlConfigService, EmailTemplateService emailTemplateService, TemplateEngineComponent templateEngineComponent) {
        this.mailService = mailService;
        this.staticPageRepository = staticPageRepository;
        this.messageSource = messageSource;
        this.quisListingProperties = quisListingProperties;
        this.qlConfigService = qlConfigService;
        this.emailTemplateService = emailTemplateService;
        this.templateEngineComponent = templateEngineComponent;
    }


    public void sendContactUs(ContactDTO contactDTO, String language) {
        QlConfig adminEmailConfig = qlConfigService.findOneByKey("admin_email");

        // try and find the template by the selected language
        EmailTemplate contactUsEmailTemplate = emailTemplateService.findOneByName("contact-us");

        String html = getValueByLanguage(language, contactUsEmailTemplate.getQlString());
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", contactDTO.getName());
        variables.put("email", contactDTO.getEmail());
        variables.put("subject", contactDTO.getSubject());
        variables.put("message", contactDTO.getMessage().replaceAll("(\r\n|\n)", "<br />"));
        String emailContent = templateEngineComponent.getTemplateFromMap(html, variables);

        mailService.sendEmail(adminEmailConfig.getValue(), contactDTO.getSubject(), emailContent, false, true);
    }

    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());

        Locale locale = Locale.forLanguageTag(user.getLangKey());
        // try and find the template by the selected language
        EmailTemplate passwordResetEmailTemplate = emailTemplateService.findOneByName("password-reset");

        QlConfig siteNameConfig = qlConfigService.findOneByKey("site-name");
        QlConfig resetFinishPageConfig = qlConfigService.findOneByKey("reset-password-finish-page-id");

        String html = getValueByLanguage(user.getLangKey(), passwordResetEmailTemplate.getQlString());

        String title = messageSource.getMessage("email.reset.title", new String[]{siteNameConfig.getValue()}, locale);

        Map<String, Object> variables = new HashMap<>();
        variables.put(USER, user);
        variables.put(BASE_URL, quisListingProperties.getMail().getBaseUrl());
        variables.put(BASE_NAME, siteNameConfig.getValue());
        variables.put("title", title);
        variables.put("resetText1", messageSource.getMessage("email.reset.text1", new String[]{siteNameConfig.getValue()}, locale));
        variables.put("resetFinishPage", getPageSlug(user, resetFinishPageConfig));

        String emailContent = templateEngineComponent.getTemplateFromMap(html, variables);
        mailService.sendEmail(user.getEmail(), title, emailContent, false, true);
    }

    public void sendActivationEmail(User user) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());

        EmailTemplate activationEmailTemplate = emailTemplateService.findOneByName("activation_email");

        QlConfig siteNameConfig = qlConfigService.findOneByKey("site-name");
        QlConfig activationPageConfig = qlConfigService.findOneByKey("activation-page-id");

        String activationPageSlug = getPageSlug(user, activationPageConfig);

        String subject = messageSource.getMessage("email.activation.title", new String[]{siteNameConfig.getValue()}, locale);
        String activationText = messageSource.getMessage("email.activation.text1", new String[]{siteNameConfig.getValue()}, locale);

        String html = getValueByLanguage(user.getLangKey(), activationEmailTemplate.getQlString());

        Map<String, Object> variables = new HashMap<>();

        variables.put(USER, user);
        variables.put(BASE_URL, quisListingProperties.getMail().getBaseUrl());
        variables.put(BASE_NAME, siteNameConfig.getValue());
        variables.put(ACTIVATE_PAGE, activationPageSlug);
        variables.put(SUBJECT, subject);
        variables.put(ACTIVATION_TEXT, activationText);

        String emailContent = templateEngineComponent.getTemplateFromMap(html, variables);
        mailService.sendEmail(user.getEmail(), subject, emailContent, false, true);
    }

    private String getPageSlug(User user, QlConfig activationPageConfig) {
        StaticPage activationPage = staticPageRepository.findOne(Long.valueOf(activationPageConfig.getValue()));
        Translation translation = activationPage.getTranslation();
        String activationPageSlug = activationPage.getName();
        if (!translation.getLanguageCode().equals(user.getLangKey())) {
            // check if there is a translation
            Translation translationForLanguage = translationExists(user.getLangKey(), translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                StaticPage oneByTranslation = staticPageRepository.findOneByTranslation(translationForLanguage);
                activationPageSlug = oneByTranslation.getName();
            }
        }
        return activationPageSlug;
    }

    private Translation translationExists(String languageCode, Set<Translation> translationList) {
        for (Translation translation : translationList) {
            if (translation.getLanguageCode().equals(languageCode)) {
                return translation;
            }
        }
        return null;
    }

    private String getValueByLanguage(String languageCode, QlString qlString) {
        if (qlString.getLanguageCode().equals(languageCode)) {
            return qlString.getValue();
        }
        Set<StringTranslation> stringTranslation = qlString.getStringTranslation();
        for (StringTranslation translation : stringTranslation) {
            if (translation.getLanguageCode().equals(languageCode)) {
                return translation.getValue();
            }
        }

        // if nothing is founded return default value from qlString
        return qlString.getValue();
    }

}
