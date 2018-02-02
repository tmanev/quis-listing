package com.manev.quislisting.web.rest;

import com.manev.quislisting.service.EmailSendingService;
import com.manev.quislisting.service.dto.ContactDTO;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ContactRest {

    private final Logger log = LoggerFactory.getLogger(ContactRest.class);
    private final EmailSendingService emailSendingService;

    public ContactRest(EmailSendingService emailSendingService) {
        this.emailSendingService = emailSendingService;
    }


    @RequestMapping(value = RestRouter.Contact.BASE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createEmailNotification(@Valid @RequestBody ContactDTO contactDTO) {
        log.debug("REST request to sent ContactDTO : {}", contactDTO);

        emailSendingService.sendContactUs(contactDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlert("Message sent", "Contacts"))
                .build();
    }

}