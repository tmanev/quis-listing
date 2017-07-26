package com.manev.quislisting.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ql_static_page")
public class StaticPage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column
    private String title;

    @NotNull
    @Column
    private String name;

    @Column
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private StaticPage.Status status;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "translation_id")
    private Translation translation;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StaticPage.Status getStatus() {
        return status;
    }

    public void setStatus(StaticPage.Status status) {
        this.status = status;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public enum Status {
        DRAFT,
        PUBLISH
    }
}
