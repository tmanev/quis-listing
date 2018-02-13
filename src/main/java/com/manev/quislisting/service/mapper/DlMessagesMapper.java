package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.AbstractMessage;
import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.DlMessageDTO;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Custom mapper for converting instances of {@link AbstractMessage} to {@DlMessageDTO}.
 */
@Component
public class DlMessagesMapper {

    private static final String EMPTY_SEPARATOR = " ";
    private static final String DATE_PATTERN = "dd/MM/yyyy";

    private final UserService userService;

    public DlMessagesMapper(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Method that converts {@link AbstractMessage} to {@DlMessageDTO}.
     *
     * @param message {@link DlMessage} object that needs to be converted
     * @return {@link DlMessageDTO} converted object
     */
    public DlMessageDTO messageToDlMessageDTO(final AbstractMessage message) {
        final DlMessageDTO dlMessageDTO = new DlMessageDTO();
        dlMessageDTO.setId(message.getId());
        dlMessageDTO.setReceiverId(message.getReceiver());
        dlMessageDTO.setSenderId(message.getSender().getId());
        dlMessageDTO.setSenderName(message.getSender().getFirstName() + EMPTY_SEPARATOR +
                message.getSender().getLastName());
        dlMessageDTO.setSenderEmail(message.getSender().getLogin());
        dlMessageDTO.setText(message.getText());
        dlMessageDTO.setListingId(message.getListingId());
        dlMessageDTO.setCreatedDate(parseCreatedDate(message.getCreatedDate()));

        return dlMessageDTO;
    }

    public void createMessage(final AbstractMessage message, final DlMessageDTO dlMessageDTO,
            final Optional<User> sender) {
        final Optional<User> user = getSender(sender, dlMessageDTO);
        user.ifPresent(message::setSender);

        message.setReceiver(dlMessageDTO.getReceiverId());
        message.setText(dlMessageDTO.getText());
        message.setListingId(dlMessageDTO.getListingId());
    }

    public DlMessageDTO prepareMessageDTOForFrontend(final DlMessageDTO dlMessageDTO) {
        final DlMessageDTO messageDTO = new DlMessageDTO();
        messageDTO.setListingId(dlMessageDTO.getListingId());
        messageDTO.setReceiverId(dlMessageDTO.getReceiverId());
        messageDTO.setSenderId(dlMessageDTO.getSenderId());
        messageDTO.setSenderName(dlMessageDTO.getSenderName());
        messageDTO.setSenderEmail(dlMessageDTO.getSenderEmail());

        return dlMessageDTO;
    }

    private String parseCreatedDate(final ZonedDateTime createdDate) {
        return DateTimeFormatter.ofPattern(DATE_PATTERN).format(createdDate);
    }

    private Optional<User> getSender(final Optional<User> sender, final DlMessageDTO dlMessageDTO) {
        if (dlMessageDTO.getSenderId() == null) {
            if (sender.isPresent()) {
                dlMessageDTO.setSenderId(sender.get().getId());
                dlMessageDTO.setSenderName(sender.get().getFirstName() + EMPTY_SEPARATOR + sender.get().getLastName());
                return sender;
            }
        } else {
            return userService.findOneByLogin(dlMessageDTO.getSenderEmail());
        }

        return Optional.empty();
    }
}
