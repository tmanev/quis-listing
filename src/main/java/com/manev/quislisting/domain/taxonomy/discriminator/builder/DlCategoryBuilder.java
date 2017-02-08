package com.manev.quislisting.domain.taxonomy.discriminator.builder;

import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;

import java.util.Set;

public final class DlCategoryBuilder {
    private Long id;
    private Term term;
    private String taxonomy;
    private String description;
    private Long parentId;
    private Set<TermTaxonomy> children;
    private Long count = 0L;

    private DlCategoryBuilder() {
    }

    public static DlCategoryBuilder aDlCategory() {
        return new DlCategoryBuilder();
    }

    public DlCategoryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlCategoryBuilder withTerm(Term term) {
        this.term = term;
        return this;
    }

    public DlCategoryBuilder withTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
        return this;
    }

    public DlCategoryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DlCategoryBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public DlCategoryBuilder withChildren(Set<TermTaxonomy> children) {
        this.children = children;
        return this;
    }

    public DlCategoryBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public DlCategory build() {
        DlCategory dlCategory = new DlCategory();
        dlCategory.setId(id);
        dlCategory.setTerm(term);
        dlCategory.setTaxonomy(taxonomy);
        dlCategory.setDescription(description);
        dlCategory.setParentId(parentId);
        dlCategory.setChildren(children);
        dlCategory.setCount(count);
        return dlCategory;
    }
}
