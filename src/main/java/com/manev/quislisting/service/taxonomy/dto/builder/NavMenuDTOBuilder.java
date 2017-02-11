package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.TermDTO;

/**
 * Created by tmanev on 2/7/2017.
 */
public final class NavMenuDTOBuilder {
    private Long id;
    private TermDTO term;
    private Long parentId;
    private String description;
    private Long count;
    private String languageId;

    private NavMenuDTOBuilder() {
    }

    public static NavMenuDTOBuilder aNavMenuDTO() {
        return new NavMenuDTOBuilder();
    }

    public NavMenuDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public NavMenuDTOBuilder withTerm(TermDTO term) {
        this.term = term;
        return this;
    }

    public NavMenuDTOBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public NavMenuDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public NavMenuDTOBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public NavMenuDTOBuilder withLanguageId(String languageId) {
        this.languageId = languageId;
        return this;
    }

    public NavMenuDTO build() {
        NavMenuDTO navMenuDTO = new NavMenuDTO();
        navMenuDTO.setId(id);
        navMenuDTO.setTerm(term);
        navMenuDTO.setParentId(parentId);
        navMenuDTO.setDescription(description);
        navMenuDTO.setCount(count);
        navMenuDTO.setLanguageId(languageId);
        return navMenuDTO;
    }
}
