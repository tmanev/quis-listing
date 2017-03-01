package com.manev.quislisting.domain.qlml;

import javax.persistence.*;

@Entity
@Table(name = "ql_language")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String code;

    @Column
    private String englishName;

    @Column
    private String defaultLocale;

    @Column
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Language code(String code) {
        this.code = code;
        return this;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Language englishName(String englishName) {
        this.englishName = englishName;
        return this;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public Language defaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Language active(Boolean active) {
        this.active = active;
        return this;
    }
}
