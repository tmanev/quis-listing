package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.NavMenuItemDTO;
import com.manev.quislisting.service.taxonomy.dto.TermDTO;

import java.util.List;

public final class NavMenuDTOBuilder {
    private Long id;
    private TermDTO term;
    private Long parentId;
    private String description;
    private Long count;
    private String languageCode;
    private List<NavMenuItemDTO> navMenuItemDTOList;
    private String sourceLanguageCode;
    private Long translationGroupId;

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

    public NavMenuDTOBuilder withLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public NavMenuDTOBuilder withNavMenuItemDTOList(List<NavMenuItemDTO> navMenuItemDTOList) {
        this.navMenuItemDTOList = navMenuItemDTOList;
        return this;
    }

    public NavMenuDTO build() {
        NavMenuDTO navMenuDTO = new NavMenuDTO();
        navMenuDTO.setId(id);
        navMenuDTO.setTerm(term);
        navMenuDTO.setParentId(parentId);
        navMenuDTO.setDescription(description);
        navMenuDTO.setCount(count);
        navMenuDTO.setLanguageCode(languageCode);
        navMenuDTO.setNavMenuItemDTOs(navMenuItemDTOList);
        navMenuDTO.setSourceLanguageCode(sourceLanguageCode);
        navMenuDTO.setTranslationGroupId(translationGroupId);
        return navMenuDTO;
    }

    public NavMenuDTOBuilder withSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
        return this;
    }

    public NavMenuDTOBuilder withTranslationGroupId(Long translationGroupId) {
        this.translationGroupId = translationGroupId;
        return this;
    }
}
