package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = DlCategory.TAXONOMY)
public class DlCategory extends TermTaxonomy {
    public static final String TAXONOMY = "dl-category";

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true, updatable = true)
    private DlCategory parent;

    public DlCategory getParent() {
        return parent;
    }

    public void setParent(DlCategory parent) {
        this.parent = parent;
    }

}
