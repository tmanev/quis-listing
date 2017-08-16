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
@RequestMapping("/forgot-password")
public class ForgotPasswordController extends BaseController {

    public ForgotPasswordController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService, LanguageRepository languageRepository, LanguageTranslationRepository languageTranslationRepository, LocaleResolver localeResolver, StaticPageService staticPageService, MessageSource messageSource) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver, staticPageService, messageSource);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute("title", messageSource.getMessage("page.forgot_password.title", null, locale));
        modelMap.addAttribute("view", "client/forgot-password");

        return "client/index";
    }


}
