package com.manev.quislisting.web.mvc.mylistings;

import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.web.mvc.BaseController;
import com.manev.quislisting.web.mvc.MvcRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Controller
public class AddListingStep3Controller extends BaseController {

    @Autowired
    private DlListingService dlListingService;

    @Autowired
    private DlLocationService dlLocationService;

    @RequestMapping(value = MvcRouter.MyListings.ADD_LISTING_STEP_3, method = RequestMethod.GET)
    public String showAddListingStep3(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) throws UnsupportedEncodingException {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();

        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id));

        if (dlListingDTO == null) {
            return redirectToPageNotFound();
        }

        modelMap.addAttribute("dlLocationCountries", dlLocationService.findAllByParentId(null, language));
        modelMap.addAttribute("dlListingDTO", dlListingDTO);
        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.add_listing.title", null, locale));

        fillListingLocationsMode(modelMap, dlListingDTO, language);

        modelMap.addAttribute("view", "client/my-listings/add-listing-step-3");

        return PAGE_CLIENT_INDEX;
    }

}
