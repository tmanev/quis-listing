package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.post.AbstractPost;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = QlPage.TYPE)
public class QlPage extends AbstractPost {
    public static final String TYPE = "page";

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        PUBLISH,
        DRAFT
    }
}
