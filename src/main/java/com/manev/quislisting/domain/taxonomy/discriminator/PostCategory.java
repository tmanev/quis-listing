package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = PostCategory.TAXONOMY)
public class PostCategory extends TermTaxonomy {
    public static final String TAXONOMY = "post-category";

}
