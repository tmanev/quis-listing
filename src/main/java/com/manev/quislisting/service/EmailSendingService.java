package com.manev.quislisting.service;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.exception.MissingConfigurationException;
import com.manev.quislisting.repository.EmailTemplateRepository;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.post.PageRepository;
import com.manev.quislisting.service.dto.ContactDTO;
import com.manev.quislisting.service.util.StringAndClassLoaderResourceResolver;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.Locale;
import java.util.Set;

@Service
public class EmailSendingService {

    private static final String ACTIVATE_PAGE = "activatePage";
    private static final String BASE_URL = "baseUrl";
    private static final String USER = "user";
    private static final String BASE_NAME = "baseName";
    private static final String SUBJECT = "subject";
    private static final String ACTIVATION_TEXT = "activationText";
    private static final String REDUNDANT = "redundant";
    private final Logger log = LoggerFactory.getLogger(EmailSendingService.class);
    private final MessageSource messageSource;
    private final QuisListingProperties quisListingProperties;
    private MailService mailService;
    private QlConfigRepository qlConfigRepository;
    private SpringTemplateEngine springTemplateEngine;
    private PageRepository pageRepository;
    private EmailTemplateRepository emailTemplateRepository;

    public EmailSendingService(MailService mailService, QlConfigRepository qlConfigRepository,
                               PageRepository pageRepository, EmailTemplateRepository emailTemplateRepository, MessageSource messageSource, QuisListingProperties quisListingProperties) {
        this.mailService = mailService;
        this.qlConfigRepository = qlConfigRepository;
        this.pageRepository = pageRepository;
        this.emailTemplateRepository = emailTemplateRepository;
        this.messageSource = messageSource;
        this.quisListingProperties = quisListingProperties;


        TemplateResolver resolver = new TemplateResolver();
        resolver.setResourceResolver(new StringAndClassLoaderResourceResolver());
        resolver.setPrefix("mail/"); // src/main/resources/mail
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding(CharEncoding.UTF_8);
        resolver.setOrder(1);
        this.springTemplateEngine = new SpringTemplateEngine();
        this.springTemplateEngine.setTemplateResolver(resolver);
    }

    public void sendContactUs(ContactDTO contactDTO, String language) {
        QlConfig adminEmailConfig = qlConfigRepository.findOneByKey("admin_email");
        if (adminEmailConfig == null) {
            throw new MissingConfigurationException("Admin email not configured");
        }

        // try and find the template by the selected language
        EmailTemplate contactUsEmailTemplate = emailTemplateRepository.findOneByName("contact-us");
        if (contactUsEmailTemplate == null) {
            throw new MissingConfigurationException("Email template not configured");
        }

        IContext context = new StringAndClassLoaderResourceResolver.StringContext(getValueByLanguage(language, contactUsEmailTemplate.getQlString()));
        context.getVariables().put("name", contactDTO.getName());
        context.getVariables().put("email", contactDTO.getEmail());
        context.getVariables().put("subject", contactDTO.getSubject());
        context.getVariables().put("message", contactDTO.getMessage().replaceAll("(\r\n|\n)", "<br />"));
        String actual = springTemplateEngine.process(REDUNDANT, context);

        mailService.sendEmail(adminEmailConfig.getValue(), contactDTO.getSubject(), actual, false, true);
    }

    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());

        Locale locale = Locale.forLanguageTag(user.getLangKey());
        // try and find the template by the selected language
        EmailTemplate passwordResetEmailTemplate = emailTemplateRepository.findOneByName("password-reset");
        if (passwordResetEmailTemplate == null) {
            throw new MissingConfigurationException("Email template not configured");
        }

        QlConfig siteNameConfig = qlConfigRepository.findOneByKey("site-name");
        if (siteNameConfig == null) {
            throw new MissingConfigurationException("Site name not configured");
        }

        QlConfig resetFinishPageConfig = qlConfigRepository.findOneByKey("reset-password-finish-page-id");
        if (resetFinishPageConfig == null) {
            throw new MissingConfigurationException("Reset finish page not configured");
        }

        Context context = new StringAndClassLoaderResourceResolver.StringContext(getValueByLanguage(user.getLangKey(),
                passwordResetEmailTemplate.getQlString()));

        String title = messageSource.getMessage("email.reset.title", new String[]{siteNameConfig.getValue()}, locale);

        context.setVariable(USER, user);
        context.setVariable(BASE_URL, quisListingProperties.getMail().getBaseUrl());
        context.setVariable(BASE_NAME, siteNameConfig.getValue());
        context.setVariable("title", title);
        context.setVariable("resetText1", messageSource.getMessage("email.reset.text1", new String[]{siteNameConfig.getValue()}, locale));
        context.setVariable("resetFinishPage", getPageSlug(user, resetFinishPageConfig));

        String actual = springTemplateEngine.process(REDUNDANT, context);
        mailService.sendEmail(user.getEmail(), title, actual, false, true);
    }

    public void sendActivationEmail(User user) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());

        EmailTemplate activationEmailTemplate = emailTemplateRepository.findOneByName("activation_email");
        if (activationEmailTemplate == null) {
            throw new MissingConfigurationException("Activation email not configured");
        }

        QlConfig siteNameConfig = qlConfigRepository.findOneByKey("site-name");
        if (siteNameConfig == null) {
            throw new MissingConfigurationException("Site name not configured");
        }

        QlConfig activationPageConfig = qlConfigRepository.findOneByKey("activation-page-id");
        if (activationPageConfig == null) {
            throw new MissingConfigurationException("Activation page not configured");
        }
        String activationPageSlug = getPageSlug(user, activationPageConfig);

        String subject = messageSource.getMessage("email.activation.title", new String[]{siteNameConfig.getValue()}, locale);
        String activationText = messageSource.getMessage("email.activation.text1", new String[]{siteNameConfig.getValue()}, locale);
        Context context = new StringAndClassLoaderResourceResolver
                .StringContext(getValueByLanguage(user.getLangKey(), activationEmailTemplate.getQlString()));
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, quisListingProperties.getMail().getBaseUrl());
        context.setVariable(BASE_NAME, siteNameConfig.getValue());
        context.setVariable(ACTIVATE_PAGE, activationPageSlug);
        context.setVariable(SUBJECT, subject);
        context.setVariable(ACTIVATION_TEXT, activationText);
        String content = springTemplateEngine.process(REDUNDANT, context);
        mailService.sendEmail(user.getEmail(), subject, content, false, true);
    }

    private String getPageSlug(User user, QlConfig activationPageConfig) {
        QlPage activationPage = pageRepository.findOne(Long.valueOf(activationPageConfig.getValue()));
        Translation translation = activationPage.getTranslation();
        String activationPageSlug = activationPage.getName();
        if (!translation.getLanguageCode().equals(user.getLangKey())) {
            // check if there is a translation
            Translation translationForLanguage = translationExists(user.getLangKey(), translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                QlPage oneByTranslation = pageRepository.findOneByTranslation(translationForLanguage);
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
