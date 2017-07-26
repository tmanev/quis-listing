package com.manev.quislisting.domain.post.discriminator.builder;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.post.discriminator.DlListing;

import java.time.ZonedDateTime;

public final class DlListingBuilder {
    private Long id;
    private String title;
    private String content;
    private String name;
    private DlListing.Status status;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private Translation translation;

    private DlListingBuilder() {
    }

    public static DlListingBuilder aDlListing() {
        return new DlListingBuilder();
    }

    public DlListingBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlListingBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public DlListingBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public DlListingBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DlListingBuilder withStatus(DlListing.Status status) {
        this.status = status;
        return this;
    }

    public DlListingBuilder withCreated(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public DlListingBuilder withModified(ZonedDateTime modified) {
        this.modified = modified;
        return this;
    }

    public DlListingBuilder withTranslation(Translation translation) {
        this.translation = translation;
        return this;
    }

    public DlListing build() {
        DlListing dlListing = new DlListing();
        dlListing.setId(id);
        dlListing.setTitle(title);
        dlListing.setContent(content);
        dlListing.setName(name);
        dlListing.setStatus(status);
        dlListing.setCreated(created);
        dlListing.setModified(modified);
        dlListing.setTranslation(translation);
        return dlListing;
    }
}
