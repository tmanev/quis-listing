package com.manev.quislisting.config;

import com.manev.quislisting.security.*;
import com.manev.quislisting.security.jwt.JWTConfigurer;
import com.manev.quislisting.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    private final SessionRegistry sessionRegistry;

    private final CorsFilter corsFilter;

    private final MvcAuthenticationSuccessHandler quisAuthenticationSuccessHandler;

    public MvcSecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService,
                                    TokenProvider tokenProvider, SessionRegistry sessionRegistry,
                                    CorsFilter corsFilter, MvcAuthenticationSuccessHandler quisAuthenticationSuccessHandler) {

        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.sessionRegistry = sessionRegistry;
        this.corsFilter = corsFilter;
        this.quisAuthenticationSuccessHandler = quisAuthenticationSuccessHandler;
    }

//    @PostConstruct
//    public void init() {
//        try {
//            authenticationManagerBuilder.authenticationProvider(authProvider())
////                .userDetailsService(userDetailsService)
////                    .passwordEncoder(passwordEncoder())
//            ;
//        } catch (Exception e) {
//            throw new BeanInitializationException("Security configuration failed", e);
//        }
//    }


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
//            .authenticationProvider(authProvider())
            .sessionManagement()
            .maximumSessions(32) // maximum number of concurrent sessions for one user
            .sessionRegistry(sessionRegistry)
            .and().and()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(new ContinueEntryPoint("/sign-in"))
//            .authenticationEntryPoint(http401UnauthorizedEntryPoint())
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

//    @Bean
//    public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
//        return new Http401UnauthorizedEntryPoint();
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authProvider()).userDetailsService(userDetailsService)
//                    .passwordEncoder(passwordEncoder())
//        ;
//    }

//    @Bean
//    public DaoAuthenticationProvider authProvider() {
//        CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
