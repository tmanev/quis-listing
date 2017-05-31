package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.service.dto.EmailTemplateDTO;
import org.springframework.stereotype.Component;

/**
 * Created by adri on 4/5/2017.
 */
@Component
public class EmailTemplateMapper {

    public EmailTemplate emailTemplateDTOToEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setId(emailTemplateDTO.getId());
        emailTemplate.setName(emailTemplateDTO.getName());
        emailTemplate.setText(emailTemplateDTO.getText());
        // qlString is mapped later on
        return emailTemplate;
    }

    public EmailTemplateDTO emailTemplateToEmailTemplateDTO(EmailTemplate emailTemplate) {
        EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
        emailTemplateDTO.setId(emailTemplate.getId());
        emailTemplateDTO.setName(emailTemplate.getName());
        emailTemplateDTO.setText(emailTemplate.getText());
        emailTemplateDTO.setQlString(emailTemplate.getQlString());
        return emailTemplateDTO;
    }
}
