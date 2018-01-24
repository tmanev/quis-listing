package com.manev.quislisting.domain.taxonomy;

import com.manev.quislisting.domain.Translation;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ql_term_taxonomy")
@Inheritance
@DiscriminatorColumn(name = "taxonomy", discriminatorType = DiscriminatorType.STRING, length = 32)
public abstract class TermTaxonomy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String slug;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "translation_id", nullable = false, updatable = false)
    private Translation translation;

    @Column(name = "taxonomy", insertable = false, updatable = false)
    private String taxonomy;

    @NotNull
    @Column
    private String description;

    @Column
    private Long count = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }
}
