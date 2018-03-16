package com.manev.quislisting.security;

import com.manev.quislisting.security.jwt.ApiJWTFilter;

import javax.servlet.http.Cookie;

public class QlCookieUtil {
    private QlCookieUtil() {
    }

    public static Cookie getAuthCookie(String token) {
        return new Cookie(ApiJWTFilter.QL_AUTH, "Bearer:" + token);
    }
}
