package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = NavMenu.TAXONOMY)
public class NavMenu extends TermTaxonomy {
    public static final String TAXONOMY = "nav-menu";

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true, updatable = true)
    private NavMenu parent;

    public NavMenu getParent() {
        return parent;
    }

    public void setParent(NavMenu parent) {
        this.parent = parent;
    }
}
