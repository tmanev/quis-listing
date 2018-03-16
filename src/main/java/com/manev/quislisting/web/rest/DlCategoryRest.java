package com.manev.quislisting.web.rest;

import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DlCategoryRest {

    private final Logger log = LoggerFactory.getLogger(DlLocationRest.class);
    private final DlCategoryService dlCategoryService;

    public DlCategoryRest(final DlCategoryService dlCategoryService) {
        this.dlCategoryService = dlCategoryService;
    }

    @GetMapping(RestRouter.DlCategory.LIST)
    public ResponseEntity<Map<DlCategory, List<DlCategory>>> getAllDlCategories(final @RequestParam(required = false, defaultValue = "en")
            String languageCode) {
        log.debug("REST request to get a page of DlCategoryDTO");
        return new ResponseEntity<>(dlCategoryService.findAllByLanguageCodeGrouped(languageCode), HttpStatus.OK);
    }

}
