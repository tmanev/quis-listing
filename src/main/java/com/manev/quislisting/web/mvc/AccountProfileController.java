package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(MvcRouter.Account.PROFILE)
public class AccountProfileController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request,
                            @RequestParam Map<String, String> allRequestParams) throws UnsupportedEncodingException {
        Locale locale = localeResolver.resolveLocale(request);
        String title = messageSource.getMessage("page.account.profile.title", null, locale);
        model.addAttribute("title", title);

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> oneByLogin = userService.findOneByLogin(currentUserLogin);
        if (oneByLogin.isPresent()) {
            model.addAttribute("user", oneByLogin.get());
            model.addAttribute("view", "client/account/profile");
        } else {
            redirectToPageNotFound();
        }

        return "client/index";
    }

}
