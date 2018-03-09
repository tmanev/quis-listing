package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.DlMessagesOverviewRepository;
import com.manev.quislisting.repository.DlMessagesRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.DlMessageDTO;
import com.manev.quislisting.service.form.DlListingMessageForm;
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
import java.util.List;
import java.util.Optional;

/**
 * Service that holds all methods that are used for the messaging interface between the users.
 */
@Service
@Transactional
public class DlMessagesService {

    private final Logger log = LoggerFactory.getLogger(DlListingService.class);

    private final static Clock CLOCK = Clock.systemUTC();

    private final DlMessagesRepository dlMessagesRepository;
    private final DlMessagesOverviewRepository dlMessagesOverviewRepository;
    private final DlMessagesMapper dlMessagesMapper;
    private final DlListingRepository dlListingRepository;
    private final UserService userService;

    public DlMessagesService(final DlMessagesRepository dlMessagesRepository,
                             final DlMessagesOverviewRepository dlMessagesOverviewRepository, final DlMessagesMapper dlMessagesMapper, DlListingRepository dlListingRepository, UserService userService) {
        this.dlMessagesRepository = dlMessagesRepository;
        this.dlMessagesOverviewRepository = dlMessagesOverviewRepository;
        this.dlMessagesMapper = dlMessagesMapper;
        this.dlListingRepository = dlListingRepository;
        this.userService = userService;
    }

    public DlMessageDTO save(final DlMessageDTO dlMessageDTO, final Optional<User> sender) {
        log.debug("Request to save DlMessageDTO : {}", dlMessageDTO);

        final DlMessage dlMessage = new DlMessage();
        dlMessagesMapper.createMessage(dlMessage, dlMessageDTO, sender);

        final DlMessage savedDlMessage = dlMessagesRepository.save(dlMessage);

        sender.ifPresent(user -> {
            final DlMessageOverview messageBySenderOrReceiver = dlMessagesOverviewRepository
                    .findOneBySenderOrReceiver(user, dlMessageDTO.getReceiverId());

            if (messageBySenderOrReceiver == null) {
                final DlMessageOverview dlMessageOverview = new DlMessageOverview();
                dlMessagesMapper.createMessage(dlMessageOverview, dlMessageDTO, sender);
                dlMessagesOverviewRepository.save(dlMessageOverview);
            } else {
                messageBySenderOrReceiver.setText(dlMessageDTO.getText());
                dlMessagesOverviewRepository.save(messageBySenderOrReceiver);
            }
        });

        return dlMessagesMapper.messageToDlMessageDTO(savedDlMessage);
    }

    public Page<DlMessageDTO> findAllMessagesByReceiver(final Pageable pageable, final Long receiver) {
        final Page<DlMessageOverview> result = dlMessagesOverviewRepository.findAllMessagesByReceiver(pageable,
                receiver);
        return result.map(dlMessagesMapper::messageToDlMessageDTO);
    }

    public Page<DlMessageDTO> findAllMessagesForListingId(final Pageable pageable, final Long listingId,
            final Long receiver, final Long sender) {
        final Page<DlMessage> result = dlMessagesRepository.findAllMessagesForListingId(pageable, listingId, receiver,
                sender);
        return result.map(dlMessagesMapper::messageToDlMessageDTO);
    }

    @Transactional(readOnly = true)
    public DlMessageDTO findOne(final Long id) {
        log.debug("Request to get DlMessageDTO: {}", id);
        final long start = System.currentTimeMillis();
        final DlMessageOverview result = dlMessagesOverviewRepository.findOneByListingId(id);
        final DlMessageDTO dlMessagesDTO = result != null ? dlMessagesMapper.messageToDlMessageDTO(result) : null;
        log.info("findOne id: {}, took: {} ms", id, System.currentTimeMillis() - start);
        return dlMessagesDTO;
    }

    public void delete(final Long listingId) {
        log.debug("Request to delete DlMessage : {}", listingId);
        log.debug("Request to delete DlMessageOverview : {}", listingId);
        final long start = System.currentTimeMillis();
        final List<DlMessage> dlMessages = dlMessagesRepository.findAllByListingId(listingId);
        final DlMessageOverview dlMessageOverview = dlMessagesOverviewRepository.findOneByListingId(listingId);
        dlMessages.forEach(dlMessage -> {
            dlMessage.setDeleted(new Timestamp(CLOCK.millis()));
            dlMessagesRepository.save(dlMessage);
        });
        dlMessagesOverviewRepository.delete(dlMessageOverview);
        log.info("Deletion of DlMessage and DlMessageOverview: {}, took: {} ms", listingId,
                System.currentTimeMillis() - start);
    }

    public void sendMessageForListing(Long id, DlListingMessageForm form, String currentUserLogin) {
        DlListing dlListing = dlListingRepository.findOne(id);
        User sender = getSender(form, currentUserLogin);
        DlMessage dlMessage = createDlMessage(form, dlListing, sender);
        dlMessagesRepository.save(dlMessage);

        saveMessageOverview(form, dlListing, sender);
    }

    private DlMessage createDlMessage(DlListingMessageForm form, DlListing dlListing, User sender) {
        DlMessage dlMessage = new DlMessage();
        dlMessage.setSender(sender);
        dlMessage.setReceiver(dlListing.getUser().getId());
        dlMessage.setListingId(dlListing.getId());
        dlMessage.setText(form.getText());
        return dlMessage;
    }

    private DlMessageOverview createDlMessageOverview(DlListingMessageForm form, DlListing dlListing, User sender) {
        DlMessageOverview dlMessage = new DlMessageOverview();
        dlMessage.setSender(sender);
        dlMessage.setReceiver(dlListing.getUser().getId());
        dlMessage.setListingId(dlListing.getId());
        dlMessage.setText(form.getText());
        return dlMessage;
    }

    private void saveMessageOverview(DlListingMessageForm form, DlListing dlListing, User sender) {
        final DlMessageOverview messageBySenderOrReceiver = dlMessagesOverviewRepository
                .findOneBySenderOrReceiver(sender, dlListing.getUser().getId());
        if (messageBySenderOrReceiver == null) {
            DlMessageOverview dlMessageOverview = createDlMessageOverview(form, dlListing, sender);
            dlMessagesOverviewRepository.save(dlMessageOverview);
        } else {
            messageBySenderOrReceiver.setText(form.getText());
            dlMessagesOverviewRepository.save(messageBySenderOrReceiver);
        }
    }

    private User getSender(DlListingMessageForm form, String currentUserLogin) {
        if (!StringUtils.isEmpty(currentUserLogin)) {
            Optional<User> sender = userService.findOneByLogin(currentUserLogin);
            return sender.orElseGet(() -> userService.createUserFromMessageForm(form.getSenderName(), form.getSenderEmail()));
        } else {
            // create user from form input
            return userService.createUserFromMessageForm(form.getSenderName(), form.getSenderEmail());
        }
    }

}
