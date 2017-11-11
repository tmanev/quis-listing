package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlLocationBuilder;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.DlLocationDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
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
                .withName(dlLocationDTO.getName())
                .withSlug(dlLocationDTO.getSlug())
                .withDescription(dlLocationDTO.getDescription())
                .withTranslation(
                        TranslationBuilder.aTranslation()
                                .withLanguageCode(dlLocationDTO.getLanguageCode())
                                .withTranslationGroup(new TranslationGroup())
                                .withSourceLanguageCode(dlLocationDTO.getSourceLanguageCode())
                                .build())
                .build();
    }

    public List<DlLocationDTO> dlLocationToDlLocationDtoFlat(List<DlLocation> dlLocations) {
        Set<Long> ids = new HashSet<>();

        List<DlLocationDTO> result = new ArrayList<>();
        for (DlLocation dlLocation : dlLocations) {
            doMappingAndFillDepthLevel(dlLocation, ids, result);
        }

        return result;
    }

    private int doMappingAndFillDepthLevel(DlLocation dlLocation, Set<Long> ids, List<DlLocationDTO> result) {
        if (dlLocation.getParent() != null) {
            int depthLevel = doMappingAndFillDepthLevel(dlLocation.getParent(), ids, result) + 1;
            pushToTheList(dlLocation, ids, result, depthLevel);
            return depthLevel;
        } else {
            int depthLevel = 0;
            pushToTheList(dlLocation, ids, result, depthLevel);
            return 0;
        }
    }

    private void pushToTheList(DlLocation dlLocation, Set<Long> ids, List<DlLocationDTO> result, int depthLevel) {
        if (!ids.contains(dlLocation.getId())) {
            ids.add(dlLocation.getId());
            DlLocationDTO dlLocationDTO = dlLocationToDlLocationDTO(dlLocation);
            dlLocationDTO.setDepthLevel(depthLevel);
            result.add(dlLocationDTO);
        }
    }

    public DlLocationDTO dlLocationToDlLocationDTO(DlLocation dlLocation) {
        return DlLocationDTOBuilder.aDlLocationDTO().withId(dlLocation.getId())
                        .withName(dlLocation.getName())
                        .withSlug(dlLocation.getSlug())
                .withDescription(dlLocation.getDescription())
                .withParentId(dlLocation.getParent() != null ? dlLocation.getParent().getId() : null)
                .withParent(dlLocation.getParent() != null ? this.dlLocationToDlLocationDTO(dlLocation.getParent()) : null)
                .withCount(dlLocation.getCount())
                .withLanguageId(dlLocation.getTranslation() != null ? dlLocation.getTranslation().getLanguageCode() : null)
                .build();
    }

    public DlLocation dlLocationDTOTodlLocation(DlLocation existingDlLocation, DlLocationDTO dlLocationDTO) {
        existingDlLocation.setName(dlLocationDTO.getName());
        existingDlLocation.setDescription(dlLocationDTO.getDescription());
        existingDlLocation.setSlug(dlLocationDTO.getSlug());

        return existingDlLocation;
    }
}
