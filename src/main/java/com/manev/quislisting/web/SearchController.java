package com.manev.quislisting.web;

import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.post.StaticPageService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {

    public SearchController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService, LanguageRepository languageRepository, LanguageTranslationRepository languageTranslationRepository, LocaleResolver localeResolver, StaticPageService staticPageService, MessageSource messageSource) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver, staticPageService, messageSource);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String title = messageSource.getMessage("page.search.title", null, locale);
        staticPageService.findOne(2L);
        model.addAttribute("title", title);
        model.addAttribute("view", "client/search");

        return "client/index";
    }

}
