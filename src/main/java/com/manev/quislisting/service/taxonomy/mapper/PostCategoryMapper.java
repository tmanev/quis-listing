package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.PostCategoryBuilder;
import com.manev.quislisting.service.taxonomy.dto.PostCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.PostCategoryDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PostCategoryMapper {

    public PostCategory postCategoryDTOToPostCategory(PostCategoryDTO postCategoryDto) {
        return PostCategoryBuilder.aPostCategory()
                .withId(postCategoryDto.getId())
                .withTerm(TermBuilder.aTerm()
                        .withId(postCategoryDto.getTerm().getId())
                        .withName(postCategoryDto.getTerm().getName())
                        .withSlug(postCategoryDto.getTerm().getSlug())
                        .build())
                .withDescription(postCategoryDto.getDescription())
                .withCount(postCategoryDto.getCount())
                .withTranslation(
                        TranslationBuilder.aTranslation()
                                .withLanguageCode(postCategoryDto.getLanguageCode())
                                .build())
                .build();
    }

    public List<PostCategoryDTO> dlLocationToDlLocationDtoFlat(Page<PostCategory> page) {
        Set<Long> ids = new HashSet<>();

        List<PostCategoryDTO> result = new ArrayList<>();
        for (PostCategory postCategory : page) {
            doMappingAndFillDepthLevel(postCategory, ids, result);
        }

        return result;
    }

    private int doMappingAndFillDepthLevel(PostCategory postCategory, Set<Long> ids, List<PostCategoryDTO> result) {
        if (postCategory.getParent() != null) {
            int depthLevel = doMappingAndFillDepthLevel(postCategory.getParent(), ids, result) + 1;
            pushToTheList(postCategory, ids, result, depthLevel);
            return depthLevel;
        } else {
            int depthLevel = 0;
            pushToTheList(postCategory, ids, result, depthLevel);
            return 0;
        }
    }

    private void pushToTheList(PostCategory postCategory, Set<Long> ids, List<PostCategoryDTO> result, int depthLevel) {
        if (!ids.contains(postCategory.getId())) {
            ids.add(postCategory.getId());
            PostCategoryDTO postCategoryDTO = postCategoryToPostCategoryDTO(postCategory);
            postCategoryDTO.setDepthLevel(depthLevel);
            result.add(postCategoryDTO);
        }
    }

    public PostCategoryDTO postCategoryToPostCategoryDTO(PostCategory postCategory) {
        return PostCategoryDTOBuilder.aPostCategoryDTO()
                .withId(postCategory.getId())
                .withTerm(TermDTOBuilder.aTerm()
                        .withId(postCategory.getTerm().getId())
                        .withName(postCategory.getTerm().getName())
                        .withSlug(postCategory.getTerm().getSlug())
                        .build())
                .withDescription(postCategory.getDescription())
                .withParentId(postCategory.getParent() != null ? postCategory.getParent().getId() : null)
                .withCount(postCategory.getCount())
                .withLanguageId(postCategory.getTranslation() != null ? postCategory.getTranslation().getLanguageCode() : null)
                .build();
    }

}
