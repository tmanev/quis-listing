package com.manev.quislisting.service;

import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.repository.EmailTemplateRepository;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.service.dto.ContactDTO;
import com.manev.quislisting.service.util.StringAndClassLoaderResourceResolver;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

@Service
public class EmailSendingService {

    private MailService mailService;
    private QlConfigRepository qlConfigRepository;
    private SpringTemplateEngine springTemplateEngine;

    private EmailTemplateRepository emailTemplateRepository;

    private LocaleResolver localeResolver;

    public EmailSendingService(MailService mailService, QlConfigRepository qlConfigRepository,
                               EmailTemplateRepository emailTemplateRepository, LocaleResolver localeResolver) {
        this.mailService = mailService;
        this.qlConfigRepository = qlConfigRepository;
        this.emailTemplateRepository = emailTemplateRepository;
        this.localeResolver = localeResolver;

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

    public void sendContactUs(ContactDTO contactDTO) {
        QlConfig adminEmailConfig = qlConfigRepository.findOneByKey("admin_email");
        if (adminEmailConfig == null) {
            throw new RuntimeException("Admin email not configured");
        }

        // try and find the template by the selected language
        EmailTemplate contactUsEmailTemplate = emailTemplateRepository.findOneByName("contact-us");
        if (contactUsEmailTemplate == null) {
            throw new RuntimeException("Email template not configured");
        }

        IContext context = new StringAndClassLoaderResourceResolver.StringContext(contactUsEmailTemplate.getText());
        context.getVariables().put("name", contactDTO.getName());
        context.getVariables().put("email", contactDTO.getEmail());
        context.getVariables().put("subject", contactDTO.getSubject());
        context.getVariables().put("message", contactDTO.getMessage().replaceAll("(\r\n|\n)", "<br />"));
        context.getVariables().put("sendBy", "This e-mail was sent from a contact form on Quis Listing (http://quislisting.com)");
        String actual = springTemplateEngine.process("redundant", context);

        mailService.sendEmail(adminEmailConfig.getValue(), contactDTO.getSubject(), actual, false, true);
    }

}
