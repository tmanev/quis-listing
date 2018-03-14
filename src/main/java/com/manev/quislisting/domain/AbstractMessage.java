package com.manev.quislisting.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Abstract entity that holds the common attributes for the messages interface.
 */
@MappedSuperclass
public abstract class AbstractMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false, updatable = false)
    private User sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false, updatable = false)
    private User receiver;

    @NotNull
    @Column
    private String text;

    @NotNull
    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @CreatedDate
    @Column(name = "created", nullable = false)
    private Timestamp created;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(final User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(final Long listingId) {
        this.listingId = listingId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

}
