package com.manev.quislisting.domain;

import javax.persistence.*;

@Entity
@Table(name = "ql_translation")
public class Translation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tr_group_id")
    private TranslationGroup translationGroup;

    @Column
    private String languageCode;

    @Column
    private String sourceLanguageCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TranslationGroup getTranslationGroup() {
        return translationGroup;
    }

    public void setTranslationGroup(TranslationGroup translationGroup) {
        this.translationGroup = translationGroup;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }
}
