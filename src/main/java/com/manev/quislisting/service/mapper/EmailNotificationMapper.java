package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.EmailNotification;
import com.manev.quislisting.service.dto.EmailNotificationDTO;
import org.springframework.stereotype.Component;

/**
 * Created by adri on 4/5/2017.
 */
@Component
public class EmailNotificationMapper {

    public EmailNotification emailNotificationDTOToEmailNotification(EmailNotificationDTO emailNotificationDTO){
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setId(emailNotificationDTO.getId());
        emailNotification.setName(emailNotificationDTO.getName());
        emailNotification.setText(emailNotificationDTO.getText());
        return emailNotification;
    }
    public EmailNotificationDTO emailNotificationToEmailNotificationDTO(EmailNotification emailNotification){
        EmailNotificationDTO emailNotificationDTO = new EmailNotificationDTO();
        emailNotificationDTO.setId(emailNotification.getId());
        emailNotificationDTO.setName(emailNotification.getName());
        emailNotificationDTO.setText(emailNotification.getText());
        return emailNotificationDTO;
    }
}
