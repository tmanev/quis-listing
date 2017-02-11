package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.TermDTO;

/**
 * Created by tmanev on 2/7/2017.
 */
public final class DlCategoryDTOBuilder {
    private Long id;
    private TermDTO term;
    private Long parentId;
    private String description;
    private Long count;
    private String languageId;

    private DlCategoryDTOBuilder() {
    }

    public static DlCategoryDTOBuilder aDlCategoryDTO() {
        return new DlCategoryDTOBuilder();
    }

    public DlCategoryDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlCategoryDTOBuilder withTerm(TermDTO term) {
        this.term = term;
        return this;
    }

    public DlCategoryDTOBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public DlCategoryDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DlCategoryDTOBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public DlCategoryDTOBuilder withLanguageId(String languageId) {
        this.languageId = languageId;
        return this;
    }

    public DlCategoryDTO build() {
        DlCategoryDTO dlCategoryDTO = new DlCategoryDTO();
        dlCategoryDTO.setId(id);
        dlCategoryDTO.setTerm(term);
        dlCategoryDTO.setParentId(parentId);
        dlCategoryDTO.setDescription(description);
        dlCategoryDTO.setCount(count);
        dlCategoryDTO.setLanguageId(languageId);
        return dlCategoryDTO;
    }
}
