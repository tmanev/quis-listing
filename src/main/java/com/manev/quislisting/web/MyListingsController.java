package com.manev.quislisting.web;

import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.post.AbstractPostService;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping(value = "/my-listings")
public class MyListingsController extends BaseController {

    private final MessageSource messageSource;
    private final DlListingService dlListingService;

    public MyListingsController(NavMenuRepository navMenuRepository,
                                QlConfigService qlConfigService, LanguageRepository languageRepository,
                                LanguageTranslationRepository languageTranslationRepository,
                                LocaleResolver localeResolver, AbstractPostService abstractPostService, MessageSource messageSource, DlListingService dlListingService) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver, abstractPostService);
        this.messageSource = messageSource;
        this.dlListingService = dlListingService;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddListingPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute("title", messageSource.getMessage("page.my_listings.add_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/add-listing");

        return "client/index";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditListingPage(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id));
        modelMap.addAttribute("dlListingDTO", dlListingDTO);

        modelMap.addAttribute("title", messageSource.getMessage("page.my_listings.edit_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/edit-listing");

        return "client/index";
    }
}
