package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.EmailNotification;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.repository.EmailNotificationRepository;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.service.dto.EmailNotificationDTO;
import com.manev.quislisting.service.mapper.DlContentFieldMapper;
import com.manev.quislisting.service.mapper.EmailNotificationMapper;
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
public class EmailNotificationService {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    private EmailNotificationRepository emailNotificationRepository;
    private EmailNotificationMapper emailNotificationMapper;

    public EmailNotificationService(EmailNotificationRepository emailNotificationRepository, EmailNotificationMapper emailNotificationMapper){
        this.emailNotificationRepository=emailNotificationRepository;
        this.emailNotificationMapper=emailNotificationMapper;
    }

    public EmailNotificationDTO save(EmailNotificationDTO emailNotificationDTO){
        log.debug("Request to save EmailNotificationDTO : {}", emailNotificationDTO);

        EmailNotification emailNotification=emailNotificationMapper.emailNotificationDTOToEmailNotification(emailNotificationDTO);
        EmailNotification emailNotificationSaved=emailNotificationRepository.save(emailNotification);
        return emailNotificationMapper.emailNotificationToEmailNotificationDTO(emailNotificationSaved);

    }
    public Page<EmailNotificationDTO> findAll(Pageable pageable){
        log.debug("Request to get all EmailNotificationDTO");
        Page<EmailNotification> result =emailNotificationRepository.findAll(pageable);
        return result.map(emailNotificationMapper::emailNotificationToEmailNotificationDTO);
    }
    public  EmailNotificationDTO findOne(Long id){
        log.debug("Request to get EmailNotificationDTO: {}", id);
        EmailNotification result= emailNotificationRepository.findOne(id);
        return result!=null ? emailNotificationMapper.emailNotificationToEmailNotificationDTO(result) : null;
    }



    public void delete(Long id) {
        log.debug("Request to delete EmailNotificationService : {}", id);
        emailNotificationRepository.delete(id);
    }
}


