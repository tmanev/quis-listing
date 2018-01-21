package com.manev.quislisting.web.mvc;

import com.manev.quislisting.repository.model.CategoryCount;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    @Autowired
    private DlCategoryService dlCategoryService;
    @Autowired
    private DlListingService dlListingService;

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
