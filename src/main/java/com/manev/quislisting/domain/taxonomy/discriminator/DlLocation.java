package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = DlLocation.TAXONOMY)
public class DlLocation extends TermTaxonomy {
    public static final String TAXONOMY = "dl-location";
}
