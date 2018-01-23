package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.repository.EmailTemplateRepository;
import com.manev.quislisting.service.EmailTemplateService;
import com.manev.quislisting.service.dto.EmailTemplateDTO;
import com.manev.quislisting.web.rest.AdminRestRouter;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import com.manev.quislisting.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@RestController
public class EmailTemplateAdminRest {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateRepository.class);
    private final EmailTemplateService emailTemplateService;

    public EmailTemplateAdminRest(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @PostMapping(AdminRestRouter.EmailTemplate.LIST)
    public ResponseEntity<EmailTemplateDTO> createEmailNotification(@RequestBody EmailTemplateDTO emailTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save EmailNotificationDTO : {}", emailTemplateDTO);
        if (emailTemplateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
                    "A new entity cannot already have an ID")).body(null);


        }
        EmailTemplateDTO result = emailTemplateService.save(emailTemplateDTO);
        return ResponseEntity.created(new URI(AdminRestRouter.EmailTemplate.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.EmailTemplate.LIST)
    public ResponseEntity<EmailTemplateDTO> updateEmailNotification(@RequestBody EmailTemplateDTO emailTemplateDTO) throws URISyntaxException {
        log.debug("REST request to update EmailTemplateDTO : {}", emailTemplateDTO);
        if (emailTemplateDTO.getId() == null) {
            return createEmailNotification(emailTemplateDTO);

        }
        EmailTemplateDTO result = emailTemplateService.save(emailTemplateDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emailTemplateDTO.getId().toString()))
                .body(result);
    }


    @GetMapping(AdminRestRouter.EmailTemplate.LIST)
    public ResponseEntity<List<EmailTemplateDTO>> getAllEmailNotification(Pageable pageable) {
        log.debug("REST request to get a page of EmailTemplateDTO");
        Page<EmailTemplateDTO> page = emailTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.EmailTemplate.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.EmailTemplate.DETAIL)
    public ResponseEntity<EmailTemplateDTO> getEmailNotification(@PathVariable Long id) {
        log.debug("REST request to get EmailTemplate: {", id);
        EmailTemplateDTO emailTemplateDTO = emailTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(emailTemplateDTO));
    }

    @DeleteMapping(AdminRestRouter.EmailTemplate.DETAIL)
    public ResponseEntity<Void> deleteEmailNotification(@PathVariable Long id) {
        log.debug("REST request to delete EmailTemplateDTO : {}", id);
        emailTemplateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


}