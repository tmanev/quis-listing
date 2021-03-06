package com.manev.quislisting.web.rest;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.Authority;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.repository.AuthorityRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.security.AuthoritiesConstants;
import com.manev.quislisting.security.jwt.TokenProvider;
import com.manev.quislisting.service.EmailSendingService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.web.rest.vm.ManagedUserVM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class AccountRestTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService mockUserService;

    @Mock
    private EmailSendingService mockEmailSendingService;

    @Mock
    private MessageSource mockMessageSource;

    @Mock
    private LocaleResolver mockLocaleResolver;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc restUserMockMvc;

    private MockMvc restMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(mockEmailSendingService).sendActivationEmail((User) anyObject());

        AccountRest accountResource =
                new AccountRest(userRepository, userService, mockEmailSendingService, tokenProvider, authenticationManager, mockMessageSource, mockLocaleResolver, passwordEncoder);

        AccountRest accountUserMockResource =
                new AccountRest(userRepository, mockUserService, mockEmailSendingService, tokenProvider, authenticationManager, mockMessageSource, mockLocaleResolver, passwordEncoder);

        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
    }

    @Test
    public void testNonAuthenticatedUser() throws Exception {
        restUserMockMvc.perform(get(RestRouter.User.AUTHENTICATE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testAuthenticatedUser() throws Exception {
        restUserMockMvc.perform(get(RestRouter.User.AUTHENTICATE)
                .with(request -> {
                    request.setRemoteUser("test");
                    return request;
                })
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("test"));
    }

    @Test
    public void testGetExistingAccount() throws Exception {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);

        User user = new User();
        user.setLogin("test");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setEmail("john.doe@jhipster.com");
        user.setImageUrl("http://placehold.it/50x50");
        user.setAuthorities(authorities);
        when(mockUserService.getUserWithAuthorities()).thenReturn(user);

        restUserMockMvc.perform(get(RestRouter.Account.BASE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.login").value("test"))
                .andExpect(jsonPath("$.firstName").value("john"))
                .andExpect(jsonPath("$.lastName").value("doe"))
                .andExpect(jsonPath("$.email").value("john.doe@jhipster.com"))
                .andExpect(jsonPath("$.imageUrl").value("http://placehold.it/50x50"))
                .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
    }

    @Test
    public void testGetUnknownAccount() throws Exception {
        when(mockUserService.getUserWithAuthorities()).thenReturn(null);

        restUserMockMvc.perform(get(RestRouter.Account.BASE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Transactional
    public void testRegisterValid() throws Exception {
        ManagedUserVM validUser = new ManagedUserVM(
                null,                   // id
                "joe",                  // login
                "password",             // password
                "Joe",                  // firstName
                "Shmoe",                // lastName
                "joe@example.com",      // e-mail
                true,                   // activated
                "http://placehold.it/50x50", //imageUrl
                "en",                   // langKey
                Boolean.TRUE,                   // updates
                null,                   // createdBy
                null,                   // createdDate
                null,                   // lastModifiedBy
                null,                   // lastModifiedDate
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        restMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(validUser)))
                .andExpect(status().isCreated());

        Optional<User> user = userRepository.findOneByLogin("joe");
        assertThat(user.isPresent()).isTrue();
    }

    @Test
    @Transactional
    public void testRegisterInvalidLogin() throws Exception {
        ManagedUserVM invalidUser = new ManagedUserVM(
                null,                   // id
                "funky-log!n",          // login <-- invalid
                "password",             // password
                "Funky",                // firstName
                "One",                  // lastName
                "funky@example.com",    // e-mail
                true,                   // activated
                "http://placehold.it/50x50", //imageUrl
                "en",                   // langKey
                Boolean.TRUE,                   // updates
                null,                   // createdBy
                null,                   // createdDate
                null,                   // lastModifiedBy
                null,                   // lastModifiedDate
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        restUserMockMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(invalidUser)))
                .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByEmail("funky@example.com");
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    @Transactional
    public void testRegisterInvalidEmail() throws Exception {
        ManagedUserVM invalidUser = new ManagedUserVM(
                null,               // id
                "bob",              // login
                "password",         // password
                "Bob",              // firstName
                "Green",            // lastName
                "invalid",          // e-mail <-- invalid
                true,               // activated
                "http://placehold.it/50x50", //imageUrl
                "en",                   // langKey
                Boolean.TRUE,                   // updates
                null,                   // createdBy
                null,                   // createdDate
                null,                   // lastModifiedBy
                null,                   // lastModifiedDate
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        restUserMockMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(invalidUser)))
                .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByLogin("bob");
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    @Transactional
    public void testRegisterInvalidPassword() throws Exception {
        ManagedUserVM invalidUser = new ManagedUserVM(
                null,               // id
                "bob",              // login
                "123",              // password with only 3 digits
                "Bob",              // firstName
                "Green",            // lastName
                "bob@example.com",  // e-mail
                true,               // activated
                "http://placehold.it/50x50", //imageUrl
                "en",                   // langKey
                Boolean.TRUE,                   // updates
                null,                   // createdBy
                null,                   // createdDate
                null,                   // lastModifiedBy
                null,                   // lastModifiedDate
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        restUserMockMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(invalidUser)))
                .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByLogin("bob");
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    @Transactional
    public void testRegisterDuplicateLogin() throws Exception {
        // Good
        ManagedUserVM validUser = new ManagedUserVM(
                null,                   // id
                "alice",                // login
                "password",             // password
                "Alice",                // firstName
                "Something",            // lastName
                "alice@example.com",    // e-mail
                true,                   // activated
                "http://placehold.it/50x50", //imageUrl
                "en",                   // langKey
                Boolean.TRUE,                   // updates
                null,                   // createdBy
                null,                   // createdDate
                null,                   // lastModifiedBy
                null,                   // lastModifiedDate
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        // Duplicate login, different e-mail
        ManagedUserVM duplicatedUser = new ManagedUserVM(validUser.getId(), validUser.getLogin(), validUser.getPassword(), validUser.getLogin(), validUser.getLastName(),
                "alicejr@example.com", true, validUser.getImageUrl(), validUser.getLangKey(), validUser.getUpdates(), validUser.getCreatedBy(), validUser.getCreatedDate(), validUser.getLastModifiedBy(), validUser.getLastModifiedDate(), validUser.getAuthorities());

        // Good user
        restMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(validUser)))
                .andExpect(status().isCreated());

        // Duplicate login
        restMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(duplicatedUser)))
                .andExpect(status().is4xxClientError());

        Optional<User> userDup = userRepository.findOneByEmail("alicejr@example.com");
        assertThat(userDup.isPresent()).isFalse();
    }

    @Test
    @Transactional
    public void testRegisterDuplicateEmail() throws Exception {
        // Good
        ManagedUserVM validUser = new ManagedUserVM(
                null,                   // id
                "john",                 // login
                "password",             // password
                "John",                 // firstName
                "Doe",                  // lastName
                "john@example.com",     // e-mail
                true,                   // activated
                "http://placehold.it/50x50", //imageUrl
                "en",                   // langKey
                Boolean.TRUE,                   // updates
                null,                   // createdBy
                null,                   // createdDate
                null,                   // lastModifiedBy
                null,                   // lastModifiedDate
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));

        // Duplicate e-mail, different login
        ManagedUserVM duplicatedUser = new ManagedUserVM(validUser.getId(), "johnjr", validUser.getPassword(), validUser.getLogin(), validUser.getLastName(),
                validUser.getEmail(), true, validUser.getImageUrl(), validUser.getLangKey(), validUser.getUpdates(), validUser.getCreatedBy(), validUser.getCreatedDate(), validUser.getLastModifiedBy(), validUser.getLastModifiedDate(), validUser.getAuthorities());

        // Good user
        restMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(validUser)))
                .andExpect(status().isCreated());

        // Duplicate e-mail
        restMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(duplicatedUser)))
                .andExpect(status().is4xxClientError());

        Optional<User> userDup = userRepository.findOneByLogin("johnjr");
        assertThat(userDup.isPresent()).isFalse();
    }

    @Test
    @Transactional
    public void testRegisterAdminIsIgnored() throws Exception {
        ManagedUserVM validUser = new ManagedUserVM(
                null,                   // id
                "badguy",               // login
                "password",             // password
                "Bad",                  // firstName
                "Guy",                  // lastName
                "badguy@example.com",   // e-mail
                true,                   // activated
                "http://placehold.it/50x50", //imageUrl
                "en",                   // langKey
                Boolean.TRUE,                   // updates
                null,                   // createdBy
                null,                   // createdDate
                null,                   // lastModifiedBy
                null,                   // lastModifiedDate
                new HashSet<>(Arrays.asList(AuthoritiesConstants.ADMIN)));

        restMvc.perform(
                post(RestRouter.User.REGISTER)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(validUser)))
                .andExpect(status().isCreated());

        Optional<User> userDup = userRepository.findOneByLogin("badguy");
        assertThat(userDup.isPresent()).isTrue();
        assertThat(userDup.get().getAuthorities()).hasSize(1)
                .containsExactly(authorityRepository.findOne(AuthoritiesConstants.USER));
    }

    @Test
    @Transactional
    public void testSaveInvalidLogin() throws Exception {
        UserDTO invalidUser = new UserDTO(
                null,                   // id
                "funky-log!n",          // login <-- invalid
                "Funky",                // firstName
                "One",                  // lastName
                "funky@example.com",    // e-mail
                true,                   // activated
                "http://placehold.it/50x50", //imageUrl
                "en",                   // langKey
                Boolean.TRUE,       // updates
                null,                   // createdBy
                null,                   // createdDate
                null,                   // lastModifiedBy
                null,                   // lastModifiedDate
                new HashSet<>(Arrays.asList(AuthoritiesConstants.USER))
        );

        restUserMockMvc.perform(
                post(RestRouter.Account.BASE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(invalidUser)))
                .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByEmail("funky@example.com");
        assertThat(user.isPresent()).isFalse();
    }
}
