package com.manev.quislisting.domain;

import com.manev.quislisting.domain.qlml.QlString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ql_dl_content_field_item")
public class DlContentFieldItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dl_content_field_id")
    private DlContentField dlContentField;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "string_id")
    private QlString qlString;

    @NotNull
    @Column
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DlContentFieldItem id(Long id) {
        this.id = id;
        return this;
    }

    public DlContentField getDlContentField() {
        return dlContentField;
    }

    public void setDlContentField(DlContentField dlContentField) {
        this.dlContentField = dlContentField;
    }

    public DlContentFieldItem dlContentField(DlContentField dlContentField) {
        this.dlContentField = dlContentField;
        return this;
    }

    public QlString getQlString() {
        return qlString;
    }

    public void setQlString(QlString qlString) {
        this.qlString = qlString;
    }

    public DlContentFieldItem qlString(QlString qlString) {
        this.qlString = qlString;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DlContentFieldItem value(String value) {
        this.value = value;
        return this;
    }
}
