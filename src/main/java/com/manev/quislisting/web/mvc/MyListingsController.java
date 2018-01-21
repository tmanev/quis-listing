package com.manev.quislisting.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static com.manev.quislisting.web.rest.RestRouter.Mvc.MyListings.BASE;

@Controller
public class MyListingsController extends BaseController {

    @RequestMapping(value = BASE, method = RequestMethod.GET)
    public String showMyListingsPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/my-listings");

        return PAGE_CLIENT_INDEX;
    }

}
