package com.manev.quislisting.security.jwt;

import com.manev.quislisting.service.GeoLocationService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static com.manev.quislisting.config.ThymeleafConfiguration.QL_LANG_KEY;
import static org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class MvcJWTFilter extends GenericFilterBean {

    private static final String QL_AUTH = "ql-auth";
    private final Logger log = LoggerFactory.getLogger(MvcJWTFilter.class);

    private TokenProvider tokenProvider;

    private GeoLocationService geoLocationService;

    MvcJWTFilter(TokenProvider tokenProvider, GeoLocationService geoLocationService) {
        this.tokenProvider = tokenProvider;
        this.geoLocationService = geoLocationService;
    }

    private static String getRemoteIp(HttpServletRequest req) {
        String ip = req.getRemoteAddr();

        if (req.getHeader("X-Forwarded-For") != null) {
            String[] headerParts = req.getHeader("X-Forwarded-For").split(",");

            if (headerParts.length > 0 && headerParts[0] != null) {
                ip = headerParts[0];
            }

        } else if (req.getHeader("X-Real-IP") != null) {
            ip = req.getHeader("X-Real-IP");
        }

        return ip;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            resolveLanguage(httpServletRequest, servletResponse);
            String jwt = resolveToken(httpServletRequest);
            if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
                Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ExpiredJwtException eje) {
            log.info("Security exception for user {} - {}", eje.getClaims().getSubject(), eje.getMessage());
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            Cookie cookie = new Cookie(QL_AUTH, null);
            cookie.setMaxAge(0);
            httpServletResponse.addCookie(cookie); // invalidate auth cookie

            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void resolveLanguage(HttpServletRequest request, ServletResponse servletResponse) {
        Cookie qlLocaleCookie = WebUtils.getCookie(request, QL_LANG_KEY);
        String languageToken = qlLocaleCookie != null ? qlLocaleCookie.getValue() : null;

        if (languageToken == null || !StringUtils.hasText(languageToken)) {
            log.info("No language is configured in cookie");
            // set the language cookie based on the ip location
            String remoteIp = getRemoteIp(request);
            log.info("Remote IP is {}", remoteIp);
            String countryIso = geoLocationService.countryIsoFromIp(remoteIp);
            log.info("Country iso is: {}", countryIso);
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            if (countryIso != null) {
                request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, new Locale(countryIso));
                Cookie qlLangKeyCookie = new Cookie(QL_LANG_KEY, countryIso);
                qlLangKeyCookie.setPath("/");
                httpServletResponse.addCookie(qlLangKeyCookie);
            } else {
                request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, new Locale("en"));
                Cookie qlLangKeyCookie = new Cookie(QL_LANG_KEY, "en");
                qlLangKeyCookie.setPath("/");
                httpServletResponse.addCookie(qlLangKeyCookie);
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie qlAuthCookie = WebUtils.getCookie(request, QL_AUTH);
        String bearerCookieToken = qlAuthCookie != null ? qlAuthCookie.getValue() : null;
        if (bearerCookieToken != null && StringUtils.hasText(bearerCookieToken) && bearerCookieToken.startsWith("Bearer:")) {
            return bearerCookieToken.substring(7, bearerCookieToken.length());
        }

        return null;
    }
}
