package com.manev.quislisting.service;

import com.manev.quislisting.domain.Authority;
import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.builder.DlMessageBuilder;
import com.manev.quislisting.repository.DlMessagesOverviewRepository;
import com.manev.quislisting.repository.DlMessagesRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.service.dto.DlMessageDTO;
import com.manev.quislisting.service.dto.builder.DlMessageDTOBuilder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    public static final String EMPTY_SEPARATOR = " ";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String LANG_KEY = "en";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DlMessagesRepository dlMessagesRepository;

    @Autowired
    private DlMessagesOverviewRepository dlMessagesOverviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DlMessageOverview createDlMessageOverview() {
        final DlMessageOverview dlMessageOverview = createDlMessageOverviewEntity();

        dlMessageOverview.setSender(userRepository.findOneByLogin(LOGIN).orElse(null));

        return dlMessagesOverviewRepository.saveAndFlush(dlMessageOverview);
    }

    public DlMessage createDlMessage() {
        final DlMessage dlMessage = createDlMessageEntity();
        dlMessage.setSender(userRepository.findOneByLogin(LOGIN).orElse(null));

        return dlMessagesRepository.saveAndFlush(dlMessage);
    }

    public static DlMessageOverview createDlMessageOverviewEntity() {
        return DlMessageBuilder.dlMessage()
                .withReceiver(RECEIVER)
                .withText(TEXT)
                .withListingId(LISTING_ID)
                .withCreatedDate(ZonedDateTime.now())
                .buildDlMessageOverview();
    }

    public static DlMessage createDlMessageEntity() {
        return DlMessageBuilder.dlMessage()
                .withReceiver(RECEIVER)
                .withText(TEXT)
                .withListingId(LISTING_ID)
                .withCreatedDate(ZonedDateTime.now())
                .buildDlMessage();
    }

    public DlMessageDTO createDlMessageDTO() {
        final User sender = userRepository.findOneByLogin(LOGIN).orElse(null);

        final DlMessageDTO dlMessageDTO = DlMessageDTOBuilder.dlMessageDTO()
                .withSenderId(sender.getId())
                .withSenderName(sender.getFirstName() + EMPTY_SEPARATOR + sender.getLastName())
                .withSenderEmail(sender.getLogin())
                .withReceiverId(RECEIVER)
                .withText(TEXT)
                .withCreatedDate(DateTimeFormatter.ofPattern(DATE_PATTERN).format(ZonedDateTime.now()))
                .build();

        return dlMessageDTO;
    }

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
