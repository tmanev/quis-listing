package com.manev.quislisting.web.mvc;

import com.manev.quislisting.repository.model.CategoryCount;
import com.manev.quislisting.service.mapper.DlListingDtoToDlListingBaseMapper;
import com.manev.quislisting.service.model.DlListingBaseModel;
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
@RequestMapping(MvcRouter.HOME)
public class HomeController extends BaseController {

    @Autowired
    private DlCategoryService dlCategoryService;
    @Autowired
    private DlListingService dlListingService;
    @Autowired
    private DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, final HttpServletRequest request) {
        final Locale locale = localeResolver.resolveLocale(request);
        final String title = messageSource.getMessage("page.home.title", null, locale);

        final Page<DlListingDTO> page = dlListingService.findAllForFrontPage(new PageRequest(0, 12), locale.getLanguage());
        final Page<DlListingBaseModel> dlListingBaseModels = page.map(dlListingDTO -> dlListingDtoToDlListingBaseMapper.convert(dlListingDTO, new DlListingBaseModel(),
                locale.getLanguage()));

        model.addAttribute("dlListings", dlListingBaseModels.getContent());
        model.addAttribute("totalDlListings", page.getTotalElements());
        model.addAttribute("loadedDlListings", page.getNumberOfElements());
        final List<CategoryCount> allCategoriesWithCount = dlCategoryService.findAllCategoriesWithCount();
        model.addAttribute("dlCategories", allCategoriesWithCount);

        model.addAttribute("title", title);
        model.addAttribute("view", "client/default");

        return "client/index";
    }

}
