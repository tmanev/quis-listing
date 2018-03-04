package com.manev.quislisting.web.mvc.mylistings;

import com.manev.quislisting.service.DlContentFieldService;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
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
import java.util.List;
import java.util.Locale;

@Controller
public class AddListingStep2Controller extends BaseController {

    @Autowired
    private DlListingService dlListingService;

    @Autowired
    private DlContentFieldService dlContentFieldService;

    @RequestMapping(value = MvcRouter.MyListings.ADD_LISTING_STEP_2, method = RequestMethod.GET)
    public String showAddListingStep2(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id));

        List<DlContentFieldDTO> dlContentFieldDTOS = dlContentFieldService.findAllByCategoryId(dlListingDTO.getDlCategories().get(0).getId(), locale.getLanguage());
        modelMap.addAttribute("dlContentFieldsDto", dlContentFieldDTOS);
        modelMap.addAttribute("dlListingDTO", dlListingDTO);
        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.add_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/add-listing-step-2");


        return PAGE_CLIENT_INDEX;
    }

}
