package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue(value = DlListing.TYPE)
public class DlListing extends AbstractPost {
    public static final String TYPE = "dl-listing";

    @ManyToMany
    @JoinTable(name = "ql_term_relationship",
            joinColumns=
            @JoinColumn(name="term_taxonomy_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="object_id", referencedColumnName="id"))
    private Set<DlCategory> dlCategories;

    public Set<DlCategory> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(Set<DlCategory> dlCategories) {
        this.dlCategories = dlCategories;
    }
}
