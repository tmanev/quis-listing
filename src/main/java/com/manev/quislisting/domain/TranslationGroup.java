package com.manev.quislisting.domain;

import javax.persistence.*;

@Entity
@Table(name = "ql_translation_group")
public class TranslationGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
}
