package com.manev.quislisting.domain.taxonomy.discriminator;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue(value = NavMenu.TAXONOMY)
public class NavMenu extends TermTaxonomy {
    public static final String TAXONOMY = "nav-menu";

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true, updatable = true)
    private NavMenu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DlCategory> children;

    public NavMenu getParent() {
        return parent;
    }

    public void setParent(NavMenu parent) {
        this.parent = parent;
    }

    public Set<DlCategory> getChildren() {
        return children;
    }

    public void setChildren(Set<DlCategory> children) {
        this.children = children;
    }
}
