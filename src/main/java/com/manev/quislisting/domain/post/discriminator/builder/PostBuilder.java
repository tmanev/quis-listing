package com.manev.quislisting.domain.post.discriminator.builder;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.Post;

import java.time.ZonedDateTime;

public final class PostBuilder {
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

    private PostBuilder() {
    }

    public static PostBuilder aPost() {
        return new PostBuilder();
    }

    public PostBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PostBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PostBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public PostBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PostBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public PostBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public PostBuilder withCreated(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public PostBuilder withModified(ZonedDateTime modified) {
        this.modified = modified;
        return this;
    }

    public PostBuilder withCommentCount(Long commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public PostBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Post build() {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setContent(content);
        post.setName(name);
        post.setType(type);
        post.setStatus(status);
        post.setCreated(created);
        post.setModified(modified);
        post.setCommentCount(commentCount);
        post.setUser(user);
        return post;
    }
}
