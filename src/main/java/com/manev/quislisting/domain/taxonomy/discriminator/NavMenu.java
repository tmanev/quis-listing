package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.StaticPageNavMenuRel;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue(value = NavMenu.TAXONOMY)
public class NavMenu extends TermTaxonomy {
    static final String TAXONOMY = "nav-menu";

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "navMenu")
    @OrderBy("menuOrder ASC")
    private Set<StaticPageNavMenuRel> staticPageNavMenuRels;

    public Set<StaticPageNavMenuRel> getStaticPageNavMenuRels() {
        return staticPageNavMenuRels;
    }

    public void setStaticPageNavMenuRels(Set<StaticPageNavMenuRel> staticPageNavMenuRels) {
        this.staticPageNavMenuRels = staticPageNavMenuRels;
    }

}
