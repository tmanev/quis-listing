package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping(MvcRouter.SEARCH)
public class SearchController extends BaseController {

    @Autowired
    private DlCategoryService dlCategoryService;
    @Autowired
    private DlLocationService dlLocationService;

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
