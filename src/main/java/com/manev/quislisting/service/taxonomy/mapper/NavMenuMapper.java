package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.NavMenuBuilder;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.NavMenuDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                .withParentId(navMenu.getParent() != null ? navMenu.getParent().getId() : null)
                .withCount(navMenu.getCount()).build();
    }

    public List<NavMenuDTO> navMenuToNavMenuDtoFlat(Page<NavMenu> page) {
        Set<Long> ids = new HashSet<>();

        List<NavMenuDTO> result = new ArrayList<>();
        for (NavMenu dlLocation : page) {
            doMappingAndFillDepthLevel(dlLocation, ids, result, 0);
        }

        return result;
    }

    private int doMappingAndFillDepthLevel(NavMenu navMenu, Set<Long> ids, List<NavMenuDTO> result, int depthLevel) {
        int resultDepthLevel = depthLevel;
        if (navMenu.getParent() != null) {
            resultDepthLevel = doMappingAndFillDepthLevel(navMenu.getParent(), ids, result, depthLevel + 1);
        }
        if (!ids.contains(navMenu.getId())) {
            ids.add(navMenu.getId());
            NavMenuDTO navMenuDTO = navMenuToNavMenuDTO(navMenu);
            navMenuDTO.setDepthLevel(resultDepthLevel);
            result.add(navMenuDTO);
        }
        return resultDepthLevel;
    }

}
