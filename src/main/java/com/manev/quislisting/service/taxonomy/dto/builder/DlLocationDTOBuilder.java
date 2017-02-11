package com.manev.quislisting.service.taxonomy.dto.builder;

import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.TermDTO;

/**
 * Created by tmanev on 2/7/2017.
 */
public final class DlLocationDTOBuilder {
    private Long id;
    private TermDTO term;
    private Long parentId;
    private String description;
    private Long count;
    private String languageId;

    private DlLocationDTOBuilder() {
    }

    public static DlLocationDTOBuilder aDlLocationDTO() {
        return new DlLocationDTOBuilder();
    }

    public DlLocationDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlLocationDTOBuilder withTerm(TermDTO term) {
        this.term = term;
        return this;
    }

    public DlLocationDTOBuilder withParentId(Long parentId) {
        this.parentId = parentId;
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
        dlLocationDTO.setTerm(term);
        dlLocationDTO.setParentId(parentId);
        dlLocationDTO.setDescription(description);
        dlLocationDTO.setCount(count);
        dlLocationDTO.setLanguageId(languageId);
        return dlLocationDTO;
    }
}
