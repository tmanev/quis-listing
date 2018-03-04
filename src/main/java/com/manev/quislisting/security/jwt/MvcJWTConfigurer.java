package com.manev.quislisting.security.jwt;

import com.manev.quislisting.service.GeoLocationService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class MvcJWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;

    private GeoLocationService geoLocationService;

    public MvcJWTConfigurer(TokenProvider tokenProvider, GeoLocationService geoLocationService) {
        this.tokenProvider = tokenProvider;
        this.geoLocationService = geoLocationService;
    }

    @Override
    public void configure(HttpSecurity http) {
        MvcJWTFilter customFilter = new MvcJWTFilter(tokenProvider, geoLocationService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
