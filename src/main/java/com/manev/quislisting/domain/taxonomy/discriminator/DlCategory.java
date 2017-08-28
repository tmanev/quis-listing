package com.manev.quislisting.domain.taxonomy.discriminator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue(value = DlCategory.TAXONOMY)
public class DlCategory extends TermTaxonomy {
    public static final String TAXONOMY = "dl-category";

    @JsonBackReference(value = "dl_category_parent_reference")
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DlCategory parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DlCategory> children;

    @JsonBackReference(value = "dl_category_listing_reference")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy="dlCategories")
    private Set<DlListing> dlListings;

    public DlCategory getParent() {
        return parent;
    }

    public void setParent(DlCategory parent) {
        this.parent = parent;
    }

    public Set<DlCategory> getChildren() {
        return children;
    }

    public void setChildren(Set<DlCategory> children) {
        this.children = children;
    }

    public Set<DlListing> getDlListings() {
        return dlListings;
    }

    public void setDlListings(Set<DlListing> dlListings) {
        this.dlListings = dlListings;
    }

}
