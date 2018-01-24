package com.manev.quislisting.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "ql_translation_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TranslationGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference(value = "translation_group_translation_reference")
    @OneToMany(cascade = ALL, mappedBy = "translationGroup")
    private Set<Translation> translations;

    public TranslationGroup() {
        // default constructor
    }

    public TranslationGroup(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<Translation> translations) {
        this.translations = translations;
    }
}
