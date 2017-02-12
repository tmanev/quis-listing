package com.manev.quislisting.domain.post;

import javax.persistence.*;

@Entity
@Table(name = "ql_postmeta")
public class PostMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false, updatable=false)
    private AbstractPost abstractPost;

    @Column
    private String key;

    @Column
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbstractPost getAbstractPost() {
        return abstractPost;
    }

    public void setAbstractPost(AbstractPost abstractPost) {
        this.abstractPost = abstractPost;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
