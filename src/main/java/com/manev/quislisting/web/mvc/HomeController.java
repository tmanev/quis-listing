package com.manev.quislisting.web.mvc;

import com.manev.quislisting.repository.model.CategoryCount;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.StaticPageService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    private DlCategoryService dlCategoryService;
    private DlListingService dlListingService;

    public HomeController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService,
                          StaticPageService staticPageService, LanguageRepository languageRepository,
                          LocaleResolver localeResolver,
                          LanguageTranslationRepository languageTranslationRepository, MessageSource messageSource, DlCategoryService dlCategoryService, DlListingService dlListingService) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver,
                staticPageService, messageSource);
        this.dlCategoryService = dlCategoryService;
        this.dlListingService = dlListingService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String title = messageSource.getMessage("page.home.title", null, locale);

        Page<DlListingDTO> page = dlListingService.findAllForFrontPage(new PageRequest(0, 12), locale.getLanguage());
        model.addAttribute("dlListings", page.getContent());
        model.addAttribute("totalDlListings", page.getTotalElements());
        model.addAttribute("loadedDlListings", page.getNumberOfElements());
        List<CategoryCount> allCategoriesWithCount = dlCategoryService.findAllCategoriesWithCount();
        model.addAttribute("dlCategories", allCategoriesWithCount);

        model.addAttribute("title", title);
        model.addAttribute("view", "client/default");


        return "client/index";
    }

}
