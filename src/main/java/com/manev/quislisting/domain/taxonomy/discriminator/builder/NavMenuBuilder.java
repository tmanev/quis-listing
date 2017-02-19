package com.manev.quislisting.domain.taxonomy.discriminator.builder;

import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;

import java.util.Set;

public final class NavMenuBuilder {
    private Long id;
    private Term term;
    private String taxonomy;
    private String description;
    private Long parentId;
    private Set<TermTaxonomy> children;
    private Long count = 0L;

    private NavMenuBuilder() {
    }

    public static NavMenuBuilder aNavMenu() {
        return new NavMenuBuilder();
    }

    public NavMenuBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public NavMenuBuilder withTerm(Term term) {
        this.term = term;
        return this;
    }

    public NavMenuBuilder withTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
        return this;
    }

    public NavMenuBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public NavMenuBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public NavMenuBuilder withChildren(Set<TermTaxonomy> children) {
        this.children = children;
        return this;
    }

    public NavMenuBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public NavMenu build() {
        NavMenu navMenu = new NavMenu();
        navMenu.setId(id);
        navMenu.setTerm(term);
        navMenu.setTaxonomy(taxonomy);
        navMenu.setDescription(description);
//        navMenu.setParentId(parentId);
        navMenu.setCount(count);
        return navMenu;
    }
}
