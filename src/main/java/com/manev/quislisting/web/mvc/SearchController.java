package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.post.StaticPageService;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {

    private final DlCategoryService dlCategoryService;
    private final DlLocationService dlLocationService;

    public SearchController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService, LanguageRepository languageRepository, LanguageTranslationRepository languageTranslationRepository, LocaleResolver localeResolver, StaticPageService staticPageService, MessageSource messageSource, DlCategoryService dlCategoryService, DlLocationService dlLocationService) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver, staticPageService, messageSource);
        this.dlCategoryService = dlCategoryService;
        this.dlLocationService = dlLocationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String title = messageSource.getMessage("page.search.title", null, locale);

        Map<DlCategory, List<DlCategory>> dlCategories = dlCategoryService.findAllByLanguageCodeGrouped(locale.getLanguage());
        model.addAttribute("dlCategoriesGrouped", dlCategories);

        // find countries
        List<DlLocationDTO> allByParentId = dlLocationService.findAllByParentId(null, locale.getLanguage());
        model.addAttribute("dlLocationCountries", allByParentId);

        model.addAttribute("title", title);
        model.addAttribute("view", "client/search");

        return "client/index";
    }

}
