package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.domain.NavMenuItem;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.StaticPageRepository;
import com.manev.quislisting.service.taxonomy.dto.StaticPageNavMenuDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class NavMenuItemMapper {

    private StaticPageRepository staticPageRepository;

    public NavMenuItemMapper(StaticPageRepository staticPageRepository) {
        this.staticPageRepository = staticPageRepository;
    }

    private NavMenuItem navMenuItemDtoToNavMenuItem(NavMenu navMenu, StaticPageNavMenuDTO staticPageNavMenuDTO) {
        NavMenuItem navMenuItem = new NavMenuItem();

        navMenuItem.setId(staticPageNavMenuDTO.getId());
        navMenuItem.setNavMenu(navMenu);

        StaticPage staticPage = staticPageRepository.findOne(staticPageNavMenuDTO.getId());
        navMenuItem.setStaticPage(staticPage);

        return navMenuItem;
    }

    public Set<NavMenuItem> navMenuItemDtoToNavMenuItem(NavMenu navMenuDTO, List<StaticPageNavMenuDTO> staticPageNavMenuDTOS) {
        Set<NavMenuItem> navMenuItems = new HashSet<>();

        for (StaticPageNavMenuDTO staticPageNavMenuDTO : staticPageNavMenuDTOS) {
            navMenuItems.add(navMenuItemDtoToNavMenuItem(navMenuDTO, staticPageNavMenuDTO));
        }

        return navMenuItems;
    }

    public StaticPageNavMenuDTO navMenuItemToNavMenuItemDto(NavMenuItem navMenuItem) {
        StaticPageNavMenuDTO staticPageNavMenuDTO = new StaticPageNavMenuDTO();
        staticPageNavMenuDTO.setId(navMenuItem.getId());
        staticPageNavMenuDTO.setTitle(navMenuItem.getTitle());
        staticPageNavMenuDTO.setOrder(navMenuItem.getMenuOrder());

        return staticPageNavMenuDTO;
    }

    public List<StaticPageNavMenuDTO> navMenuItemToNavMenuItemDto(Set<NavMenuItem> navMenuItems) {
        List<StaticPageNavMenuDTO> staticPageNavMenuDTOS = new ArrayList<>();
        if (navMenuItems != null) {
            for (NavMenuItem navMenuItem : navMenuItems) {
                staticPageNavMenuDTOS.add(navMenuItemToNavMenuItemDto(navMenuItem));
            }
        }
        return staticPageNavMenuDTOS;
    }

}
