package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlLocationBuilder;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.DlLocationDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DlLocationMapper {

    public DlLocation dlLocationDTOTodlLocation(DlLocationDTO dlLocationDTO) {
        return DlLocationBuilder.aDlLocation()
                .withId(dlLocationDTO.getId())
                .withTerm(
                        TermBuilder.aTerm()
                                .withId(dlLocationDTO.getTerm().getId())
                                .withName(dlLocationDTO.getTerm().getName())
                                .withSlug(dlLocationDTO.getTerm().getSlug()).build()
                ).withDescription(dlLocationDTO.getDescription())
                .withParentId(dlLocationDTO.getParentId())
                .withCount(dlLocationDTO.getCount()).build();
    }

    public DlLocationDTO dlLocationToDlLocationDTO(DlLocation dlLocation) {
        return DlLocationDTOBuilder.aDlLocationDTO().withId(dlLocation.getId())
                .withTerm(
                        TermDTOBuilder.aTerm()
                                .withId(dlLocation.getTerm().getId())
                                .withName(dlLocation.getTerm().getName())
                                .withSlug(dlLocation.getTerm().getSlug()).build())
                .withDescription(dlLocation.getDescription())
                .withParentId(dlLocation.getParent() != null ? dlLocation.getParent().getId() : null)
                .withCount(dlLocation.getCount()).build();
    }

    public List<DlLocationDTO> dlLocationToDlLocationDtoFlat(Page<DlLocation> page) {
        Set<Long> ids = new HashSet<>();

        List<DlLocationDTO> result = new ArrayList<>();
        for (DlLocation dlLocation : page) {
            doMappingAndFillDepthLevel(dlLocation, ids, result, 0);
        }

        return result;
    }

    private int doMappingAndFillDepthLevel(DlLocation dlCategory, Set<Long> ids, List<DlLocationDTO> result, int depthLevel) {
        int resultDepthLevel = depthLevel;
        if (dlCategory.getParent() != null) {
            resultDepthLevel = doMappingAndFillDepthLevel(dlCategory.getParent(), ids, result, depthLevel + 1);
        }
        if (!ids.contains(dlCategory.getId())) {
            ids.add(dlCategory.getId());
            DlLocationDTO dlCategoryDTO = dlLocationToDlLocationDTO(dlCategory);
            dlCategoryDTO.setDepthLevel(resultDepthLevel);
            result.add(dlCategoryDTO);
        }
        return resultDepthLevel;
    }
}
