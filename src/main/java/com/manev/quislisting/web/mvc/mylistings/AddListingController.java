package com.manev.quislisting.web.mvc.mylistings;

import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.web.mvc.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static com.manev.quislisting.web.rest.RestRouter.Mvc.MyListings.ADD;

@Controller
public class AddListingController extends BaseController {

    @Autowired
    private DlCategoryService dlCategoryService;

    @RequestMapping(value = ADD, method = RequestMethod.GET)
    public String showAddListingPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        List<DlCategoryDTO> dlCategories = dlCategoryService.findAllByLanguageCode(locale.getLanguage());
        modelMap.addAttribute("dlCategoriesDtoFlat", dlCategories);
        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.add_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/add-listing");

        return PAGE_CLIENT_INDEX;
    }

}
