package com.manev.quislisting.web.mvc;

import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.DlContentFieldService;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.StaticPageService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

@Controller
@RequestMapping(value = "/listings")
public class ListingsController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(ListingsController.class);

    private final DlListingService dlListingService;
    private final DlCategoryService dlCategoryService;
    private final DlContentFieldService dlContentFieldService;
    private final UserService userService;
    private final DlLocationService dlLocationService;

    public ListingsController(NavMenuRepository navMenuRepository,
                              QlConfigService qlConfigService, LanguageRepository languageRepository,
                              LanguageTranslationRepository languageTranslationRepository,
                              LocaleResolver localeResolver, StaticPageService staticPageService, MessageSource messageSource, DlListingService dlListingService, DlCategoryService dlCategoryService, DlContentFieldService dlContentFieldService, UserService userService, DlLocationService dlLocationService) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver, staticPageService, messageSource);
        this.dlListingService = dlListingService;
        this.dlCategoryService = dlCategoryService;
        this.dlContentFieldService = dlContentFieldService;
        this.userService = userService;
        this.dlLocationService = dlLocationService;
    }

    @RequestMapping(value = "/{id}/{slug}", method = RequestMethod.GET)
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
