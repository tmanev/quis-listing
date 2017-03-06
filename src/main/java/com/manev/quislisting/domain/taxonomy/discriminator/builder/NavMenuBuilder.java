package com.manev.quislisting.domain.taxonomy.discriminator.builder;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;

public final class NavMenuBuilder {
    private Long id;
    private NavMenu parent;
    private Term term;
    private Translation translation;
    private String taxonomy;
    private String description;
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

    public NavMenuBuilder withParent(NavMenu parent) {
        this.parent = parent;
        return this;
    }

    public NavMenuBuilder withTerm(Term term) {
        this.term = term;
        return this;
    }

    public NavMenuBuilder withTranslation(Translation translation) {
        this.translation = translation;
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

    public NavMenuBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public NavMenu build() {
        NavMenu navMenu = new NavMenu();
        navMenu.setId(id);
        navMenu.setParent(parent);
        navMenu.setTerm(term);
        navMenu.setTranslation(translation);
        navMenu.setTaxonomy(taxonomy);
        navMenu.setDescription(description);
        navMenu.setCount(count);
        return navMenu;
    }
}
