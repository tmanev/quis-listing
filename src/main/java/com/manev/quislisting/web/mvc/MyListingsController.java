package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.DlContentFieldService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.web.model.JsTranslations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.manev.quislisting.web.rest.RestRouter.Mvc.MyListings.ADD;
import static com.manev.quislisting.web.rest.RestRouter.Mvc.MyListings.BASE;
import static com.manev.quislisting.web.rest.RestRouter.Mvc.MyListings.EDIT;
import static com.manev.quislisting.web.rest.RestRouter.Mvc.MyListings.PREVIEW;
import static com.manev.quislisting.web.rest.RestRouter.Mvc.MyListings.PUBLISH_REQUEST_SUCCESS;

@Controller
public class MyListingsController extends BaseController {

    private static final String ATTRIBUTE_TITLE = "title";
    private static final String PAGE_CLIENT_INDEX = "client/index";

    @Autowired
    private DlListingService dlListingService;
    @Autowired
    private DlCategoryService dlCategoryService;
    @Autowired
    private DlContentFieldService dlContentFieldService;
    @Autowired
    private UserService userService;
    @Autowired
    private DlLocationService dlLocationService;

    @RequestMapping(value = BASE, method = RequestMethod.GET)
    public String showMyListingsPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/my-listings");

        return PAGE_CLIENT_INDEX;
    }

    @RequestMapping(value = ADD, method = RequestMethod.GET)
    public String showAddListingPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        List<DlCategoryDTO> dlCategories = dlCategoryService.findAllByLanguageCode(locale.getLanguage());
        modelMap.addAttribute("dlCategoriesDtoFlat", dlCategories);
        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.add_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/add-listing");

        return PAGE_CLIENT_INDEX;
    }

    @RequestMapping(value = EDIT, method = RequestMethod.GET)
    public String showEditListingPage(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id), language);

        if (dlListingDTO == null) {
            return redirectToPageNotFound();
        }
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(currentUserLogin);

        if (userWithAuthoritiesByLogin.isPresent()) {
            if (!dlListingDTO.getAuthor().getId().equals(userWithAuthoritiesByLogin.get().getId())) {
                return redirectToPageNotFound();
            }
        } else {
            throw new UsernameNotFoundException("User " + currentUserLogin + " was not found in the " +
                    "database");
        }

        modelMap.addAttribute("dlListingDTO", dlListingDTO);

        List<DlContentFieldDTO> dlContentFieldDTOS = dlContentFieldService.findAllByCategoryId(dlListingDTO.getDlCategories().get(0).getId(), locale.getLanguage());
        modelMap.addAttribute("dlContentFieldsDto", dlContentFieldDTOS);
        List<DlCategoryDTO> dlCategories = dlCategoryService.findAllByLanguageCode(locale.getLanguage());
        modelMap.addAttribute("dlCategoriesDtoFlat", dlCategories);
        modelMap.addAttribute("dlLocationCountries", dlLocationService.findAllByParentId(null, language));

        JsTranslations jsTranslations = new JsTranslations();
        jsTranslations.addTranslation("page.my_listings.edit_listing.notifications.publish_validation.title", messageSource.getMessage("page.my_listings.edit_listing.notifications.publish_validation.title", null, locale));
        jsTranslations.addTranslation("page.my_listings.edit_listing.notifications.publish_validation.message", messageSource.getMessage("page.my_listings.edit_listing.notifications.publish_validation.message", null, locale));

        modelMap.addAttribute("jsTranslations", jsTranslations.getTranslations());

        List<DlLocationDTO> dlLocations = dlListingDTO.getDlLocations();
        if (dlLocations != null && !dlLocations.isEmpty()) {
            DlLocationDTO dlLocationDTO = dlLocations.get(0);
            modelMap.addAttribute("dlLocationStates", dlLocationService.findAllByParentId(String.valueOf(dlLocationDTO.getParent().getParent().getId()), language));
            modelMap.addAttribute("dlLocationCities", dlLocationService.findAllByParentId(String.valueOf(dlLocationDTO.getParent().getId()), language));
        } else {
            modelMap.addAttribute("dlLocationStates", Collections.emptyList());
            modelMap.addAttribute("dlLocationCities", Collections.emptyList());
        }

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.edit_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/edit-listing");

        return PAGE_CLIENT_INDEX;
    }

    @RequestMapping(value = PREVIEW, method = RequestMethod.GET)
    public String showPreviewListingPage(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id), language);

        if (dlListingDTO == null) {
            return redirectToPageNotFound();
        }
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(currentUserLogin);

        if (userWithAuthoritiesByLogin.isPresent()) {
            if (!dlListingDTO.getAuthor().getId().equals(userWithAuthoritiesByLogin.get().getId())) {
                return redirectToPageNotFound();
            }
        } else {
            throw new UsernameNotFoundException("User " + currentUserLogin + " was not found in the " +
                    "database");
        }

        List<DlContentFieldDTO> dlContentFieldDTOS = dlContentFieldService.findAllByCategoryId(dlListingDTO.getDlCategories().get(0).getId(), language);

        modelMap.addAttribute("dlContentFieldsDto", dlContentFieldDTOS);

        modelMap.addAttribute("dlListingDTO", dlListingDTO);

        modelMap.addAttribute(ATTRIBUTE_TITLE, dlListingDTO.getTitle());
        modelMap.addAttribute("view", "client/listing");

        return PAGE_CLIENT_INDEX;
    }

    @RequestMapping(value = PUBLISH_REQUEST_SUCCESS, method = RequestMethod.GET)
    public String publishSuccessful(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.publish_successful.title", null, locale));

        modelMap.addAttribute("view", "client/my-listings/publish-successful");

        return PAGE_CLIENT_INDEX;
    }
}
