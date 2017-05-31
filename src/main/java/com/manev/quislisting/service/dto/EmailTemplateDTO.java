package com.manev.quislisting.service.dto;

import com.manev.quislisting.domain.qlml.QlString;

/**
 * Created by adri on 4/5/2017.
 */
public class EmailTemplateDTO {
    private Long id;
    private String name;
    private String text;

    private QlString qlString;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QlString getQlString() {
        return qlString;
    }

    public void setQlString(QlString qlString) {
        this.qlString = qlString;
    }
}
