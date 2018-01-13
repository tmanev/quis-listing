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

import static com.manev.quislisting.config.ThymeleafConfiguration.QUIS_LISTING_LOCALE_COOKIE;
import static org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class JWTFilter extends GenericFilterBean {

    public static final String QL_AUTH = "ql-auth";
    private final Logger log = LoggerFactory.getLogger(JWTFilter.class);

    private TokenProvider tokenProvider;

    private GeoLocationService geoLocationService;

    public JWTFilter(TokenProvider tokenProvider, GeoLocationService geoLocationService) {
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
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.addCookie(new Cookie(QL_AUTH, null)); // invalidate auth cookie

            if (httpServletRequest.getRequestURL().toString().contains("/api")
                    && !httpServletRequest.getRequestURL().toString().contains("/api/public")) {
                ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    private void resolveLanguage(HttpServletRequest request, ServletResponse servletResponse) {
        Cookie qlLocaleCookie = WebUtils.getCookie(request, QUIS_LISTING_LOCALE_COOKIE);
        String languageToken = qlLocaleCookie != null ? qlLocaleCookie.getValue() : null;

        if (languageToken == null | !StringUtils.hasText(languageToken)) {
            log.info("No language is configured in cookie");
            // set the language cookie based on the ip location
            String remoteIp = getRemoteIp(request);
            log.info("Remote IP is {}", remoteIp);
            String countryIso = geoLocationService.countryIsoFromIp(remoteIp);
            log.info("Country iso is: {}", countryIso);
            if (countryIso != null) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, new Locale(countryIso));
                httpServletResponse.addCookie(new Cookie(QUIS_LISTING_LOCALE_COOKIE, countryIso));
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        Cookie qlAuthCookie = WebUtils.getCookie(request, QL_AUTH);
        String bearerCookieToken = qlAuthCookie != null ? qlAuthCookie.getValue() : null;
        if (bearerCookieToken != null && StringUtils.hasText(bearerCookieToken) && bearerCookieToken.startsWith("Bearer:")) {
            return bearerCookieToken.substring(7, bearerCookieToken.length());
        }

        return null;
    }
}
