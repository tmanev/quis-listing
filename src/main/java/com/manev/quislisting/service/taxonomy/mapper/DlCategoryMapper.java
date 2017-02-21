package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlCategoryBuilder;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.DlCategoryDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                ).withDescription(dlCategoryDTO.getDescription()).build();
    }

    public List<DlCategoryDTO> dlCategoryToDlCategoryDtoFlat(Page<DlCategory> page) {
        Set<Long> ids = new HashSet<>();

        List<DlCategoryDTO> result = new ArrayList<>();
        for (DlCategory dlCategory : page) {
            doMappingAndFillDepthLevel(dlCategory, ids, result, 0);
        }

        return result;
    }

    private int doMappingAndFillDepthLevel(DlCategory dlCategory, Set<Long> ids, List<DlCategoryDTO> result, int depthLevel) {
        int resultDepthLevel = depthLevel;
        if (dlCategory.getParent() != null) {
            resultDepthLevel = doMappingAndFillDepthLevel(dlCategory.getParent(), ids, result, depthLevel + 1);
        }
        if (!ids.contains(dlCategory.getId())) {
            ids.add(dlCategory.getId());
            DlCategoryDTO dlCategoryDTO = dlCategoryToDlCategoryDTO(dlCategory);
            dlCategoryDTO.setDepthLevel(resultDepthLevel);
            result.add(dlCategoryDTO);
        }
        return resultDepthLevel;
    }

    public DlCategoryDTO dlCategoryToDlCategoryDTO(DlCategory dlCategory) {
        return DlCategoryDTOBuilder.aDlCategoryDTO().withId(dlCategory.getId())
                .withTerm(
                        TermDTOBuilder.aTerm()
                                .withId(dlCategory.getTerm().getId())
                                .withName(dlCategory.getTerm().getName())
                                .withSlug(dlCategory.getTerm().getSlug()).build())
                .withDescription(dlCategory.getDescription())
                .withParentId(dlCategory.getParent() != null ? dlCategory.getParent().getId() : null)
                .withCount(dlCategory.getCount()).build();
    }

}
