package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = PostCategory.TAXONOMY)
public class PostCategory extends TermTaxonomy {
    public static final String TAXONOMY = "post-category";

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true, updatable = true)
    private PostCategory parent;

    public PostCategory getParent() {
        return parent;
    }

    public void setParent(PostCategory parent) {
        this.parent = parent;
    }

}
