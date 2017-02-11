package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.PostCategoryBuilder;
import com.manev.quislisting.service.taxonomy.dto.PostCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.PostCategoryDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import org.springframework.stereotype.Component;

@Component
public class PostCategoryMapper {

    public PostCategory postCategoryDTOToPostCategory(PostCategoryDTO postCategoryDto) {
        return PostCategoryBuilder.aPostCategory()
                .withId(postCategoryDto.getId())
                .withTerm(
                        TermBuilder.aTerm()
                                .withId(postCategoryDto.getTerm().getId())
                                .withName(postCategoryDto.getTerm().getName())
                                .withSlug(postCategoryDto.getTerm().getSlug()).build()
                ).withDescription(postCategoryDto.getDescription())
                .withParentId(postCategoryDto.getParentId())
                .withCount(postCategoryDto.getCount()).build();
    }

    public PostCategoryDTO postCategoryToPostCategoryDTO(PostCategory postCategory) {
        return PostCategoryDTOBuilder.aPostCategoryDTO().withId(postCategory.getId())
                .withTerm(
                        TermDTOBuilder.aTerm()
                                .withId(postCategory.getTerm().getId())
                                .withName(postCategory.getTerm().getName())
                                .withSlug(postCategory.getTerm().getSlug()).build())
                .withDescription(postCategory.getDescription())
                .withParentId(postCategory.getParentId())
                .withCount(postCategory.getCount()).build();
    }

}
