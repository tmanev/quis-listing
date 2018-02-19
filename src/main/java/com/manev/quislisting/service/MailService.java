package com.manev.quislisting.service;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.User;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;

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

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final QuisListingProperties quisListingProperties;

    private final JavaMailSender javaMailSender;

    public MailService(final QuisListingProperties quisListingProperties, final JavaMailSender javaMailSender) {

        this.quisListingProperties = quisListingProperties;
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(final String to, final String subject, final String content, final boolean isMultipart,
            final boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(quisListingProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (final Exception e) {
            log.warn("E-mail could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendSocialRegistrationValidationEmail(final User user, final String provider) {
        log.debug("Sending social registration validation email to '{}'", user.getEmail());
        final Locale locale = Locale.forLanguageTag(user.getLangKey());
        final Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, user.getLogin());
        context.setVariable("provider", StringUtils.capitalize(provider));
        sendEmail(user.getLogin(), "subject", "content", false, true);
    }

}
