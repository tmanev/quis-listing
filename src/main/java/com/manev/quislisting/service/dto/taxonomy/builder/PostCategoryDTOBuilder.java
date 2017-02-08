package com.manev.quislisting.service.dto.taxonomy.builder;

import com.manev.quislisting.service.dto.taxonomy.PostCategoryDTO;
import com.manev.quislisting.service.dto.taxonomy.TermDTO;

public final class PostCategoryDTOBuilder {
    private Long id;
    private TermDTO termDTO;
    private Long parentId;
    private String description;
    private Long count;
    private String languageId;

    private PostCategoryDTOBuilder() {
    }

    public static PostCategoryDTOBuilder aPostCategoryDTO() {
        return new PostCategoryDTOBuilder();
    }

    public PostCategoryDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PostCategoryDTOBuilder withTerm(TermDTO termDTO) {
        this.termDTO = termDTO;
        return this;
    }

    public PostCategoryDTOBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public PostCategoryDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PostCategoryDTOBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public PostCategoryDTOBuilder withLanguageId(String languageId) {
        this.languageId = languageId;
        return this;
    }

    public PostCategoryDTO build() {
        PostCategoryDTO postCategoryDTO = new PostCategoryDTO();
        postCategoryDTO.setId(id);
        postCategoryDTO.setTerm(termDTO);
        postCategoryDTO.setParentId(parentId);
        postCategoryDTO.setDescription(description);
        postCategoryDTO.setCount(count);
        postCategoryDTO.setLanguageId(languageId);
        return postCategoryDTO;
    }
}
