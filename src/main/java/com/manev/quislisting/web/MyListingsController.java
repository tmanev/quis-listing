package com.manev.quislisting.web;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.post.AbstractPostService;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping(value = "/my-listings")
public class MyListingsController extends BaseController {

    private final MessageSource messageSource;
    private final DlListingService dlListingService;
    private final DlCategoryService dlCategoryService;
    private final UserService userService;

    public MyListingsController(NavMenuRepository navMenuRepository,
                                QlConfigService qlConfigService, LanguageRepository languageRepository,
                                LanguageTranslationRepository languageTranslationRepository,
                                LocaleResolver localeResolver, AbstractPostService abstractPostService, MessageSource messageSource, DlListingService dlListingService, DlCategoryService dlCategoryService, UserService userService) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver, abstractPostService);
        this.messageSource = messageSource;
        this.dlListingService = dlListingService;
        this.dlCategoryService = dlCategoryService;
        this.userService = userService;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddListingPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        List<DlCategoryDTO> dlCategories = dlCategoryService.findAllByLanguageCode(locale.getLanguage());
        modelMap.addAttribute("dlCategoriesDtoFlat", dlCategories);
        modelMap.addAttribute("title", messageSource.getMessage("page.my_listings.add_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/add-listing");

        return "client/index";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditListingPage(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) throws UnsupportedEncodingException {
        Locale locale = localeResolver.resolveLocale(request);

        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id));

        if (dlListingDTO == null) {
            return redirectToPageNotFound();
        }
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(currentUserLogin);

        if (userWithAuthoritiesByLogin.isPresent()){
            if (!dlListingDTO.getAuthor().getId().equals(userWithAuthoritiesByLogin.get().getId())){
                return redirectToPageNotFound();
            }
        } else {
            throw new UsernameNotFoundException("User " + currentUserLogin + " was not found in the " +
                    "database");
        }

        modelMap.addAttribute("dlListingDTO", dlListingDTO);

        List<DlCategoryDTO> dlCategories = dlCategoryService.findAllByLanguageCode(locale.getLanguage());
        modelMap.addAttribute("dlCategoriesDtoFlat", dlCategories);
        modelMap.addAttribute("title", messageSource.getMessage("page.my_listings.edit_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/edit-listing");

        return "client/index";
    }
}
