package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = DlLocation.TAXONOMY)
public class DlLocation extends TermTaxonomy {
    public static final String TAXONOMY = "dl-location";

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DlLocation parent;

    public DlLocation getParent() {
        return parent;
    }

    public void setParent(DlLocation parent) {
        this.parent = parent;
    }
}
