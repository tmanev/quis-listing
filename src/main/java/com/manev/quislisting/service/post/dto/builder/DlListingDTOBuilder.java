package com.manev.quislisting.service.post.dto.builder;

import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.PostUser;

import java.time.ZonedDateTime;

public final class DlListingDTOBuilder {
    private Long id;
    private String title;
    private String content;
    private String name;
    private String status;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private Long commentCount = 0L;
    private PostUser user;

    private DlListingDTOBuilder() {
    }

    public static DlListingDTOBuilder aDlListingDTO() {
        return new DlListingDTOBuilder();
    }

    public DlListingDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlListingDTOBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public DlListingDTOBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public DlListingDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DlListingDTOBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public DlListingDTOBuilder withCreated(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public DlListingDTOBuilder withModified(ZonedDateTime modified) {
        this.modified = modified;
        return this;
    }

    public DlListingDTOBuilder withCommentCount(Long commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public DlListingDTOBuilder withUser(PostUser user) {
        this.user = user;
        return this;
    }

    public DlListingDTO build() {
        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setId(id);
        dlListingDTO.setTitle(title);
        dlListingDTO.setContent(content);
        dlListingDTO.setName(name);
        dlListingDTO.setStatus(status);
        dlListingDTO.setCreated(created);
        dlListingDTO.setModified(modified);
        dlListingDTO.setCommentCount(commentCount);
        dlListingDTO.setUser(user);
        return dlListingDTO;
    }
}
