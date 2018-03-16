package com.manev.quislisting.config;

import com.manev.quislisting.repository.CustomSocialUsersConnectionRepository;
import com.manev.quislisting.repository.SocialUserConnectionRepository;
import com.manev.quislisting.security.CustomSignInAdapter;
import com.manev.quislisting.security.jwt.TokenProvider;
import com.manev.quislisting.web.mvc.MvcRouter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

/**
 * Basic Spring Social configuration.
 *
 * <p>
 * Creates the beans necessary to manage Connections to social services and
 * link accounts from those services to internal Users.
 */
@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {

    private final Logger log = LoggerFactory.getLogger(SocialConfiguration.class);

    private final SocialUserConnectionRepository socialUserConnectionRepository;

    private final Environment environment;

    public SocialConfiguration(final SocialUserConnectionRepository socialUserConnectionRepository,
            final Environment environment) {

        this.socialUserConnectionRepository = socialUserConnectionRepository;
        this.environment = environment;
    }

    @Bean
    public ConnectController connectController(final ConnectionFactoryLocator connectionFactoryLocator,
            final ConnectionRepository connectionRepository) {

        final ConnectController controller = new ConnectController(connectionFactoryLocator, connectionRepository);
        controller.setApplicationUrl(environment.getProperty("spring.application.url"));
        return controller;
    }

    @Override
    public void addConnectionFactories(final ConnectionFactoryConfigurer connectionFactoryConfigurer,
            final Environment environment) {
        final String googleClientId = environment.getProperty("security.oauth2.client.clientId");
        final String googleClientSecret = environment.getProperty("security.oauth2.client.clientSecret");
        if (StringUtils.isNotBlank(googleClientId) && StringUtils.isNotBlank(googleClientSecret)) {
            log.debug("Configuring GoogleConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                    new GoogleConnectionFactory(
                            googleClientId,
                            googleClientSecret
                    )
            );
        } else {
            log.error("Cannot configure GoogleConnectionFactory id or secret null");
        }
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(
            final ConnectionFactoryLocator connectionFactoryLocator) {
        return new CustomSocialUsersConnectionRepository(socialUserConnectionRepository, connectionFactoryLocator);
    }

    @Bean
    public SignInAdapter signInAdapter(final UserDetailsService userDetailsService,
            final QuisListingProperties quisListingProperties, final TokenProvider tokenProvider) {
        return new CustomSignInAdapter(userDetailsService, quisListingProperties,
                tokenProvider);
    }

    @Bean
    public ProviderSignInController providerSignInController(final ConnectionFactoryLocator connectionFactoryLocator,
            final UsersConnectionRepository usersConnectionRepository, final SignInAdapter signInAdapter) {
        final ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator,
                usersConnectionRepository, signInAdapter);
        providerSignInController.setSignUpUrl(MvcRouter.Social.SIGN_UP);
        providerSignInController.setApplicationUrl(environment.getProperty("spring.application.url"));
        return providerSignInController;
    }

    @Bean
    public ProviderSignInUtils getProviderSignInUtils(final ConnectionFactoryLocator connectionFactoryLocator,
            final UsersConnectionRepository usersConnectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
    }
}
