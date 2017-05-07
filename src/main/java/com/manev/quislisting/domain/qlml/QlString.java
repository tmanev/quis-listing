package com.manev.quislisting.domain.qlml;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ql_string")
public class QlString {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String languageCode;

    @Column
    private String context;

    @Column
    private String name;

    @Column
    private String value;

    @Column
    private Integer status;

    @OneToMany(mappedBy = "qlString", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<StringTranslation> stringTranslation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public QlString languageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public QlString context(String context) {
        this.context = context;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QlString name(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public QlString value(String value) {
        this.value = value;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public QlString status(Integer status) {
        this.status = status;
        return this;
    }

    public Set<StringTranslation> getStringTranslation() {
        return stringTranslation;
    }

    public void setStringTranslation(Set<StringTranslation> stringTranslation) {
        this.stringTranslation = stringTranslation;
    }

    public QlString stringTranslation(Set<StringTranslation> stringTranslation) {
        this.stringTranslation = stringTranslation;
        return this;
    }

    public void addValue() {
    }
}
