package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.PostMeta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Optional;

@Entity
@DiscriminatorValue(value = DlListing.TYPE)
public class DlListing extends AbstractPost {
    public static final String TYPE = "dl-listing";

    public String getPostMetaValue(String key) {
        if (this.getPostMeta() != null) {
            Optional<PostMeta> first = this.getPostMeta().stream().filter(p -> key.equals(p.getKey()))
                    .findFirst();
            if (first.isPresent()) {
                return first.get().getValue();
            }
        }

        return null;
    }
}
