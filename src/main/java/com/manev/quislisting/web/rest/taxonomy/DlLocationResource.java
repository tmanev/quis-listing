package com.manev.quislisting.web.rest.taxonomy;

import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static com.manev.quislisting.web.rest.RestRouter.RESOURCE_API_DL_LOCATIONS;

@RestController
@RequestMapping(RESOURCE_API_DL_LOCATIONS)
public class DlLocationResource {

    private final Logger log = LoggerFactory.getLogger(DlLocationResource.class);
    private final DlLocationService dlLocationService;
    private final LocaleResolver localeResolver;

    public DlLocationResource(DlLocationService dlLocationService, LocaleResolver localeResolver) {
        this.dlLocationService = dlLocationService;
        this.localeResolver = localeResolver;
    }

    @GetMapping
    public ResponseEntity<List<DlLocationDTO>> getAllDlLocations(@RequestParam String parentId,
                                                                 HttpServletRequest request) {
        log.debug("REST request to get a page of DlLocationDTO");
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        return new ResponseEntity<>(dlLocationService.findAllByParentId(parentId, language), HttpStatus.OK);
    }

}
