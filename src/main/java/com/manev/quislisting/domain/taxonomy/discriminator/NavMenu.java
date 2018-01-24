package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.NavMenuItem;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.Set;

@Entity
@DiscriminatorValue(value = NavMenu.TAXONOMY)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
