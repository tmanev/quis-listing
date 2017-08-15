package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.NavMenuItem;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue(value = NavMenu.TAXONOMY)
public class NavMenu extends TermTaxonomy {
    static final String TAXONOMY = "nav-menu";

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "navMenu")
    @OrderBy("menuOrder ASC")
    private Set<NavMenuItem> navMenuItems;

    public Set<NavMenuItem> getNavMenuItems() {
        return navMenuItems;
    }

    public void setNavMenuItems(Set<NavMenuItem> navMenuItems) {
        this.navMenuItems = navMenuItems;
    }

}
