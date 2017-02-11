package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlCategoryBuilder;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.DlCategoryDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import org.springframework.stereotype.Component;

@Component
public class DlCategoryMapper {

    public DlCategory dlCategoryDTOTodlCategory(DlCategoryDTO dlCategoryDTO) {
        return DlCategoryBuilder.aDlCategory()
                .withId(dlCategoryDTO.getId())
                .withTerm(
                        TermBuilder.aTerm()
                                .withId(dlCategoryDTO.getTerm().getId())
                                .withName(dlCategoryDTO.getTerm().getName())
                                .withSlug(dlCategoryDTO.getTerm().getSlug()).build()
                ).withDescription(dlCategoryDTO.getDescription())
                .withParentId(dlCategoryDTO.getParentId())
                .withCount(dlCategoryDTO.getCount()).build();
    }

    public DlCategoryDTO dlCategoryToDlCategoryDTO(DlCategory dlCategory) {
        return DlCategoryDTOBuilder.aDlCategoryDTO().withId(dlCategory.getId())
                .withTerm(
                        TermDTOBuilder.aTerm()
                                .withId(dlCategory.getTerm().getId())
                                .withName(dlCategory.getTerm().getName())
                                .withSlug(dlCategory.getTerm().getSlug()).build())
                .withDescription(dlCategory.getDescription())
                .withParentId(dlCategory.getParentId())
                .withCount(dlCategory.getCount()).build();
    }

}
