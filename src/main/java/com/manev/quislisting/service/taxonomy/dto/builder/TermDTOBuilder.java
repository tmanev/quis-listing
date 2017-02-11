package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.taxonomy.dto.TermDTO;

public final class TermDTOBuilder {
    private Long id;
    private String name;
    private String slug;

    private TermDTOBuilder() {
    }

    public static TermDTOBuilder aTerm() {
        return new TermDTOBuilder();
    }

    public TermDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TermDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TermDTOBuilder withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public TermDTO build() {
        TermDTO termDTO = new TermDTO();
        termDTO.setId(id);
        termDTO.setName(name);
        termDTO.setSlug(slug);
        return termDTO;
    }
}
