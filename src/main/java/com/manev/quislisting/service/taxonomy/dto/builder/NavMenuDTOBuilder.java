package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.StaticPageNavMenuDTO;

import java.util.List;

public final class NavMenuDTOBuilder {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private String description;
    private Long count;
    private String languageCode;
    private List<StaticPageNavMenuDTO> staticPageNavMenuDTOList;
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

    public NavMenuDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public NavMenuDTOBuilder withSlug(String slug) {
        this.slug = slug;
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

    public NavMenuDTOBuilder withNavMenuItemDTOList(List<StaticPageNavMenuDTO> staticPageNavMenuDTOList) {
        this.staticPageNavMenuDTOList = staticPageNavMenuDTOList;
        return this;
    }

    public NavMenuDTO build() {
        NavMenuDTO navMenuDTO = new NavMenuDTO();
        navMenuDTO.setId(id);
        navMenuDTO.setName(name);
        navMenuDTO.setSlug(slug);
        navMenuDTO.setParentId(parentId);
        navMenuDTO.setDescription(description);
        navMenuDTO.setCount(count);
        navMenuDTO.setLanguageCode(languageCode);
        navMenuDTO.setStaticPageNavMenuDTOS(staticPageNavMenuDTOList);
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
