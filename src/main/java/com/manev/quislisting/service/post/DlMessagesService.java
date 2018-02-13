package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.repository.DlMessagesOverviewRepository;
import com.manev.quislisting.repository.DlMessagesRepository;
import com.manev.quislisting.service.mapper.DlMessagesMapper;
import com.manev.quislisting.service.dto.DlMessageDTO;
import java.sql.Timestamp;
import java.time.Clock;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public DlMessagesService(final DlMessagesRepository dlMessagesRepository,
            final DlMessagesOverviewRepository dlMessagesOverviewRepository, final DlMessagesMapper dlMessagesMapper) {
        this.dlMessagesRepository = dlMessagesRepository;
        this.dlMessagesOverviewRepository = dlMessagesOverviewRepository;
        this.dlMessagesMapper = dlMessagesMapper;
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

}
