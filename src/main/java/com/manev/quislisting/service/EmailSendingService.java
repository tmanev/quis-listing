package com.manev.quislisting.service;

import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.repository.EmailTemplateRepository;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.service.dto.ContactDTO;
import com.manev.quislisting.service.exception.QlServiceException;
import com.manev.quislisting.service.util.StringAndClassLoaderResourceResolver;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.Set;

@Service
public class EmailSendingService {

    private MailService mailService;
    private QlConfigRepository qlConfigRepository;
    private SpringTemplateEngine springTemplateEngine;

    private EmailTemplateRepository emailTemplateRepository;


    public EmailSendingService(MailService mailService, QlConfigRepository qlConfigRepository,
                               EmailTemplateRepository emailTemplateRepository) {
        this.mailService = mailService;
        this.qlConfigRepository = qlConfigRepository;
        this.emailTemplateRepository = emailTemplateRepository;

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
            throw new RuntimeException("Admin email not configured");
        }

        // try and find the template by the selected language
        EmailTemplate contactUsEmailTemplate = emailTemplateRepository.findOneByName("contact-us");
        if (contactUsEmailTemplate == null) {
            throw new RuntimeException("Email template not configured");
        }

        IContext context = new StringAndClassLoaderResourceResolver.StringContext(getValueByLanguage(language, contactUsEmailTemplate.getQlString()));
        context.getVariables().put("name", contactDTO.getName());
        context.getVariables().put("email", contactDTO.getEmail());
        context.getVariables().put("subject", contactDTO.getSubject());
        context.getVariables().put("message", contactDTO.getMessage().replaceAll("(\r\n|\n)", "<br />"));
        String actual = springTemplateEngine.process("redundant", context);

        mailService.sendEmail(adminEmailConfig.getValue(), contactDTO.getSubject(), actual, false, true);
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
