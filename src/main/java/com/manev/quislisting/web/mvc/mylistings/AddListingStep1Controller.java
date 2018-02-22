package com.manev.quislisting.web.mvc.mylistings;

import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.web.mvc.BaseController;
import com.manev.quislisting.web.mvc.MvcRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Controller
public class AddListingStep1Controller extends BaseController {

    @Autowired
    private DlCategoryService dlCategoryService;

    @Autowired
    private DlListingService dlListingService;

    @RequestMapping(value = MvcRouter.MyListings.ADD_LISTING_STEP_1_NEW, method = RequestMethod.GET)
    public String showAddListingNewPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        List<DlCategoryDTO> dlCategories = dlCategoryService.findAllByLanguageCode(locale.getLanguage());
        modelMap.addAttribute("dlListingDTO", new DlListingDTO());
        modelMap.addAttribute("dlCategoriesDtoFlat", dlCategories);
        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.add_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/add-listing-step-1");

        modelMap.addAttribute("jsTranslations", getJsTranslations(locale));

        return PAGE_CLIENT_INDEX;
    }

    @RequestMapping(value = MvcRouter.MyListings.ADD_LISTING_STEP_1, method = RequestMethod.GET)
    public String showAddListingPage(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id));

        List<DlCategoryDTO> dlCategories = dlCategoryService.findAllByLanguageCode(locale.getLanguage());
        modelMap.addAttribute("dlListingDTO", dlListingDTO);
        modelMap.addAttribute("dlCategoriesDtoFlat", dlCategories);
        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.add_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/add-listing-step-1");

        modelMap.addAttribute("jsTranslations", getJsTranslations(locale));

        return PAGE_CLIENT_INDEX;
    }

}
