package com.manev.quislisting.service;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.Authority;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.repository.AuthorityRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.security.AuthoritiesConstants;
import com.manev.quislisting.service.util.TwitterProfileWithEmail;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Service that holds all the common methods for crud operations for a social user.
 */
@Service
public class SocialService {

    private static final String TWITTER_BASE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String INCLUDE_EMAIL = "?include_email=true";
    private final Logger log = LoggerFactory.getLogger(SocialService.class);
    private final UsersConnectionRepository usersConnectionRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final Environment environment;

    private final QuisListingProperties quisListingProperties;

    public SocialService(final UsersConnectionRepository usersConnectionRepository,
                         final AuthorityRepository authorityRepository, final PasswordEncoder passwordEncoder,
                         final UserRepository userRepository, final Environment environment,
                         final QuisListingProperties quisListingProperties) {
        this.usersConnectionRepository = usersConnectionRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.environment = environment;
        this.quisListingProperties = quisListingProperties;
    }

    public void createSocialUser(final Connection<?> connection, final String langKey) {
        if (connection == null) {
            log.error("Cannot create social user because connection is null");
            throw new IllegalArgumentException("Connection cannot be null");
        }

        final UserProfile userProfile = connection.fetchUserProfile();
        final String providerId = connection.getKey().getProviderId();
        final String imageUrl = connection.getImageUrl();
        final User user = createUserIfNotExist(userProfile, langKey, providerId, imageUrl);
        createSocialConnection(user.getLogin(), connection);
    }

    private User createUserIfNotExist(final UserProfile userProfile, final String langKey, final String providerId,
                                      final String imageUrl) {
        final String email = getLoginDependingOnProviderId(userProfile, providerId);
        String userName = userProfile.getUsername();
        if (!StringUtils.isBlank(userName)) {
            userName = userName.toLowerCase(Locale.ENGLISH);
        }
        if (StringUtils.isBlank(email) && StringUtils.isBlank(userName)) {
            log.error("Cannot create social user because email and login are null");
            throw new IllegalArgumentException("Email and login cannot be null");
        }
        if (StringUtils.isBlank(email) && userRepository.findOneByLogin(userName).isPresent()) {
            log.error("Cannot create social user because email is null and login already exist, login -> {}", userName);
            throw new IllegalArgumentException("Email cannot be null with an existing login");
        }
        if (!StringUtils.isBlank(email)) {
            final Optional<User> user = userRepository.findOneByLogin(email);
            if (user.isPresent()) {
                log.info("User already exist associate the connection to this account");
                return user.get();
            }
        }

        final String encryptedPassword = passwordEncoder.encode(RandomStringUtils.random(10));
        final Set<Authority> authorities = new HashSet<>(1);
        authorities.add(authorityRepository.findOne(AuthoritiesConstants.USER));

        final User newUser = new User();
        newUser.setLogin(email);
        newUser.setEmail(email);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userProfile.getFirstName());
        newUser.setLastName(userProfile.getLastName());
        newUser.setActivated(true);
        newUser.setAuthorities(authorities);
        newUser.setLangKey(langKey);
        newUser.setImageUrl(imageUrl);
        newUser.setUpdates(Boolean.TRUE);

        return userRepository.save(newUser);
    }

    private String getLoginDependingOnProviderId(final UserProfile userProfile, final String providerId) {
        switch (providerId) {
            case "twitter":
                return retrieveTwitterObject();
            default:
                return userProfile.getEmail();
        }
    }

    private String retrieveTwitterObject() {
        final String twitterAppId = environment.getProperty("spring.social.twitter.app-id");
        final String twitterAppSecret = environment.getProperty("spring.social.twitter.app-secret");
        final String twitterAccessToken = quisListingProperties.getTwitter().getAccessToken();
        final String twitterAccessTokenSecret = quisListingProperties.getTwitter().getAccessTokenSecret();

        if (StringUtils.isNoneBlank(twitterAppId, twitterAppSecret, twitterAccessToken, twitterAccessTokenSecret)) {
            final TwitterTemplate twitterTemplate = new TwitterTemplate(twitterAppId, twitterAppSecret,
                    twitterAccessToken,
                    twitterAccessTokenSecret);
            final RestTemplate restTemplate = twitterTemplate.getRestTemplate();
            final TwitterProfileWithEmail response = restTemplate.getForObject(TWITTER_BASE_URL + INCLUDE_EMAIL,
                    TwitterProfileWithEmail.class);

            return response.getEmail();
        }

        return StringUtils.EMPTY;
    }

    private void createSocialConnection(final String login, final Connection<?> connection) {
        final ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(login);
        connectionRepository.addConnection(connection);
    }

    @PostConstruct
    private void init() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        final String[] fieldsToMap = {
                "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices", "education",
                "email", "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown",
                "inspirational_people", "installed", "install_type", "is_verified", "languages", "last_name",
                "link", "locale", "location", "meeting_for", "middle_name", "name", "name_format", "political",
                "quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings",
                "significant_other", "sports", "test_group", "timezone", "third_party_id", "updated_time",
                "verified", "viewer_can_send_gift", "website", "work"
        };

        final Field field = Class.forName("org.springframework.social.facebook.api.UserOperations").getDeclaredField("PROFILE_FIELDS");
        field.setAccessible(true);

        final Field modifiers = field.getClass().getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, fieldsToMap);
    }
}
