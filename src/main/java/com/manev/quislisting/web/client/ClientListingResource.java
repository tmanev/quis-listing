package com.manev.quislisting.web.client;

import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.post.AbstractPostService;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.ClientDlListingDTO;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_DL_LISTINGS;

@RestController
@RequestMapping(RESOURCE_API_DL_LISTINGS)
public class ClientListingResource {

    private static final String ENTITY_NAME = "DlListing";

    private final DlListingService dlListingService;
    private final QlConfigService qlConfigService;
    private final AbstractPostService abstractPostService;
    private final LocaleResolver localeResolver;

    public ClientListingResource(DlListingService dlListingService, QlConfigService qlConfigService, AbstractPostService abstractPostService, LocaleResolver localeResolver) {
        this.dlListingService = dlListingService;
        this.qlConfigService = qlConfigService;
        this.abstractPostService = abstractPostService;
        this.localeResolver = localeResolver;
    }

}
