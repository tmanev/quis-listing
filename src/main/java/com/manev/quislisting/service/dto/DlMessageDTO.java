package com.manev.quislisting.service.dto;

import com.manev.quislisting.service.model.UserBaseModel;

import java.sql.Timestamp;

/**
 * DTO object for the messages interface.
 */
public class DlMessageDTO {

    private Long id;
    private UserBaseModel sender;
    private UserBaseModel receiver;
    private Long listingId;
    private String text;
    private Timestamp created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserBaseModel getSender() {
        return sender;
    }

    public void setSender(UserBaseModel sender) {
        this.sender = sender;
    }

    public UserBaseModel getReceiver() {
        return receiver;
    }

    public void setReceiver(UserBaseModel receiver) {
        this.receiver = receiver;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
