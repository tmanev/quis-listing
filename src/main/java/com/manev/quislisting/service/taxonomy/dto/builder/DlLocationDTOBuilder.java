package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;

public final class DlLocationDTOBuilder {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private String description;
    private Long count;
    private String languageId;
    private DlLocationDTO parent;

    private DlLocationDTOBuilder() {
    }

    public static DlLocationDTOBuilder aDlLocationDTO() {
        return new DlLocationDTOBuilder();
    }

    public DlLocationDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlLocationDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DlLocationDTOBuilder withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public DlLocationDTOBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public DlLocationDTOBuilder withParent(DlLocationDTO parent) {
        this.parent = parent;
        return this;
    }

    public DlLocationDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DlLocationDTOBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public DlLocationDTOBuilder withLanguageId(String languageId) {
        this.languageId = languageId;
        return this;
    }

    public DlLocationDTO build() {
        DlLocationDTO dlLocationDTO = new DlLocationDTO();
        dlLocationDTO.setId(id);
        dlLocationDTO.setName(name);
        dlLocationDTO.setSlug(slug);
        dlLocationDTO.setParentId(parentId);
        dlLocationDTO.setParent(parent);
        dlLocationDTO.setDescription(description);
        dlLocationDTO.setCount(count);
        dlLocationDTO.setLanguageCode(languageId);
        return dlLocationDTO;
    }
}
