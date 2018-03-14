package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.AbstractMessage;
import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.service.dto.DlMessageDTO;
import org.springframework.stereotype.Component;

/**
 * Custom mapper for converting instances of {@link AbstractMessage} to {@link DlMessageDTO}.
 */
@Component
public class DlMessagesMapper {

    private UserBaseModelMapper userBaseModelMapper;

    public DlMessagesMapper(UserBaseModelMapper userBaseModelMapper) {
        this.userBaseModelMapper = userBaseModelMapper;
    }

    /**
     * Method that converts {@link AbstractMessage} to {@link DlMessageDTO}.
     *
     * @param message {@link DlMessage} object that needs to be converted
     * @return {@link DlMessageDTO} converted object
     */
    public DlMessageDTO messageToDlMessageDTO(final AbstractMessage message) {
        final DlMessageDTO dlMessageDTO = new DlMessageDTO();
        dlMessageDTO.setId(message.getId());
        dlMessageDTO.setReceiver(userBaseModelMapper.map(message.getReceiver()));
        dlMessageDTO.setSender(userBaseModelMapper.map(message.getSender()));
        dlMessageDTO.setText(message.getText());
        dlMessageDTO.setListingId(message.getListingId());
        dlMessageDTO.setCreated(message.getCreated());

        return dlMessageDTO;
    }

    public DlMessageOverview createMessageOverview(DlMessage dlMessage) {
        DlMessageOverview dlMessageOverview = new DlMessageOverview();
        dlMessageOverview.setListingId(dlMessage.getListingId());
        dlMessageOverview.setSender(dlMessage.getSender());
        dlMessageOverview.setReceiver(dlMessage.getReceiver());
        dlMessageOverview.setText(dlMessage.getText());
        dlMessageOverview.setCreated(dlMessage.getCreated());
        dlMessageOverview.setIsRead(null);
        dlMessageOverview.setDeleted(null);

        return dlMessageOverview;
    }

}
