package com.manev.quislisting.web.mvc;

import com.manev.quislisting.service.SocialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Rest controller for supporting social sign-in.
 */
@RestController
@EnableSocial
public class SignUpSocialController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(SignUpSocialController.class);

    private final SocialService socialService;

    private final ProviderSignInUtils providerSignInUtils;

    private final LocaleResolver localeResolver;

    public SignUpSocialController(final SocialService socialService, final ProviderSignInUtils providerSignInUtils, LocaleResolver localeResolver) {
        this.socialService = socialService;
        this.providerSignInUtils = providerSignInUtils;
        this.localeResolver = localeResolver;
    }

    @GetMapping(MvcRouter.Social.SIGN_UP)
    public RedirectView signUp(WebRequest webRequest, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        try {
            final Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);
            socialService.createSocialUser(connection, locale.getLanguage());
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
