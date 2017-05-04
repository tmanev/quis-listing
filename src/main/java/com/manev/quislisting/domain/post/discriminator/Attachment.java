package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.post.AbstractPost;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = Attachment.TYPE)
public class Attachment extends AbstractPost {

    public static final String TYPE = "attachment";
    public static final String QL_ATTACHED_FILE = "ql-attached-file";
    public static final String QL_ATTACHMENT_METADATA = "ql-attachment-metadata";
    private String mimeType;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        BY_ADMIN,
        TEMP;
    }
}
