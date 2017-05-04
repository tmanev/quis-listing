package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.domain.post.discriminator.QlPage;

import java.time.ZonedDateTime;

public class QlPageDTO {

    private Long id;
    private String title;
    private String content;
    private String name;
    private String expirationDate;
    private QlPage.Status status;
    private String views;
    private Author author;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private String languageCode;
    private Long trGroupId;

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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public QlPage.Status getStatus() {
        return status;
    }

    public void setStatus(QlPage.Status status) {
        this.status = status;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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


    public Long getTrGroupId() {
        return trGroupId;
    }

    public void setTrGroupId(Long trGroupId) {
        this.trGroupId = trGroupId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
