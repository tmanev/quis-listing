package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.NavMenuBuilder;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.NavMenuDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
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
                .withTerm(TermBuilder.aTerm()
                        .withId(navMenuDTO.getTerm().getId())
                        .withName(navMenuDTO.getTerm().getName())
                        .withSlug(navMenuDTO.getTerm().getSlug())
                        .build())
                .withDescription(navMenuDTO.getDescription())
                .withTranslation(
                        TranslationBuilder.aTranslation()
                                .withLanguageCode(navMenuDTO.getLanguageCode())
                                .build())
                .build();
    }


    public List<NavMenuDTO> navMenuToNavMenuDtoFlat(Page<NavMenu> page) {
        Set<Long> ids = new HashSet<>();

        List<NavMenuDTO> result = new ArrayList<>();
        for (NavMenu navMenu : page) {
            doMappingAndFillDepthLevel(navMenu, ids, result);
        }

        return result;
    }

    private int doMappingAndFillDepthLevel(NavMenu navMenu, Set<Long> ids, List<NavMenuDTO> result) {
        if (navMenu.getParent() != null) {
            int depthLevel = doMappingAndFillDepthLevel(navMenu.getParent(), ids, result) + 1;
            pushToTheList(navMenu, ids, result, depthLevel);
            return depthLevel;
        } else {
            int depthLevel = 0;
            pushToTheList(navMenu, ids, result, depthLevel);
            return 0;
        }
    }

    private void pushToTheList(NavMenu navMenu, Set<Long> ids, List<NavMenuDTO> result, int depthLevel) {
        if (!ids.contains(navMenu.getId())) {
            ids.add(navMenu.getId());
            NavMenuDTO navMenuDTO = navMenuToNavMenuDTO(navMenu);
            navMenuDTO.setDepthLevel(depthLevel);
            result.add(navMenuDTO);
        }
    }

    public NavMenuDTO navMenuToNavMenuDTO(NavMenu navMenu) {
        return NavMenuDTOBuilder.aNavMenuDTO()
                .withId(navMenu.getId())
                .withTerm(TermDTOBuilder.aTerm()
                        .withId(navMenu.getTerm().getId())
                        .withName(navMenu.getTerm().getName())
                        .withSlug(navMenu.getTerm().getSlug())
                        .build())
                .withDescription(navMenu.getDescription())
                .withParentId(navMenu.getParent() != null ? navMenu.getParent().getId() : null)
                .withCount(navMenu.getCount())
                .withLanguageId(navMenu.getTranslation() != null ? navMenu.getTranslation().getLanguageCode() : null)
                .build();
    }

}
