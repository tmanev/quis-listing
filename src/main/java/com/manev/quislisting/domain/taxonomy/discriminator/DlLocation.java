package com.manev.quislisting.domain.taxonomy.discriminator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DiscriminatorValue(value = DlLocation.TAXONOMY)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DlLocation extends TermTaxonomy {
    public static final String TAXONOMY = "dl-location";

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DlLocation parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DlLocation> children;

    public DlLocation getParent() {
        return parent;
    }

    public void setParent(DlLocation parent) {
        this.parent = parent;
    }

    public Set<DlLocation> getChildren() {
        return children;
    }

    public void setChildren(Set<DlLocation> children) {
        this.children = children;
    }

}
