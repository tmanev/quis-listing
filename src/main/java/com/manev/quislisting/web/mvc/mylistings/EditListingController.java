package com.manev.quislisting.web.mvc.mylistings;

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
import com.manev.quislisting.web.mvc.BaseController;
import com.manev.quislisting.web.mvc.MvcRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.manev.quislisting.security.AuthoritiesConstants.ADMIN;

@Controller
public class EditListingController extends BaseController {

    @Autowired
    private DlListingService dlListingService;
    @Autowired
    private UserService userService;
    @Autowired
    private DlContentFieldService dlContentFieldService;
    @Autowired
    private DlCategoryService dlCategoryService;
    @Autowired
    private DlLocationService dlLocationService;

    @RequestMapping(value = MvcRouter.MyListings.EDIT, method = RequestMethod.GET)
    public String showEditListingPage(@PathVariable String id, final ModelMap modelMap, HttpServletRequest request) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        DlListingDTO dlListingDTO = dlListingService.findOne(Long.valueOf(id));

        if (dlListingDTO == null) {
            return redirectToPageNotFound();
        }
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(currentUserLogin);

        if (userWithAuthoritiesByLogin.isPresent()) {
            if (!dlListingDTO.getAuthor().getId().equals(userWithAuthoritiesByLogin.get().getId()) && !SecurityUtils.isCurrentUserInRole(ADMIN)) {
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

        fillListingLocationsMode(modelMap, dlListingDTO, language);

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_listings.edit_listing.title", null, locale));
        modelMap.addAttribute("view", "client/my-listings/edit-listing");

        return PAGE_CLIENT_INDEX;
    }

}
