package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.post.StaticPageService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/password-reset")
public class PasswordResetFinishController extends BaseController {

    private final UserService userService;

    public PasswordResetFinishController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService, LanguageRepository languageRepository, LanguageTranslationRepository languageTranslationRepository, LocaleResolver localeResolver, StaticPageService staticPageService, MessageSource messageSource, UserService userService) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver, staticPageService, messageSource);
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request,
                            @RequestParam Map<String, String> allRequestParams) throws UnsupportedEncodingException {
        Locale locale = localeResolver.resolveLocale(request);
        String title = messageSource.getMessage("page.password_reset.title", null, locale);
        model.addAttribute("title", title);
        if (handleResetPasswordFinishPage(model, allRequestParams)) return redirectToPageNotFound();

        return "client/index";
    }

    private boolean handleResetPasswordFinishPage(ModelMap modelMap, @RequestParam Map<String, String> allRequestParams) {
        String key = allRequestParams.get("key");
        if (key == null) {
            return true;
        } else {
            Optional<User> oneByResetKey = userService.findOneByResetKey(key);
            if (!oneByResetKey.isPresent()) {
                return true;
            }
            modelMap.addAttribute("user", new User());
            modelMap.addAttribute("view", "client/account/password-reset-finish");
        }
        return false;
    }

}
