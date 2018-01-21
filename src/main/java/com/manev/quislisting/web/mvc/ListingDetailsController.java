package com.manev.quislisting.web.mvc;

import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

@Controller
public class ListingDetailsController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(ListingDetailsController.class);

    @Autowired
    private DlListingService dlListingService;

    @RequestMapping(value = MvcRouter.Listings.VIEW, method = RequestMethod.GET)
    public String showEditListingPage(@PathVariable String id, @PathVariable String slug,
                                      final ModelMap modelMap, HttpServletRequest request) throws IOException {
        long start = System.currentTimeMillis();
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id), language);

        if (dlListingDTO == null) {
            return redirectToPageNotFound();
        }

        modelMap.addAttribute("showEditButton", dlListingDTO.getAuthor().getLogin().equals(SecurityUtils.getCurrentUserLogin()));

        modelMap.addAttribute("dlListingDTO", dlListingDTO);

        modelMap.addAttribute("title", dlListingDTO.getTitle());
        modelMap.addAttribute("view", "client/listing");

        log.info("Loading of listing id: {}, name: {}, took: {} ms", dlListingDTO.getId(), dlListingDTO.getName(), System.currentTimeMillis() - start);
        return "client/index";
    }

}
