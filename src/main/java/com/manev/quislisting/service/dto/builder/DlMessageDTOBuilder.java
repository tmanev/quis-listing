package com.manev.quislisting.service.dto.builder;

import com.manev.quislisting.service.dto.DlMessageDTO;

/**
 * Builder class for {@link DlMessageDTO}.
 */
public class DlMessageDTOBuilder {

    private Long id;
    private Long senderId;
    private String senderName;
    private String senderEmail;
    private Long receiverId;
    private String text;
    private Long listingId;
    private String createdDate;

    private DlMessageDTOBuilder() {

    }

    public static DlMessageDTOBuilder dlMessageDTO() {
        return new DlMessageDTOBuilder();
    }

    public DlMessageDTOBuilder withSenderId(final Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public DlMessageDTOBuilder withSenderName(final String senderName) {
        this.senderName = senderName;
        return this;
    }

    public DlMessageDTOBuilder withSenderEmail(final String senderEmail) {
        this.senderEmail = senderEmail;
        return this;
    }

    public DlMessageDTOBuilder withReceiverId(final Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public DlMessageDTOBuilder withText(final String text) {
        this.text = text;
        return this;
    }

    public DlMessageDTOBuilder withListingId(final Long listingId) {
        this.listingId = listingId;
        return this;
    }

    public DlMessageDTOBuilder withCreatedDate(final String createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public DlMessageDTO build() {
        final DlMessageDTO dlMessageDTO = new DlMessageDTO();
        dlMessageDTO.setId(id);
        dlMessageDTO.setSenderId(senderId);
        dlMessageDTO.setSenderName(senderName);
        dlMessageDTO.setSenderEmail(senderEmail);
        dlMessageDTO.setReceiverId(receiverId);
        dlMessageDTO.setText(text);
        dlMessageDTO.setListingId(listingId);
        dlMessageDTO.setCreatedDate(createdDate);

        return dlMessageDTO;
    }
}
