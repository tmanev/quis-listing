package com.manev.quislisting.service;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.repository.EmailTemplateRepository;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.service.util.StringAndClassLoaderResourceResolver;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Set;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";
    private static final String BASE_NAME = "baseName";
    private static final String SUBJECT = "subject";
    private static final String ACTIVATION_TEXT = "activationText";
    private final Logger log = LoggerFactory.getLogger(MailService.class);
    private final QuisListingProperties quisListingProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final EmailTemplateRepository emailTemplateRepository;

    private SpringTemplateEngine springTemplateEngine;

    private QlConfigRepository qlConfigRepository;

    public MailService(QuisListingProperties quisListingProperties, JavaMailSender javaMailSender,
                       MessageSource messageSource, SpringTemplateEngine templateEngine,
                       EmailTemplateRepository emailTemplateRepository, QlConfigRepository qlConfigRepository) {

        this.quisListingProperties = quisListingProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.emailTemplateRepository = emailTemplateRepository;
        this.qlConfigRepository = qlConfigRepository;

        TemplateResolver resolver = new TemplateResolver();
        resolver.setResourceResolver(new StringAndClassLoaderResourceResolver());
        resolver.setPrefix("mail/"); // src/main/resources/mail
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding(CharEncoding.UTF_8);
        resolver.setOrder(1);

        this.springTemplateEngine = new SpringTemplateEngine();
        this.springTemplateEngine.setTemplateEngineMessageSource(messageSource);
        this.springTemplateEngine.setTemplateResolver(resolver);
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(quisListingProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());

        EmailTemplate activationEmailTemplate = emailTemplateRepository.findOneByName("activation_email");
        if (activationEmailTemplate == null) {
            throw new RuntimeException("Activation email not configured");
        }

        QlConfig siteNameConfig = qlConfigRepository.findOneByKey("site-name");
        if (siteNameConfig == null) {
            throw new RuntimeException("Site name not configured");
        }

        Locale locale = Locale.forLanguageTag(user.getLangKey());
        String subject = messageSource.getMessage("email.activation.title", new String[]{siteNameConfig.getValue()}, locale);
        String activationText = messageSource.getMessage("email.activation.text1", new String[]{siteNameConfig.getValue()}, locale);
        Context context = new StringAndClassLoaderResourceResolver
                .StringContext(getValueByLanguage(user.getLangKey(), activationEmailTemplate.getQlString()));
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, quisListingProperties.getMail().getBaseUrl());
        context.setVariable(BASE_NAME, siteNameConfig.getValue());
        context.setVariable(SUBJECT, subject);
        context.setVariable(ACTIVATION_TEXT, activationText);
        String content = springTemplateEngine.process("redundant", context);
        sendEmail(user.getEmail(), subject, content, false, true);
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

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, quisListingProperties.getMail().getBaseUrl());
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, quisListingProperties.getMail().getBaseUrl());
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
}
