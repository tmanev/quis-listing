package com.manev.quislisting.web.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.service.mapper.DlListingDtoToDlListingBaseMapper;
import com.manev.quislisting.service.model.DlListingBaseModel;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.web.rest.filter.DlListingSearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
    @Autowired
    private DlListingService dlListingService;
    @Autowired
    private DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, final @RequestParam(required = false) String query,
            final HttpServletRequest request) throws IOException {
        final Locale locale = localeResolver.resolveLocale(request);
        final String title = messageSource.getMessage("page.search.title", null, locale);

        final Map<DlCategory, List<DlCategory>> dlCategories = dlCategoryService
                .findAllByLanguageCodeGrouped(locale.getLanguage());
        model.addAttribute("dlCategoriesGrouped", dlCategories);

        // find countries
        final List<DlLocationDTO> allByParentId = dlLocationService.findAllByParentId(null, locale.getLanguage());
        model.addAttribute("dlLocationCountries", allByParentId);

        model.addAttribute("dlListings", new ArrayList<>());
        model.addAttribute("totalDlListings", 0);
        model.addAttribute("loadedDlListings", 0);
        if (query!=null) {
            final ObjectMapper mapper = new ObjectMapper();
            final DlListingSearchFilter dlListingSearchFilter = mapper
                    .readValue(URLDecoder.decode(query, "UTF-8"), DlListingSearchFilter.class);

            if (dlListingSearchFilter.getLanguageCode() == null) {
                dlListingSearchFilter.setLanguageCode(locale.getLanguage());
            }
            final Page<DlListingDTO> page = dlListingService.search(dlListingSearchFilter, new PageRequest(0, 8));
            final Page<DlListingBaseModel> dlListingBaseModels = page
                    .map(dlListingDTO -> dlListingDtoToDlListingBaseMapper.convert(dlListingDTO, new DlListingBaseModel(),
                    locale.getLanguage()));
            model.addAttribute("dlListings", dlListingBaseModels.getContent());
            model.addAttribute("totalDlListings", page.getTotalElements());
            model.addAttribute("loadedDlListings", page.getNumberOfElements());
        }

        model.addAttribute("title", title);
        model.addAttribute("view", "client/search");

        return "client/index";
    }

}
