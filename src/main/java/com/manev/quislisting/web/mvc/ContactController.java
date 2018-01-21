package com.manev.quislisting.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping(value = "/contact")
public class ContactController extends BaseController {

    @GetMapping
    public String contactPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute("title", messageSource.getMessage("page.contact.title", null, locale));
        modelMap.addAttribute("view", "client/contacts");

        return "client/index";
    }

}
