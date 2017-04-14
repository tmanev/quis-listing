package com.manev.quislisting.service.taxonomy.dto;

import java.util.List;

public class NavMenuDTO extends TermTaxonomyDTO {

    private List<NavMenuItemDTO> navMenuItemDTOs;

    public List<NavMenuItemDTO> getNavMenuItemDTOs() {
        return navMenuItemDTOs;
    }

    public void setNavMenuItemDTOs(List<NavMenuItemDTO> navMenuItemDTOs) {
        this.navMenuItemDTOs = navMenuItemDTOs;
    }
}
