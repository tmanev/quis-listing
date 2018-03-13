package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.DlMessageOverviewRepository;
import com.manev.quislisting.repository.DlMessagesRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.DlMessageDTO;
import com.manev.quislisting.service.form.DlListingMessageForm;
import com.manev.quislisting.service.form.DlWriteMessageForm;
import com.manev.quislisting.service.mapper.DlMessagesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Clock;
import java.util.Optional;

/**
 * Service that holds all methods that are used for the messaging interface between the users.
 */
@Service
@Transactional
public class DlMessagesService {

    private final static Clock clock = Clock.systemUTC();
    private final Logger log = LoggerFactory.getLogger(DlListingService.class);
    private final DlMessagesRepository dlMessagesRepository;
    private final DlMessageOverviewRepository dlMessagesOverviewRepository;
    private final DlMessagesMapper dlMessagesMapper;
    private final DlListingRepository dlListingRepository;
    private final UserService userService;

    public DlMessagesService(final DlMessagesRepository dlMessagesRepository,
                             final DlMessageOverviewRepository dlMessagesOverviewRepository, final DlMessagesMapper dlMessagesMapper, DlListingRepository dlListingRepository, UserService userService) {
        this.dlMessagesRepository = dlMessagesRepository;
        this.dlMessagesOverviewRepository = dlMessagesOverviewRepository;
        this.dlMessagesMapper = dlMessagesMapper;
        this.dlListingRepository = dlListingRepository;
        this.userService = userService;
    }

    public DlMessageDTO create(final DlWriteMessageForm dlWriteMessageForm, final Long dlMessageOverviewId) {
        log.debug("Request to save DlMessageDTO : {}", dlWriteMessageForm);
        DlMessageOverview dlMessageOverview = dlMessagesOverviewRepository.findOne(dlMessageOverviewId);

        final DlMessage dlMessage = new DlMessage();

        // same user that initially sent the message
        dlMessage.setSender(dlMessageOverview.getReceiver());
        dlMessage.setReceiver(dlMessageOverview.getSender());
        dlMessage.setText(dlWriteMessageForm.getText());
        dlMessage.setCreated(new Timestamp(clock.millis()));
        dlMessage.setListingId(dlMessageOverview.getListingId());

        dlMessagesRepository.save(dlMessage);
        //        mailService.sendEmail(listingDTO.getAuthor().getLogin(), String.format(NEW_MAIL_FOR_LISTING,
//                listingDTO.getName()), dlWriteMessageForm.getText(), false, true);

        // update overview messages
        saveMessageOverview(dlMessage);

        return dlMessagesMapper.messageToDlMessageDTO(dlMessage);
    }

    public Page<DlMessageDTO> findAllOverviewMessagesForUser(final Pageable pageable, final Long userId) {
        final Page<DlMessageOverview> result = dlMessagesOverviewRepository.findAllOverviewMessagesForUser(pageable, userId);
        return result.map(dlMessagesMapper::messageToDlMessageDTO);
    }

    public Page<DlMessageDTO> findConversationMessages(final Pageable pageable, final Long listingId,
                                                       final Long receiverUserId, final Long senderUserId) {
        User sender = userService.findOne(senderUserId);
        User receiver = userService.findOne(receiverUserId);
        final Page<DlMessage> result = dlMessagesRepository.findAllMessages(pageable, listingId, sender, receiver);
        return result.map(dlMessagesMapper::messageToDlMessageDTO);
    }

    public DlMessageDTO findMessageOverviewById(final Long messageOverviewId) {
        log.debug("Request to get DlMessageDTO: {}", messageOverviewId);
        final long start = System.currentTimeMillis();
        final DlMessageOverview result = dlMessagesOverviewRepository.findOne(messageOverviewId);
        final DlMessageDTO dlMessagesDTO = dlMessagesMapper.messageToDlMessageDTO(result);
        log.info("findMessageOverviewById messageOverviewId: {}, took: {} ms", messageOverviewId, System.currentTimeMillis() - start);
        return dlMessagesDTO;
    }

    public void delete(final Long messageOverviewId) {
        log.debug("Request to delete DlMessageOverview : {}", messageOverviewId);
        final long start = System.currentTimeMillis();
        final DlMessageOverview dlMessageOverview = dlMessagesOverviewRepository.findOne(messageOverviewId);
        dlMessageOverview.setDeleted(new Timestamp(clock.millis()));
        dlMessagesOverviewRepository.save(dlMessageOverview);
        log.info("Deletion of DlMessage and DlMessageOverview: {}, took: {} ms", messageOverviewId, System.currentTimeMillis() - start);
    }

    public void sendMessageForListing(Long id, DlListingMessageForm form, String currentUserLogin) {
        DlListing dlListing = dlListingRepository.findOne(id);
        User sender = getSender(form, currentUserLogin);
        DlMessage dlMessage = createDlMessage(form, dlListing, sender);
        dlMessagesRepository.save(dlMessage);

        saveMessageOverview(dlMessage);
    }

    private DlMessage createDlMessage(DlListingMessageForm form, DlListing dlListing, User sender) {
        DlMessage dlMessage = new DlMessage();
        dlMessage.setSender(sender);
        dlMessage.setReceiver(dlListing.getUser());
        dlMessage.setListingId(dlListing.getId());
        dlMessage.setText(form.getText());
        dlMessage.setCreated(new Timestamp(System.currentTimeMillis()));
        return dlMessage;
    }

    private void saveMessageOverview(DlMessage dlMessage) {
        DlMessageOverview messageBySenderOrReceiver = dlMessagesOverviewRepository.findOneByDlListingAndSenderAndReceiver(dlMessage.getListingId(), dlMessage.getSender(), dlMessage.getReceiver());
        if (messageBySenderOrReceiver == null) {
            final DlMessageOverview dlMessageOverview = dlMessagesMapper.createMessageOverview(dlMessage);
            dlMessagesOverviewRepository.save(dlMessageOverview);
        } else {
            messageBySenderOrReceiver.setText(dlMessage.getText());
            messageBySenderOrReceiver.setCreated(dlMessage.getCreated());
            messageBySenderOrReceiver.setDeleted(null);
            dlMessagesOverviewRepository.save(messageBySenderOrReceiver);
        }
    }

    private User getSender(DlListingMessageForm form, String currentUserLogin) {
        if (!StringUtils.isEmpty(currentUserLogin)) {
            Optional<User> sender = userService.findOneByLogin(currentUserLogin);
            return sender.orElseGet(() -> userService.createUserFromMessageForm(form.getSenderName(), form.getSenderEmail()));
        } else {
            // create user from form input
            Optional<User> oneByLoginEmail = userService.findOneByLogin(form.getSenderEmail());
            return oneByLoginEmail.orElseGet(() -> userService.createUserFromMessageForm(form.getSenderName(), form.getSenderEmail()));
        }
    }

}
