package com.manev.quislisting.service;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.service.dto.ContactDTO;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService {

    private MailService mailService;
    private QlConfigRepository qlConfigRepository;

    public EmailSendingService(MailService mailService, QlConfigRepository qlConfigRepository) {
        this.mailService = mailService;
        this.qlConfigRepository = qlConfigRepository;
    }

    public void sendContactUs(ContactDTO contactDTO) {

        QlConfig adminEmailConfig = qlConfigRepository.findOneByKey("admin_email");
        if (adminEmailConfig == null) {
            throw new RuntimeException("Admin email not configured");
        }

        String subject = String.format("Contact from %s", contactDTO.getName());
        String content = "Subject: " + contactDTO.getSubject() + "\n" + contactDTO.getMessage();

        mailService.sendEmail(adminEmailConfig.getValue(), subject, content, false, true);
    }

}
