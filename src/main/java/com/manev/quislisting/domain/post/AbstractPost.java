package com.manev.quislisting.domain.post;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "ql_post")
@Inheritance
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class AbstractPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @NotNull
    @Column
    private String name;

    @Column
    private String content;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "translation_id")
    private Translation translation;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @Column
    private ZonedDateTime created;

    @Column
    private ZonedDateTime modified;

    @Column
    private Long commentCount = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abstractPost", fetch = FetchType.EAGER)
    private Set<PostMeta> postMeta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public void setModified(ZonedDateTime modified) {
        this.modified = modified;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<PostMeta> getPostMeta() {
        return postMeta;
    }

    public void setPostMeta(Set<PostMeta> postMeta) {
        this.postMeta = postMeta;
    }

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

    public void addPostMeta(PostMeta newPostMeta) {
        if (this.postMeta == null) {
            this.postMeta = new HashSet<>();
        }
        PostMeta postMetaExists = findPostMetaByKey(newPostMeta.getKey());
        if (postMetaExists != null) {
            postMetaExists.setValue(newPostMeta.getValue());
        } else {
            this.postMeta.add(newPostMeta);
        }
    }

    private PostMeta findPostMetaByKey(String key) {
        for (PostMeta meta : postMeta) {
            if (meta.getKey().equals(key)) {
                return meta;
            }
        }
        return null;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }
}
