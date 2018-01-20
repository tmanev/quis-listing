package com.manev.quislisting.domain.qlml;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ql_string")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QlString {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column
    private String languageCode;

    @NotNull
    @Column
    private String context;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String value;

    @NotNull
    @Column
    private Integer status;

    @OneToMany(mappedBy = "qlString", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    public void addStringTranslation(StringTranslation stringTranslation) {
        if (this.stringTranslation == null) {
            this.stringTranslation = new HashSet<>();
        }
        this.stringTranslation.add(stringTranslation);
    }
}
