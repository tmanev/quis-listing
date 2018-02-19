package com.manev.quislisting.web.mvc;

import com.manev.quislisting.service.SocialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Rest controller for supporting social signin.
 */
@RestController
@EnableSocial
@RequestMapping(MvcRouter.SOCIAL)
public class SocialController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(SocialController.class);

    private final SocialService socialService;

    private final ProviderSignInUtils providerSignInUtils;

    public SocialController(final SocialService socialService, final ProviderSignInUtils providerSignInUtils) {
        this.socialService = socialService;
        this.providerSignInUtils = providerSignInUtils;
    }

    @GetMapping(MvcRouter.SIGNUP)
    public RedirectView signUp(final WebRequest webRequest) {

        try {
            final Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);
            socialService.createSocialUser(connection, "en");
            return new RedirectView(URIBuilder.fromUri("/#/social-register/" + connection.getKey().getProviderId())
                    .queryParam("success", "true")
                    .build().toString(), true);
        } catch (final Exception e) {
            log.error("Exception creating social user: ", e);
            return new RedirectView(URIBuilder.fromUri("/#/social-register/no-provider")
                    .queryParam("success", "false")
                    .build().toString(), true);
        }
    }
}
