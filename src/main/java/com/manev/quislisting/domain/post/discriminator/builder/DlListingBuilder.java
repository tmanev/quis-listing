package com.manev.quislisting.domain.post.discriminator.builder;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;

import java.time.ZonedDateTime;

/**
 * Created by tmanev on 2/8/2017.
 */
public final class DlListingBuilder {
    private Long id;
    private String title;
    private String content;
    private String name;
    private String type;
    private String status;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private Long commentCount = 0L;
    private User user;

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

    public DlListingBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public DlListingBuilder withStatus(String status) {
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

    public DlListingBuilder withCommentCount(Long commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public DlListingBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public DlListing build() {
        DlListing dlListing = new DlListing();
        dlListing.setId(id);
        dlListing.setTitle(title);
        dlListing.setContent(content);
        dlListing.setName(name);
        dlListing.setType(type);
        dlListing.setStatus(status);
        dlListing.setCreated(created);
        dlListing.setModified(modified);
        dlListing.setCommentCount(commentCount);
        dlListing.setUser(user);
        return dlListing;
    }
}
