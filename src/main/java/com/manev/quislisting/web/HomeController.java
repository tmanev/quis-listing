package com.manev.quislisting.web;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.post.StaticPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {


    public HomeController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService,
                          StaticPageService staticPageService, LanguageRepository languageRepository,
                          LocaleResolver localeResolver,
                          LanguageTranslationRepository languageTranslationRepository) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver,
                staticPageService);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model) {
        StaticPage post = staticPageService.findOneByName("");

        model.addAttribute("title", post.getTitle());
        model.addAttribute("view", "client/default");
        return "client/index";
    }

}
