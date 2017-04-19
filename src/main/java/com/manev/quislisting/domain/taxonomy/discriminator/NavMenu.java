package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.post.discriminator.NavMenuItem;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue(value = NavMenu.TAXONOMY)
public class NavMenu extends TermTaxonomy {
    public static final String TAXONOMY = "nav-menu";

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ql_term_relationship",
            joinColumns =
            @JoinColumn(name = "term_taxonomy_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "object_id", referencedColumnName = "id"))
    @OrderBy("postOrder ASC")
    private Set<NavMenuItem> navMenuItems;

    public Set<NavMenuItem> getNavMenuItems() {
        return navMenuItems;
    }

    public void setNavMenuItems(Set<NavMenuItem> navMenuItems) {
        this.navMenuItems = navMenuItems;
    }
}
