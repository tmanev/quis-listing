package com.manev.quislisting.service.taxonomy.dto;

import java.util.List;

public class NavMenuDTO extends TermTaxonomyDTO {

    private List<StaticPageNavMenuDTO> staticPageNavMenuDTOS;

    public List<StaticPageNavMenuDTO> getStaticPageNavMenuDTOS() {
        return staticPageNavMenuDTOS;
    }

    public void setStaticPageNavMenuDTOS(List<StaticPageNavMenuDTO> staticPageNavMenuDTOS) {
        this.staticPageNavMenuDTOS = staticPageNavMenuDTOS;
    }
}
