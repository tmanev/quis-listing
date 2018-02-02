package com.manev.quislisting.service;

import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.service.dto.ContactDTO;
import com.manev.quislisting.service.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class EmailSendingService {

    private static final String BASE_URL = "baseUrl";
    private static final String USER = "user";
    private static final String BASE_NAME = "baseName";
    private static final String DL_LISTING = "dlListing";
    private static final String SITE_NAME = "site-name";
    private static final String ACTIVATION_LINK = "activationLink";
    private static final String EMAIL_LOGO_IMAGE = "emailLogoImage";
    private static final String INNER_EMAIL = "innerEmail";
    private static final String SUPPORT_EMAIL = "supportEmail";
    private static final String CURRENT_YEAR = "currentYear";
    private static final String RESET_LINK = "resetLink";
    private final Logger log = LoggerFactory.getLogger(EmailSendingService.class);
    private final MessageSource messageSource;
    private final QlConfigService qlConfigService;
    private final EmailTemplateService emailTemplateService;
    private MailService mailService;

    private TemplateEngineComponent templateEngineComponent;

    public EmailSendingService(MailService mailService,
                               MessageSource messageSource,
                               QlConfigService qlConfigService, EmailTemplateService emailTemplateService, TemplateEngineComponent templateEngineComponent) {
        this.mailService = mailService;
        this.messageSource = messageSource;
        this.qlConfigService = qlConfigService;
        this.emailTemplateService = emailTemplateService;
        this.templateEngineComponent = templateEngineComponent;
    }

    @Async
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

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());

        Locale locale = Locale.forLanguageTag(user.getLangKey());
        // try and find the template by the selected language
        EmailTemplate passwordResetEmailTemplate = emailTemplateService.findOneByName("password-reset");

        QlConfig siteNameConfig = qlConfigService.findOneByKey(SITE_NAME);

        String html = getValueByLanguage(user.getLangKey(), passwordResetEmailTemplate.getQlString());
        String title = messageSource.getMessage("email.reset.title", null, locale);

        Map<String, Object> variables = new HashMap<>();
        variables.put(USER, user);
        variables.put(BASE_URL, getBaseUrl());
        variables.put(BASE_NAME, siteNameConfig.getValue());
        variables.put(RESET_LINK, getBaseUrl() + "/password-reset?key=" + user.getResetKey());

        String innerHtml = templateEngineComponent.getTemplateFromMap(html, variables);
        String finalHtmlContent = processFinalEmailTemplate(innerHtml, locale.getLanguage());
        mailService.sendEmail(user.getEmail(), title, finalHtmlContent, false, true);
    }

    public void sendListingDisapprovedEmail(DlListing dlListing, String reason) {
        User user = dlListing.getUser();
        log.debug("Sending listing disapprove e-mail to '{}'", user.getEmail());

        Locale locale = Locale.forLanguageTag(user.getLangKey());

        EmailTemplate activationEmailTemplate = emailTemplateService.findOneByName("listing-disapproved");

        QlConfig siteNameConfig = qlConfigService.findOneByKey(SITE_NAME);

        String subject = messageSource.getMessage("email.listing_disapproved.subject", new String[]{dlListing.getTitle()}, locale);

        String html = getValueByLanguage(user.getLangKey(), activationEmailTemplate.getQlString());

        Map<String, Object> variables = new HashMap<>();

        variables.put(USER, user);
        variables.put(BASE_NAME, siteNameConfig.getValue());
        variables.put(BASE_URL, getBaseUrl());
        variables.put("previewListingUrl", UrlUtil.makePreviewListingUrl(getBaseUrl(), dlListing.getId()));
        variables.put("editListingUrl", UrlUtil.makeEditListingUrl(getBaseUrl(), dlListing.getId()));
        variables.put("reason_label", messageSource.getMessage("email.listing_disapproved.label.reason", new String[]{dlListing.getTitle()}, locale));
        variables.put("reason", reason);
        variables.put(DL_LISTING, dlListing);

        String emailContent = templateEngineComponent.getTemplateFromMap(html, variables);
        mailService.sendEmail(user.getEmail(), subject, emailContent, false, true);
    }

    public void sendListingApprovedEmail(DlListing dlListing) {
        User user = dlListing.getUser();
        log.debug("Sending listing approve e-mail to '{}'", user.getEmail());

        Locale locale = Locale.forLanguageTag(user.getLangKey());

        EmailTemplate activationEmailTemplate = emailTemplateService.findOneByName("listing-approved");

        QlConfig siteNameConfig = qlConfigService.findOneByKey(SITE_NAME);

        String subject = messageSource.getMessage("email.listing_approved.subject", new String[]{dlListing.getTitle()}, locale);

        String html = getValueByLanguage(user.getLangKey(), activationEmailTemplate.getQlString());

        Map<String, Object> variables = new HashMap<>();

        variables.put(USER, user);
        variables.put(BASE_NAME, siteNameConfig.getValue());
        variables.put(BASE_URL, getBaseUrl());
        variables.put("listingUrl", UrlUtil.makePublicListingUrl(getBaseUrl(), dlListing.getId(), dlListing.getName()));
        variables.put(DL_LISTING, dlListing);

        String emailContent = templateEngineComponent.getTemplateFromMap(html, variables);
        mailService.sendEmail(user.getEmail(), subject, emailContent, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());

        EmailTemplate activationEmailTemplate = emailTemplateService.findOneByName("activation_email");
        String subject = messageSource.getMessage("email.activation.title", null, locale);

        String html = getValueByLanguage(user.getLangKey(), activationEmailTemplate.getQlString());

        Map<String, Object> variables = new HashMap<>();
        variables.put(BASE_NAME, getSiteName());
        variables.put(BASE_URL, getBaseUrl());
        variables.put(USER, user);
        variables.put(ACTIVATION_LINK, getBaseUrl() + "/account-activate?key=" + user.getActivationKey());
        variables.put(SUPPORT_EMAIL, getSupportEmail());

        String innerEmailContent = templateEngineComponent.getTemplateFromMap(html, variables);
        String emailContent = processFinalEmailTemplate(innerEmailContent, locale.getLanguage());
        mailService.sendEmail(user.getEmail(), subject, emailContent, false, true);

        // re-sent the email to admin
        QlConfig adminEmailConfig = qlConfigService.findOneByKey("admin_email");
        mailService.sendEmail(adminEmailConfig.getValue(), "New user has been registered", String.format("User id is: %s, with email: %s", user.getId(), user.getEmail()), false, true);
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

    public void sendPublishedNotification(DlListing savedDlListing) {
        log.debug("Sending published info e-mail.");

        QlConfig publishRequestAdmin = qlConfigService.findOneByKey("publish_request_admin");

        String subject = String.format("Listing published! Id: %s", savedDlListing.getId());
        String publishText = String.format("Listing with title: %s, </br> has been published", savedDlListing.getTitle());

        mailService.sendEmail(publishRequestAdmin.getValue(), subject, publishText, false, true);
    }

    private String processFinalEmailTemplate(String innerEmail, String languageCode) {
        EmailTemplate baseEmailTemplate = emailTemplateService.findOneByName("base_email_template");
        String baseHtml = getValueByLanguage(languageCode, baseEmailTemplate.getQlString());

        Map<String, Object> variables = new HashMap<>();
        variables.put(BASE_NAME, getSiteName());
        variables.put(BASE_URL, getBaseUrl());
        variables.put(EMAIL_LOGO_IMAGE, getBaseUrl()+"/resources/images/logo-ql.png");
        variables.put(CURRENT_YEAR, Calendar.getInstance().get(Calendar.YEAR));
        variables.put(INNER_EMAIL, innerEmail);

        return templateEngineComponent.getTemplateFromMap(baseHtml, variables);
    }

    private String getSiteName() {
        QlConfig siteNameConfig = qlConfigService.findOneByKey(SITE_NAME);
        return siteNameConfig.getValue();
    }

    private String getBaseUrl() {
        QlConfig baseUrl = qlConfigService.findOneByKey("base-url");
        return baseUrl.getValue();
    }

    private String getSupportEmail() {
        QlConfig baseUrl = qlConfigService.findOneByKey("support_email");
        return baseUrl.getValue();
    }
}
