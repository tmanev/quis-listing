package com.manev.quislisting.service;

import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.repository.EmailTemplateRepository;
import com.manev.quislisting.service.dto.EmailTemplateDTO;
import com.manev.quislisting.service.mapper.EmailTemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Created by adri on 4/4/2017.
 */
@Service
@Transactional
public class EmailTemplateService {

    public static final String CONTEXT = "email-template";
    private final Logger log = LoggerFactory.getLogger(EmailTemplateService.class);

    private EmailTemplateRepository emailTemplateRepository;
    private EmailTemplateMapper emailTemplateMapper;

    public EmailTemplateService(EmailTemplateRepository emailTemplateRepository, EmailTemplateMapper emailTemplateMapper) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailTemplateMapper = emailTemplateMapper;
    }

    public EmailTemplateDTO save(EmailTemplateDTO emailTemplateDTO) {
        log.debug("Request to save EmailTemplateDTO : {}", emailTemplateDTO);

        EmailTemplate emailTemplate;
        if (emailTemplateDTO.getId()==null) {
            emailTemplate = emailTemplateMapper.emailTemplateDTOToEmailTemplate(emailTemplateDTO);
        } else {
            emailTemplate = emailTemplateRepository.findOne(emailTemplateDTO.getId());
            emailTemplate.setName(emailTemplateDTO.getName());
            emailTemplate.setText(emailTemplateDTO.getText());
        }

        EmailTemplate emailTemplateSaved = emailTemplateRepository.save(emailTemplate);
        saveQlString(emailTemplateSaved, emailTemplateDTO.getQlString());

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

    private EmailTemplate saveQlString(EmailTemplate emailTemplate, QlString qlString) {
        if (emailTemplate.getQlString() == null) {
            emailTemplate.setQlString(new QlString()
                    .languageCode(qlString.getLanguageCode())
                    .context(CONTEXT)
                    .name("email-template-#" + emailTemplate.getId())
                    .value(qlString.getValue())
                    .status(0));
            // store and translations
            Set<StringTranslation> stringTranslation = qlString.getStringTranslation();
            for (StringTranslation translation : stringTranslation) {
                StringTranslation translationToBeSaved = new StringTranslation();
                translationToBeSaved.setLanguageCode(translation.getLanguageCode());
                translationToBeSaved.setQlString(emailTemplate.getQlString());
                translationToBeSaved.setStatus(Boolean.FALSE);
                translationToBeSaved.setValue(translation.getValue());
                translationToBeSaved.setTranslationDate(ZonedDateTime.now());
                emailTemplate.getQlString().addStringTranslation(translationToBeSaved);
            }
        } else {
            QlString existingQlString = emailTemplate.getQlString();
            existingQlString.setValue(qlString.getValue());

            Set<StringTranslation> stringTranslation = qlString.getStringTranslation();
            for (StringTranslation translation : stringTranslation) {
                StringTranslation stringTranslationByLanguageCode = findStringTranslationByLanguageCode(translation.getLanguageCode(), existingQlString.getStringTranslation());
                if (stringTranslationByLanguageCode == null) {
                    // create
                    StringTranslation newTranslaton = new StringTranslation();
                    newTranslaton.setLanguageCode(translation.getLanguageCode());
                    newTranslaton.setValue(translation.getValue());
                    newTranslaton.setStatus(Boolean.FALSE);
                    newTranslaton.setQlString(existingQlString);
                    newTranslaton.setTranslationDate(ZonedDateTime.now());
                } else {
                    // update
                    stringTranslationByLanguageCode.setValue(translation.getValue());
                    stringTranslationByLanguageCode.setTranslationDate(ZonedDateTime.now());
                }
            }
        }
        emailTemplate = emailTemplateRepository.save(emailTemplate);
        return emailTemplate;
    }

    private StringTranslation findStringTranslationByLanguageCode(String languageCode, Set<StringTranslation> stringTranslations) {
        for (StringTranslation stringTranslation : stringTranslations) {
            if (stringTranslation.getLanguageCode().equals(languageCode)) {
                return stringTranslation;
            }
        }

        return null;
    }
}


