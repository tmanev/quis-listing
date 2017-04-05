package com.manev.quislisting.web.rest;

import com.manev.quislisting.domain.EmailNotification;
import com.manev.quislisting.repository.EmailNotificationRepository;
import com.manev.quislisting.service.EmailNotificationService;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.service.dto.EmailNotificationDTO;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import com.manev.quislisting.web.rest.util.ResponseUtil;
import com.sun.enterprise.module.Repository;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_EMAIL_NOTIFICATION;
import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Created by adri on 4/5/2017.
 */
@RequestMapping(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION)
public class EmailNotificationResource {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationRepository.class);
    private final EmailNotificationService emailNotificationService;

    public EmailNotificationResource(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }


    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailNotificationDTO> createEmailNotification(@RequestBody EmailNotificationDTO emailNotificationDTO) throws URISyntaxException {
        log.debug("REST request to save EmailNotificatinDTO : {}", emailNotificationDTO);
        if (emailNotificationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
                    "A new entity cannot already have an ID")).body(null);


        }
        EmailNotificationDTO result = emailNotificationService.save(emailNotificationDTO);
        return ResponseEntity.created(new URI(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION + "/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);


    }

    @PutMapping
    public ResponseEntity<EmailNotificationDTO> updateEmailNotification(@RequestBody EmailNotificationDTO emailNotificationDTO) throws URISyntaxException{
        log.debug("REST request to update EmailNotificationDTO : {}", emailNotificationDTO);
        if(emailNotificationDTO.getId()==null){
            return createEmailNotification(emailNotificationDTO);

        }
        EmailNotificationDTO result = emailNotificationService.save(emailNotificationDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emailNotificationDTO.getId().toString()))
                .body(result);
    }


    @GetMapping
    public  ResponseEntity<List<EmailNotificationDTO>> getAllEmailNotification(Pageable pageable) throws URISyntaxException{
        log.debug("REST request to get a page of EmailNotificationDTO");
        Page<EmailNotificationDTO> page = emailNotificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_ADMIN_EMAIL_NOTIFICATION);
        return  new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailNotificationDTO> getEmailNotification(@PathVariable Long id){
        log.debug("REST request to get EmailNotification: {", id);
        EmailNotificationDTO emailNotificationDTO= emailNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(emailNotificationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailNotification(@PathVariable Long id){
        log.debug("REST request to delete EmailNotificationDTO : {}", id);
        emailNotificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }



}