package com.manev.quislisting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Entity for storing messages between users.
 */
@Entity
@Table(name = "ql_dl_message_overview")
public class DlMessageOverview extends AbstractMessage {

    @Column
    private Timestamp deleted;

    @Column
    private Timestamp isRead;

    public Timestamp getDeleted() {
        return deleted;
    }

    public void setDeleted(final Timestamp deleted) {
        this.deleted = deleted;
    }

    public Timestamp getIsRead() {
        return isRead;
    }

    public void setIsRead(Timestamp isRead) {
        this.isRead = isRead;
    }
}
