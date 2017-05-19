package com.manev.quislisting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.QlMenuPosConfig;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.List;

public class BaseController {

    protected NavMenuRepository navMenuRepository;

    protected QlConfigRepository qlConfigRepository;

    protected LanguageRepository languageRepository;

    public BaseController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                          LanguageRepository languageRepository) {
        this.navMenuRepository = navMenuRepository;
        this.qlConfigRepository = qlConfigRepository;
        this.languageRepository = languageRepository;
    }

    @ModelAttribute("baseModel")
    public BaseModel baseModel() throws IOException {
        QlConfig qlConfigMenu = qlConfigRepository.findOneByKey("ql-menu-positions");

        QlMenuPosConfig qlMenuPosConfig = new ObjectMapper().readValue(qlConfigMenu.getValue(),
                QlMenuPosConfig.class);
        NavMenu topHeaderMenu = navMenuRepository.findOne(qlMenuPosConfig.getTopHeaderMenuRefId());
        NavMenu footerMenu = navMenuRepository.findOne(qlMenuPosConfig.getFooterMenuRefId());

        List<Language> allByActive = languageRepository.findAllByActive(true);

        return new BaseModel()
                .topHeaderMenus(topHeaderMenu.getNavMenuItems())
                .footerMenus(footerMenu.getNavMenuItems())
                .activeLanugages(allByActive);
    }

}
