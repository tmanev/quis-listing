package com.manev.quislisting.service;

import com.manev.quislisting.domain.Authority;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.repository.DlMessageOverviewRepository;
import com.manev.quislisting.repository.DlMessagesRepository;
import com.manev.quislisting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Test component for creation of the data needed for testing the rest controller.
 */
@Component
public class DlMessageTestComponent {

    public static final Long RECEIVER = 4L;
    public static final String TEXT = "randomText";
    public static final Long LISTING_ID = 1L;
    public static final String LOGIN = "john@example.com";
    public static final String FIRST_NAME = "john";
    public static final String LAST_NAME = "doe";
    public static final String EMAIL = "john@example.com";
    public static final String IMAGE_URL = "http://placehold.it/50x50";
    public static final String PASSWORD = "pass";
    public static final boolean ACTIVATED = true;
    public static final String LANG_KEY = "en";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DlMessagesRepository dlMessagesRepository;

    @Autowired
    private DlMessageOverviewRepository dlMessagesOverviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(final String login, final String firstName, final String lastName, final String email,
                           final String imageUrl, final String password, final boolean activated, final String authorityName) {
        final User user = new User();
        user.setLogin(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setImageUrl(imageUrl);
        user.setPassword(passwordEncoder.encode(password));
        user.setActivated(activated);

        final Set<Authority> authorities = new HashSet<>();
        final Authority authority = new Authority();
        authority.setName(authorityName);
        authorities.add(authority);

        user.setAuthorities(authorities);

        userRepository.saveAndFlush(user);

        return user;
    }

}
