package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = NavMenu.TAXONOMY)
public class NavMenu extends TermTaxonomy {
    public static final String TAXONOMY = "nav-menu";
}
