package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlLocationBuilder;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.DlLocationDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import org.springframework.stereotype.Component;

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
                .withParentId(dlLocation.getParentId())
                .withCount(dlLocation.getCount()).build();
    }

}
