package com.manev.quislisting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.QlMenuPosConfig;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import org.springframework.ui.ModelMap;

import java.io.IOException;

public class BaseController {

    protected NavMenuRepository navMenuRepository;

    protected QlConfigRepository qlConfigRepository;

    public BaseController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository) {
        this.navMenuRepository = navMenuRepository;
        this.qlConfigRepository = qlConfigRepository;
    }

    protected void loadMenus(ModelMap model) throws IOException {
        QlConfig qlConfigMenu = qlConfigRepository.findOneByKey("ql-menu-positions");

        QlMenuPosConfig qlMenuPosConfig = new ObjectMapper().readValue(qlConfigMenu.getValue(),
                QlMenuPosConfig.class);
        NavMenu topHeaderMenu = navMenuRepository.findOne(qlMenuPosConfig.getTopHeaderMenuRefId());
        NavMenu footerMenu = navMenuRepository.findOne(qlMenuPosConfig.getFooterMenuRefId());
        model.addAttribute("topHeaderMenus", topHeaderMenu.getNavMenuItems());
        model.addAttribute("footerMenus", footerMenu.getNavMenuItems());
    }

}
