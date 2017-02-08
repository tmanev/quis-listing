package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.NavMenuBuilder;
import com.manev.quislisting.service.dto.taxonomy.builder.NavMenuDTOBuilder;
import com.manev.quislisting.service.dto.taxonomy.builder.TermDTOBuilder;
import com.manev.quislisting.service.dto.taxonomy.NavMenuDTO;
import org.springframework.stereotype.Component;

@Component
public class NavMenuMapper {

    public NavMenu navMenuDTOToNavMenu(NavMenuDTO navMenuDTO) {
        return NavMenuBuilder.aNavMenu()
                .withId(navMenuDTO.getId())
                .withTerm(
                        TermBuilder.aTerm()
                                .withId(navMenuDTO.getTerm().getId())
                                .withName(navMenuDTO.getTerm().getName())
                                .withSlug(navMenuDTO.getTerm().getSlug()).build()
                ).withDescription(navMenuDTO.getDescription())
                .withParentId(navMenuDTO.getParentId())
                .withCount(navMenuDTO.getCount()).build();
    }

    public NavMenuDTO navMenuToNavMenuDTO(NavMenu navMenu) {
        return NavMenuDTOBuilder.aNavMenuDTO().withId(navMenu.getId())
                .withTerm(
                        TermDTOBuilder.aTerm()
                                .withId(navMenu.getTerm().getId())
                                .withName(navMenu.getTerm().getName())
                                .withSlug(navMenu.getTerm().getSlug()).build())
                .withDescription(navMenu.getDescription())
                .withParentId(navMenu.getParentId())
                .withCount(navMenu.getCount()).build();
    }

}
