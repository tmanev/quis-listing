package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlCategoryBuilder;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.DlCategoryDTOBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DlCategoryMapper {

    public DlCategory dlCategoryDtoToDlCategory(DlCategoryDTO dlCategoryDTO) {
        return DlCategoryBuilder.aDlCategory()
                .withId(dlCategoryDTO.getId())
                .withName(dlCategoryDTO.getName())
                .withSlug(dlCategoryDTO.getSlug())
                .withDescription(dlCategoryDTO.getDescription())
                .withTranslation(
                        TranslationBuilder.aTranslation()
                                .withLanguageCode(dlCategoryDTO.getLanguageCode())
                                .withTranslationGroup(new TranslationGroup())
                                .withSourceLanguageCode(dlCategoryDTO.getSourceLanguageCode())
                                .build())
                .build();
    }

    public DlCategory dlCategoryDtoToDlCategory(DlCategory existingDlCategory, DlCategoryDTO dlCategoryDTO) {
        existingDlCategory.setName(dlCategoryDTO.getName());
        existingDlCategory.setDescription(dlCategoryDTO.getDescription());
        existingDlCategory.setSlug(dlCategoryDTO.getSlug());

        return existingDlCategory;
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
                .withName(dlCategory.getName())
                .withSlug(dlCategory.getSlug())
                .withDescription(dlCategory.getDescription())
                .withParentId(dlCategory.getParent() != null ? dlCategory.getParent().getId() : null)
                .withCount(dlCategory.getCount())
                .withLanguageCode(dlCategory.getTranslation() != null ? dlCategory.getTranslation().getLanguageCode() : null)
                .withSourceLanguageCode(dlCategory.getTranslation().getSourceLanguageCode())
                .withTranslationGroupId(dlCategory.getTranslation().getTranslationGroup().getId())
                .build();
    }

}
