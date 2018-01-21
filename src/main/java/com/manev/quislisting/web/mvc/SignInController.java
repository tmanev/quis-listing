package com.manev.quislisting.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class SignInController extends BaseController {

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
