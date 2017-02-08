package com.manev.quislisting.domain.taxonomy.builder;

import com.manev.quislisting.domain.taxonomy.Term;

public final class TermBuilder {
    private Long id;
    private String name;
    private String slug;

    private TermBuilder() {
    }

    public static TermBuilder aTerm() {
        return new TermBuilder();
    }

    public TermBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TermBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TermBuilder withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public Term build() {
        Term term = new Term();
        term.setId(id);
        term.setName(name);
        term.setSlug(slug);
        return term;
    }
}
