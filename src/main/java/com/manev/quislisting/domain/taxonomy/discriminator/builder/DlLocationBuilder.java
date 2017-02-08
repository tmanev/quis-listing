package com.manev.quislisting.domain.taxonomy.discriminator.builder;

import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;

import java.util.Set;

public final class DlLocationBuilder {
    private Long id;
    private Term term;
    private String taxonomy;
    private String description;
    private Long parentId;
    private Set<TermTaxonomy> children;
    private Long count = 0L;

    private DlLocationBuilder() {
    }

    public static DlLocationBuilder aDlLocation() {
        return new DlLocationBuilder();
    }

    public DlLocationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlLocationBuilder withTerm(Term term) {
        this.term = term;
        return this;
    }

    public DlLocationBuilder withTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
        return this;
    }

    public DlLocationBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DlLocationBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public DlLocationBuilder withChildren(Set<TermTaxonomy> children) {
        this.children = children;
        return this;
    }

    public DlLocationBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public DlLocation build() {
        DlLocation dlLocation = new DlLocation();
        dlLocation.setId(id);
        dlLocation.setTerm(term);
        dlLocation.setTaxonomy(taxonomy);
        dlLocation.setDescription(description);
        dlLocation.setParentId(parentId);
        dlLocation.setChildren(children);
        dlLocation.setCount(count);
        return dlLocation;
    }
}
