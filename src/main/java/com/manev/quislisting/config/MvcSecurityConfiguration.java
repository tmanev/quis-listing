package com.manev.quislisting.config;

import com.manev.quislisting.security.AuthoritiesConstants;
import com.manev.quislisting.security.ContinueEntryPoint;
import com.manev.quislisting.security.MvcAuthenticationSuccessHandler;
import com.manev.quislisting.security.jwt.JWTConfigurer;
import com.manev.quislisting.security.jwt.TokenProvider;
import com.manev.quislisting.service.GeoLocationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(1)
public class MvcSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    private final SessionRegistry sessionRegistry;

    private final CorsFilter corsFilter;

    private final MvcAuthenticationSuccessHandler quisAuthenticationSuccessHandler;

    private final GeoLocationService geoLocationService;

    public MvcSecurityConfiguration(TokenProvider tokenProvider, SessionRegistry sessionRegistry,
                                    CorsFilter corsFilter, MvcAuthenticationSuccessHandler quisAuthenticationSuccessHandler, GeoLocationService geoLocationService) {

        this.tokenProvider = tokenProvider;
        this.sessionRegistry = sessionRegistry;
        this.corsFilter = corsFilter;
        this.quisAuthenticationSuccessHandler = quisAuthenticationSuccessHandler;
        this.geoLocationService = geoLocationService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/bower_components/**")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/test/**")
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .sessionManagement()
                .sessionFixation().none()
                .maximumSessions(32) // maximum number of concurrent sessions for one user
                .sessionRegistry(sessionRegistry)
                .and().and()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new ContinueEntryPoint("/sign-in"))
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/sign-in/**").permitAll()
                .antMatchers("/content/**").permitAll()
                .antMatchers("/my-listings/**").authenticated()
                .antMatchers("/account/**").authenticated()
                .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger-resources/configuration/ui").permitAll()
                .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
                .and()
                .formLogin()

                .successHandler(quisAuthenticationSuccessHandler)

                .loginPage("/sign-in")

                .permitAll()
                .and()
                .logout()
                .permitAll()

                .and()
                .apply(securityConfigurerAdapter());

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider, geoLocationService);
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
