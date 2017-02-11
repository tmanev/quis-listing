package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.builder.DlListingBuilder;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.builder.DlListingDTOBuilder;
import org.springframework.stereotype.Component;

@Component
public class DlListingMapper {

    public DlListing dlListingDTOToDlListing(DlListingDTO dlListingDTO) {
        return DlListingBuilder.aDlListing()
                .withId(dlListingDTO.getId())
                .withName(dlListingDTO.getName())
                .withStatus(dlListingDTO.getStatus())
                .withContent(dlListingDTO.getContent())
                .withTitle(dlListingDTO.getTitle()).build();
    }

    public DlListingDTO dlListingToDlListingDTO(DlListing dlListing) {
        return DlListingDTOBuilder.aDlListingDTO()
                .withId(dlListing.getId())
                .withName(dlListing.getName())
                .withStatus(dlListing.getStatus())
                .withContent(dlListing.getContent())
                .withTitle(dlListing.getTitle()).build();
    }



}
