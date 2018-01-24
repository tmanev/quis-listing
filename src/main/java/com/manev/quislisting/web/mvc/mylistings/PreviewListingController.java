package com.manev.quislisting.web.mvc.mylistings;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
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
import java.util.Locale;
import java.util.Optional;

@Controller
public class PreviewListingController extends BaseController {

    @Autowired
    private DlListingService dlListingService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = MvcRouter.MyListings.PREVIEW, method = RequestMethod.GET)
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

        modelMap.addAttribute("dlListingDTO", dlListingDTO);

        modelMap.addAttribute(ATTRIBUTE_TITLE, dlListingDTO.getTitle());
        modelMap.addAttribute("view", "client/listing");

        return PAGE_CLIENT_INDEX;
    }

}
