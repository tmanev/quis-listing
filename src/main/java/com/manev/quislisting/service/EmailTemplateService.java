package com.manev.quislisting.service;

import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.repository.EmailTemplateRepository;
import com.manev.quislisting.service.dto.EmailTemplateDTO;
import com.manev.quislisting.service.mapper.EmailTemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by adri on 4/4/2017.
 */
@Service
@Transactional
public class EmailTemplateService {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateService.class);

    private EmailTemplateRepository emailTemplateRepository;
    private EmailTemplateMapper emailTemplateMapper;

    public EmailTemplateService(EmailTemplateRepository emailTemplateRepository, EmailTemplateMapper emailTemplateMapper) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailTemplateMapper = emailTemplateMapper;
    }

    public EmailTemplateDTO save(EmailTemplateDTO emailTemplateDTO) {
        log.debug("Request to save EmailTemplateDTO : {}", emailTemplateDTO);

        EmailTemplate emailTemplate = emailTemplateMapper.emailTemplateDTOToEmailTemplate(emailTemplateDTO);
        EmailTemplate emailTemplateSaved = emailTemplateRepository.save(emailTemplate);
        saveQlString(emailTemplateSaved);

        return emailTemplateMapper.emailTemplateToEmailTemplateDTO(emailTemplateSaved);
    }

    public Page<EmailTemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmailTemplateDTO");
        Page<EmailTemplate> result = emailTemplateRepository.findAll(pageable);
        return result.map(emailTemplateMapper::emailTemplateToEmailTemplateDTO);
    }

    public EmailTemplateDTO findOne(Long id) {
        log.debug("Request to get EmailTemplateDTO: {}", id);
        EmailTemplate result = emailTemplateRepository.findOne(id);
        return result != null ? emailTemplateMapper.emailTemplateToEmailTemplateDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete EmailTemplateService : {}", id);
        emailTemplateRepository.delete(id);
    }

    private EmailTemplate saveQlString(EmailTemplate emailTemplate) {
        if (emailTemplate.getQlString() == null) {
            emailTemplate.setQlString(new QlString()
                    .languageCode("en")
                    .context("email-template")
                    .name("email-template-#" + emailTemplate.getId())
                    .value(emailTemplate.getText())
                    .status(0));
        } else {
            QlString qlString = emailTemplate.getQlString();
            if (!qlString.getValue().equals(emailTemplate.getName())) {
                qlString.setValue(emailTemplate.getText());
                qlString.setStatus(0);
            }
        }
        emailTemplate = emailTemplateRepository.save(emailTemplate);
        return emailTemplate;
    }
}


