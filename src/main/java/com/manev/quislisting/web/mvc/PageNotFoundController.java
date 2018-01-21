package com.manev.quislisting.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping(value = MvcRouter.PAGE_NOT_FOUND)
public class PageNotFoundController extends BaseController {

    @GetMapping
    public String pageNotFound(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute("title", messageSource.getMessage("page.page_not_found.title", null, locale));
        modelMap.addAttribute("view", "client/page-not-found");

        return "client/index";
    }

}
