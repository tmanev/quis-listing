package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.domain.StaticPageNavMenuRel;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.StaticPageRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
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

    private StaticPageNavMenuRel navMenuItemDtoToNavMenuItem(NavMenu navMenu, StaticPageNavMenuDTO staticPageNavMenuDTO) {
        StaticPageNavMenuRel staticPageNavMenuRel = new StaticPageNavMenuRel();

        staticPageNavMenuRel.setId(staticPageNavMenuDTO.getId());
        staticPageNavMenuRel.setNavMenu(navMenu);

        StaticPage staticPage = staticPageRepository.findOne(staticPageNavMenuDTO.getId());
        staticPageNavMenuRel.setStaticPage(staticPage);

        return staticPageNavMenuRel;
    }

    public Set<StaticPageNavMenuRel> navMenuItemDtoToNavMenuItem(NavMenu navMenuDTO, List<StaticPageNavMenuDTO> staticPageNavMenuDTOS) {
        Set<StaticPageNavMenuRel> navMenuItems = new HashSet<>();

        for (StaticPageNavMenuDTO staticPageNavMenuDTO : staticPageNavMenuDTOS) {
            navMenuItems.add(navMenuItemDtoToNavMenuItem(navMenuDTO, staticPageNavMenuDTO));
        }

        return navMenuItems;
    }

    public StaticPageNavMenuDTO navMenuItemToNavMenuItemDto(StaticPageNavMenuRel staticPageNavMenuRel) {
        StaticPageNavMenuDTO staticPageNavMenuDTO = new StaticPageNavMenuDTO();
        staticPageNavMenuDTO.setId(staticPageNavMenuRel.getId());
        staticPageNavMenuDTO.setTitle(staticPageNavMenuRel.getStaticPage().getTitle());
        staticPageNavMenuDTO.setOrder(staticPageNavMenuRel.getMenuOrder());

        return staticPageNavMenuDTO;
    }

    public List<StaticPageNavMenuDTO> navMenuItemToNavMenuItemDto(Set<StaticPageNavMenuRel> staticPageNavMenuRels) {
        List<StaticPageNavMenuDTO> staticPageNavMenuDTOS = new ArrayList<>();
        if (staticPageNavMenuRels != null) {
            for (StaticPageNavMenuRel staticPageNavMenuRel : staticPageNavMenuRels) {
                staticPageNavMenuDTOS.add(navMenuItemToNavMenuItemDto(staticPageNavMenuRel));
            }
        }
        return staticPageNavMenuDTOS;
    }

}
