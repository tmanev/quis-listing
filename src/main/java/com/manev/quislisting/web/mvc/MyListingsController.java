package com.manev.quislisting.web.mvc;

import com.manev.quislisting.web.model.JsTranslations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class MyListingsController extends BaseController {

    private static final String PAGE_MY_LISTINGS_NOTIFICATIONS_DELETE_LISTING_SUCCESS = "page.my_listings.notifications.delete_listing_success";

    @RequestMapping(value = MvcRouter.MyListings.BASE, method = RequestMethod.GET)
    public String showMyListingsPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        JsTranslations jsTranslations = new JsTranslations();
        jsTranslations.addTranslation(PAGE_MY_LISTINGS_NOTIFICATIONS_DELETE_LISTING_SUCCESS,
                messageSource.getMessage(PAGE_MY_LISTINGS_NOTIFICATIONS_DELETE_LISTING_SUCCESS, null, locale));

        modelMap.addAttribute("jsTranslations", jsTranslations.getTranslations());

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/my-listings");

        return PAGE_CLIENT_INDEX;
    }

}
