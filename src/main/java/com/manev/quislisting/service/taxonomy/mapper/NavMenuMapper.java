package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.post.discriminator.NavMenuItem;
import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.post.NavMenuItemRepository;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.NavMenuItemDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.NavMenuDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class NavMenuMapper {

    private NavMenuItemMapper navMenuItemMapper;
    private NavMenuItemRepository navMenuItemRepository;

    public NavMenuMapper(NavMenuItemMapper navMenuItemMapper, NavMenuItemRepository navMenuItemRepository) {
        this.navMenuItemMapper = navMenuItemMapper;
        this.navMenuItemRepository = navMenuItemRepository;
    }

    public List<NavMenuDTO> navMenuToNavMenuDtoFlat(Page<NavMenu> page) {
        List<NavMenuDTO> result = new ArrayList<>();
        for (NavMenu navMenu : page) {
            result.add(navMenuToNavMenuDTO(navMenu));
        }

        return result;
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
                .withCount(navMenu.getCount())
                .withLanguageId(navMenu.getTranslation() != null ? navMenu.getTranslation().getLanguageCode() : null)
                .withNavMenuItemDTOList(navMenuItemMapper.navMenuItemToNavMenuItemDto(navMenu.getNavMenuItems()))
                .build();
    }

    public NavMenu navMenuDTOToNavMenu(NavMenuDTO navMenuDTO) {
        return navMenuDTOToNavMenu(new NavMenu(), navMenuDTO);
    }

    public NavMenu navMenuDTOToNavMenu(NavMenu existingNavMenu, NavMenuDTO navMenuDTO) {
        existingNavMenu.setId(navMenuDTO.getId());

        Term term = existingNavMenu.getTerm();
        if (term == null) {
            term = new Term();
        }
        term.setId(navMenuDTO.getTerm().getId());
        term.setName(navMenuDTO.getTerm().getName());
        term.setSlug(navMenuDTO.getTerm().getSlug());

        existingNavMenu.setDescription(navMenuDTO.getDescription());

        Translation translation = existingNavMenu.getTranslation();
        if (translation == null) {
            translation = TranslationBuilder.aTranslation()
                    .withLanguageCode(navMenuDTO.getLanguageCode())
                    .build();
            existingNavMenu.setTranslation(translation);
        }

        Set<NavMenuItem> existingNavMenuItems = existingNavMenu.getNavMenuItems();
        List<NavMenuItem> toBeDeletedNavMenuItems = new ArrayList<>();
        if (existingNavMenuItems == null) {
            existingNavMenu.setNavMenuItems(navMenuItemMapper.navMenuItemDtoToNavMenuItem(navMenuDTO, navMenuDTO.getNavMenuItemDTOs()));
        } else {
            // search existing one
            Iterator<NavMenuItem> iterator = existingNavMenuItems.iterator();
            while (iterator.hasNext()) {
                NavMenuItem navMenuItem = iterator.next();
                NavMenuItemDTO existingNavMenuItemDto = findAndRemoveNavMenuItem(navMenuItem, navMenuDTO.getNavMenuItemDTOs());
                if (existingNavMenuItemDto != null) {
                    navMenuItem.setTitle(existingNavMenuItemDto.getTitle());
                } else {
                    // remove the item if not present in the list
                    toBeDeletedNavMenuItems.add(navMenuItem);
                    iterator.remove();
                }
            }
            // add the rest of the nav menu items
            existingNavMenuItems.addAll(navMenuItemMapper.navMenuItemDtoToNavMenuItem(navMenuDTO, navMenuDTO.getNavMenuItemDTOs()));
            navMenuItemRepository.delete(toBeDeletedNavMenuItems);
        }

        return existingNavMenu;
    }

    private NavMenuItemDTO findAndRemoveNavMenuItem(NavMenuItem navMenuItem, List<NavMenuItemDTO> navMenuItemDTOs) {
        Iterator<NavMenuItemDTO> iterator = navMenuItemDTOs.iterator();
        while (iterator.hasNext()) {
            NavMenuItemDTO navMenuItemDTO = iterator.next();
            if (navMenuItemDTO.getId() != null && navMenuItemDTO.getId().equals(navMenuItem.getId())) {
                iterator.remove();
                return navMenuItemDTO;
            }
        }
        return null;
    }
}
