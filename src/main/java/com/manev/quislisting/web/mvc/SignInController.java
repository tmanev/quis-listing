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
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class SignInController extends BaseController {

    public SignInController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService,
                            LanguageRepository languageRepository, LocaleResolver localeResolver,
                            LanguageTranslationRepository languageTranslationRepository,
                            StaticPageService staticPageService, MessageSource messageSource) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver,
                staticPageService, messageSource);
    }

    @RequestMapping(value = "/sign-in")
    public String indexPage(final ModelMap model, Locale locale, HttpServletRequest request) {
        String error = request.getParameter("error");
        if (error != null) {
            model.addAttribute("errMsg", messageSource.getMessage("page.signin.error.wrong_email_password", null, locale));
        }
        model.addAttribute("title", "Sign In");
        model.addAttribute("view", "client/signin");

        return "client/index";
    }

}
