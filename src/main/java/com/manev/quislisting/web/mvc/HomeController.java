package com.manev.quislisting.web.mvc;

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
@RequestMapping("/")
public class HomeController extends BaseController {

    public HomeController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService,
                          StaticPageService staticPageService, LanguageRepository languageRepository,
                          LocaleResolver localeResolver,
                          LanguageTranslationRepository languageTranslationRepository, MessageSource messageSource) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver,
                staticPageService, messageSource);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String title = messageSource.getMessage("page.home.title", null, locale);

        model.addAttribute("title", title);
        model.addAttribute("view", "client/default");

        return "client/index";
    }

}
