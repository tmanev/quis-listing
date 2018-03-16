package com.manev.quislisting.security;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.security.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.Cookie;

/**
 * Custom extension of {@link SignInAdapter} in order to support social signin.
 */
public class CustomSignInAdapter implements SignInAdapter {

    private final Logger log = LoggerFactory.getLogger(CustomSignInAdapter.class);

    private final UserDetailsService userDetailsService;
    private final QuisListingProperties quisListingProperties;
    private final TokenProvider tokenProvider;

    public CustomSignInAdapter(UserDetailsService userDetailsService,
                               QuisListingProperties quisListingProperties, TokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.quisListingProperties = quisListingProperties;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(userId);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            String jwt = tokenProvider.createToken(authenticationToken, false);
            ServletWebRequest servletWebRequest = (ServletWebRequest) request;
            Cookie authCookie = QlCookieUtil.getAuthCookie(jwt);
            authCookie.setPath("/");
            servletWebRequest.getResponse().addCookie(authCookie);
        } catch (AuthenticationException ae) {
            log.error("Social authentication error");
            log.trace("Authentication exception trace: {}", ae);
        }
        return quisListingProperties.getSocial().getRedirectAfterSignIn();
    }

}
