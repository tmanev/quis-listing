package com.manev.quislisting.web.rest;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.DlMessageDTO;
import com.manev.quislisting.service.form.DlWriteMessageForm;
import com.manev.quislisting.service.post.DlMessagesService;
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

import javax.validation.Valid;
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

    private final Logger log = LoggerFactory.getLogger(DlListingRest.class);

    private final DlMessagesService dlMessagesService;

    private final UserService userService;

    public DlMessageRest(final DlMessagesService dlMessagesService, final UserService userService) {
        this.dlMessagesService = dlMessagesService;
        this.userService = userService;
    }

    @PostMapping(RestRouter.DlMessageCenter.CONVERSATION_THREAD)
    public ResponseEntity<DlMessageDTO> writeMessage(@PathVariable Long dlMessageOverviewId, @Valid @RequestBody DlWriteMessageForm dlWriteMessageForm) throws URISyntaxException {
        log.debug("REST request to save DlMessageForm : {}", dlWriteMessageForm);

        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final Optional<User> receiver = userService.findOneByLogin(currentUserLogin);
        if (!receiver.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        DlMessageDTO dlMessageOverview = dlMessagesService.findMessageOverviewById(dlMessageOverviewId);
        if (!dlMessageOverview.getReceiver().getId().equals(receiver.get().getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final DlMessageDTO result = dlMessagesService.create(dlWriteMessageForm, dlMessageOverviewId);

        return ResponseEntity.created(new URI(RestRouter.DlMessageCenter.CONVERSATIONS + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @GetMapping(RestRouter.DlMessageCenter.CONVERSATIONS)
    public ResponseEntity<List<DlMessageDTO>> getAllConversationsForLoggedUser(final Pageable pageable) {
        log.debug("REST request to get overview messages for logged user");

        final String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        final Optional<User> currentUser = userService.findOneByLogin(currentUserLogin);
        if (!currentUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final Page<DlMessageDTO> page = dlMessagesService.findAllOverviewMessagesForUser(pageable, currentUser.get().getId());
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RestRouter.DlMessageCenter.CONVERSATIONS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(RestRouter.DlMessageCenter.CONVERSATION_THREAD)
    public ResponseEntity<List<DlMessageDTO>> getConversationThread(Pageable pageable, @PathVariable Long dlMessageOverviewId) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userService.findOneByLogin(currentUserLogin);
        if (!currentUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        DlMessageDTO messageOverviewById = dlMessagesService.findMessageOverviewById(dlMessageOverviewId);
        if (!messageOverviewById.getReceiver().getId().equals(currentUser.get().getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Page<DlMessageDTO> page = dlMessagesService.findConversationMessages(pageable, messageOverviewById.getListingId(), messageOverviewById.getReceiver().getId(), messageOverviewById.getSender().getId());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RestRouter.DlMessageCenter.CONVERSATIONS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping(RestRouter.DlMessageCenter.CONVERSATION_THREAD)
    public ResponseEntity<Void> deleteConversationThread(final @PathVariable Long dlMessageOverviewId) {
        log.debug("REST request to delete DlMessage and DlMessageOverview : {}", dlMessageOverviewId);

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userService.findOneByLogin(currentUserLogin);
        if (!currentUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        DlMessageDTO messageOverviewById = dlMessagesService.findMessageOverviewById(dlMessageOverviewId);
        if (!messageOverviewById.getReceiver().getId().equals(currentUser.get().getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        dlMessagesService.delete(dlMessageOverviewId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, dlMessageOverviewId.toString()))
                .build();
    }

}
