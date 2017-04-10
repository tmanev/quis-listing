package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.post.AbstractPost;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = QlPage.TYPE)
public class QlPage extends AbstractPost {
    public static final String TYPE = "page";
}
