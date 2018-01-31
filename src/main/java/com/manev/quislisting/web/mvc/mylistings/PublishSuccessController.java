package com.manev.quislisting.web.mvc.mylistings;

import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.web.mvc.BaseController;
import com.manev.quislisting.web.mvc.MvcRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class PublishSuccessController extends BaseController {

    @Autowired
    private DlListingService dlListingService;

    @RequestMapping(value = MvcRouter.MyListings.PUBLISH_REQUEST_SUCCESS, method = RequestMethod.GET)
    public String publishSuccessful(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.publish_successful.title", null, locale));

        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id), language);
        modelMap.addAttribute("dlListing", dlListingDTO);
        modelMap.addAttribute("view", "client/my-listings/publish-successful");

        return PAGE_CLIENT_INDEX;
    }

}
