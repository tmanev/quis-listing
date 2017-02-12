package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.builder.DlListingDTOBuilder;
import org.springframework.stereotype.Component;

@Component
public class DlListingMapper {

    public DlListing dlListingDTOToDlListing(DlListingDTO dlListingDTO) {
//        return DlListingBuilder.aDlListing()
//                .withId(dlListingDTO.getId())
//                .withTitle(dlListingDTO.getTitle()
//                .withStatus(dlListingDTO.getStatus())
//
//                ).build();
        return null;
    }

    public DlListingDTO dlListingToDlListingDTO(DlListing dlListing) {
        return DlListingDTOBuilder.aDlListingDTO()
                .withId(dlListing.getId())
                .withTitle(dlListing.getTitle())
                .withContent(dlListing.getContent())
                .withName(dlListing.getName())
                .withStatus(dlListing.getStatus())
                .withTitle(dlListing.getTitle()).build();
    }



}
