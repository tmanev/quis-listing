package com.manev.quislisting.domain.taxonomy.discriminator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue(value = DlCategory.TAXONOMY)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DlCategory extends TermTaxonomy {
    public static final String TAXONOMY = "dl-category";

    @JsonBackReference(value = "dl_category_parent_reference")
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DlCategory parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DlCategory> children;

    @JsonBackReference(value = "dl_category_listing_reference")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy="dlCategories")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    public void addChildren(DlCategory dlCategory) {
        if (children == null) {
            children = new HashSet<>();
        }
        children.add(dlCategory);
    }

    public Set<DlListing> getDlListings() {
        return dlListings;
    }

    public void setDlListings(Set<DlListing> dlListings) {
        this.dlListings = dlListings;
    }

}
