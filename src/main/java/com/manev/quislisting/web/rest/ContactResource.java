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
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Locale;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_CONTACTS;

@RestController
@RequestMapping(RESOURCE_API_CONTACTS)
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);
    private final EmailSendingService emailSendingService;

    private LocaleResolver localeResolver;

    public ContactResource(EmailSendingService emailSendingService, LocaleResolver localeResolver) {
        this.emailSendingService = emailSendingService;
        this.localeResolver = localeResolver;
    }


    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createEmailNotification(@Valid @RequestBody ContactDTO contactDTO,
                                                        HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to sent ContactDTO : {}", contactDTO);

        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        log.debug("Language from cookie: {}", language);

        emailSendingService.sendContactUs(contactDTO, language);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlert("Thank you for your message. It has been sent.", "Contacts"))
                .build();
    }

}