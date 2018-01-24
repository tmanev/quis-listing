package com.manev.quislisting.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping(MvcRouter.FORGOT_PASS)
public class ForgotPasswordController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute("title", messageSource.getMessage("page.forgot_password.title", null, locale));
        modelMap.addAttribute("view", "client/forgot-password");

        return "client/index";
    }

}
