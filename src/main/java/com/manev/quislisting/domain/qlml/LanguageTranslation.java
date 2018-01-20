package com.manev.quislisting.domain.qlml;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ql_language_translation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LanguageTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String languageCode;

    @Column
    private String displayLanguageCode;

    @Column
    private String name;

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

    public String getDisplayLanguageCode() {
        return displayLanguageCode;
    }

    public void setDisplayLanguageCode(String displayLanguageCode) {
        this.displayLanguageCode = displayLanguageCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
