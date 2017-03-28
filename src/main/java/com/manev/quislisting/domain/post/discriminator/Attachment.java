package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.post.AbstractPost;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = Attachment.TYPE)
public class Attachment extends AbstractPost {

    public static final String TYPE = "attachment";
    public static final String QL_ATTACHED_FILE = "ql-attached-file";
    public static final String QL_ATTACHMENT_METADATA = "ql-attachment-metadata";
    private String mimeType;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public enum Status {
        BY_ADMIN("by-admin"),
        TEMP("temp");

        private final String value;

        Status(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
