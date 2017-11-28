package com.manev.quislisting.security;

import com.manev.quislisting.security.jwt.JWTFilter;
import com.manev.quislisting.security.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static com.manev.quislisting.config.ThymeleafConfiguration.QUIS_LISTING_LOCALE_COOKIE;
import static com.manev.quislisting.security.jwt.JWTConfigurer.AUTHORIZATION_HEADER;
import static org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME;

@Component
public class MvcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public MvcAuthenticationSuccessHandler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication, isRememberMe(httpServletRequest));
        httpServletResponse.addHeader(AUTHORIZATION_HEADER, "Bearer " + jwt);
        httpServletResponse.addCookie(new Cookie(JWTFilter.QL_AUTH, "Bearer:" + jwt));

        if (authentication.isAuthenticated()) {
            QlUserDetails principal = (QlUserDetails) authentication.getPrincipal();
            httpServletRequest.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, new Locale(principal.getLangKey()));
            httpServletResponse.addCookie(new Cookie(QUIS_LISTING_LOCALE_COOKIE, principal.getLangKey()));
        }

        String redirectUrl = "/";
        String aContinue = httpServletRequest.getParameter("continue");
        if (aContinue != null) {
            redirectUrl = aContinue;
        }
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, redirectUrl);
    }

    private boolean isRememberMe(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("rememberMe") != null
                && httpServletRequest.getParameter("rememberMe").equals("on");
    }
}
