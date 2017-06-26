package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.TranslationBuilder;
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
                .withTerm(TermBuilder.aTerm()
                        .withId(dlCategoryDTO.getTerm().getId())
                        .withName(dlCategoryDTO.getTerm().getName())
                        .withSlug(dlCategoryDTO.getTerm().getSlug())
                        .build())
                .withDescription(dlCategoryDTO.getDescription())
                .withTranslation(
                        TranslationBuilder.aTranslation()
                                .withLanguageCode(dlCategoryDTO.getLanguageCode())
                                .build())
                .build();
    }

    public List<DlCategoryDTO> dlCategoryToDlCategoryDtoFlat(List<DlCategory> page) {
        Set<Long> ids = new HashSet<>();

        List<DlCategoryDTO> result = new ArrayList<>();
        for (DlCategory dlCategory : page) {
            doMappingAndFillDepthLevel(dlCategory, ids, result);
        }

        return result;
    }

    private int doMappingAndFillDepthLevel(DlCategory dlCategory, Set<Long> ids, List<DlCategoryDTO> result) {
        if (dlCategory.getParent() != null) {
            int depthLevel = doMappingAndFillDepthLevel(dlCategory.getParent(), ids, result) + 1;
            pushToTheList(dlCategory, ids, result, depthLevel);
            return depthLevel;
        } else {
            int depthLevel = 0;
            pushToTheList(dlCategory, ids, result, depthLevel);
            return 0;
        }
    }

    private void pushToTheList(DlCategory dlCategory, Set<Long> ids, List<DlCategoryDTO> result, int depthLevel) {
        if (!ids.contains(dlCategory.getId())) {
            ids.add(dlCategory.getId());
            DlCategoryDTO dlCategoryDTO = dlCategoryToDlCategoryDTO(dlCategory);
            dlCategoryDTO.setDepthLevel(depthLevel);
            result.add(dlCategoryDTO);
        }
    }

    public DlCategoryDTO dlCategoryToDlCategoryDTO(DlCategory dlCategory) {
        return DlCategoryDTOBuilder.aDlCategoryDTO()
                .withId(dlCategory.getId())
                .withTerm(TermDTOBuilder.aTerm()
                        .withId(dlCategory.getTerm().getId())
                        .withName(dlCategory.getTerm().getName())
                        .withSlug(dlCategory.getTerm().getSlug())
                        .build())
                .withDescription(dlCategory.getDescription())
                .withParentId(dlCategory.getParent() != null ? dlCategory.getParent().getId() : null)
                .withCount(dlCategory.getCount())
                .withLanguageId(dlCategory.getTranslation() != null ? dlCategory.getTranslation().getLanguageCode() : null)
                .build();
    }

}
