package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.domain.post.discriminator.QlPage;

public class QlPageDTO extends AbstractPostDTO {

    private QlPage.Status status;
    private String views;

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

}
