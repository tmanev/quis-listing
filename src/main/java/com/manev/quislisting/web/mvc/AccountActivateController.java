package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.User;
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
@RequestMapping("/account-activate")
public class AccountActivateController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request,
                            @RequestParam Map<String, String> allRequestParams) throws UnsupportedEncodingException {
        Locale locale = localeResolver.resolveLocale(request);
        String title = messageSource.getMessage("page.account.account_activate.title", null, locale);
        model.addAttribute("title", title);
        if (handleActivationLinkPage(model, allRequestParams)) return redirectToPageNotFound();

        return "client/index";
    }

    private boolean handleActivationLinkPage(ModelMap modelMap, @RequestParam Map<String, String> allRequestParams) {
        String key = allRequestParams.get("key");
        if (key == null) {
            return true;
        } else {
            Optional<User> activatedUser = userService.activateRegistration(key);
            if (activatedUser.isPresent()) {
                modelMap.addAttribute("view", "client/account/activation");
            } else {
                return true;
            }
        }
        return false;
    }

}
