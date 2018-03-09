package com.manev.quislisting.web.rest;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.MailService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.DlMessageDTO;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.DlMessagesService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Rest controller that is responsible for all GET and POST requests for the messages interface.
 */
@RestController
public class DlMessageRest {

    private static final String ENTITY_NAME = "DlMessages";
    private static final String NEW_MAIL_FOR_LISTING = "You received new email for your listing %s";

    private final Logger log = LoggerFactory.getLogger(DlListingRest.class);

    private final DlMessagesService dlMessagesService;

    private final UserService userService;

    private final DlListingService dlListingService;

    private final MailService mailService;

    public DlMessageRest(final DlMessagesService dlMessagesService, final UserService userService,
                         final DlListingService dlListingService, final MailService mailService) {
        this.dlMessagesService = dlMessagesService;
        this.userService = userService;
        this.dlListingService = dlListingService;
        this.mailService = mailService;
    }

    @PostMapping(RestRouter.DlMessage.LIST)
    public ResponseEntity<DlMessageDTO> createDlMessage(final @RequestBody DlMessageDTO dlMessageDTO) throws URISyntaxException {
        log.debug("REST request to save DlMessageDTO : {}", dlMessageDTO);

        if (dlMessageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
                            "A new entity cannot already have an ID")).body(null);
        }

        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final Optional<User> sender = userService.findOneByLogin(currentUserLogin);

        final DlListingDTO listingDTO = dlListingService.findOne(dlMessageDTO.getListingId());

        if (dlMessageDTO.getReceiverId() == null) {
            dlMessageDTO.setReceiverId(listingDTO.getAuthor().getId());
        }

        dlMessageDTO.setListingId(listingDTO.getId());

        final DlMessageDTO result = dlMessagesService.save(dlMessageDTO, sender);

        mailService.sendEmail(listingDTO.getAuthor().getLogin(), String.format(NEW_MAIL_FOR_LISTING,
                listingDTO.getName()), dlMessageDTO.getText(), false, true);

        return ResponseEntity.created(new URI(RestRouter.DlMessage.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @GetMapping(RestRouter.DlMessage.LIST)
    public ResponseEntity<List<DlMessageDTO>> getAllMessagesByReceiver(final Pageable pageable) {
        log.debug("REST request to get a page of DlMessageDTO");

        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final Optional<User> oneByLogin = userService.findOneByLogin(currentUserLogin);
        if (!oneByLogin.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final Page<DlMessageDTO> page = dlMessagesService.findAllMessagesByReceiver(pageable, oneByLogin.get().getId());
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RestRouter.DlMessage.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping(RestRouter.DlMessage.DETAIL)
    public ResponseEntity<Void> deleteDlMessages(final @PathVariable Long listingId) {
        log.debug("REST request to delete DlMessage and DlMessageOverview : {}", listingId);
        dlMessagesService.delete(listingId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, listingId.toString()))
                .build();
    }

}
