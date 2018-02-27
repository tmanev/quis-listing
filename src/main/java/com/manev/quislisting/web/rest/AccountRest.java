package com.manev.quislisting.web.rest;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.security.jwt.ApiJWTConfigurer;
import com.manev.quislisting.security.jwt.TokenProvider;
import com.manev.quislisting.service.EmailSendingService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.web.rest.RestRouter.Account;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.vm.ChangePasswordVM;
import com.manev.quislisting.web.rest.vm.KeyAndPasswordVM;
import com.manev.quislisting.web.rest.vm.LoginVM;
import com.manev.quislisting.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
public class AccountRest {

    private final Logger log = LoggerFactory.getLogger(AccountRest.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final EmailSendingService emailSendingService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;
    private LocaleResolver localeResolver;

    public AccountRest(UserRepository userRepository, UserService userService,
                       EmailSendingService emailSendingService,
                       TokenProvider tokenProvider, AuthenticationManager authenticationManager, MessageSource messageSource,
                       LocaleResolver localeResolver, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.emailSendingService = emailSendingService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * POST  /register : register the user.
     *
     * @param managedUserVM the managed user View Model
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the login or e-mail is already in use
     */
    @PostMapping(path = RestRouter.User.REGISTER,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {

        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);

        return userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase())
                .map(user -> new ResponseEntity<>("login already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> userRepository.findOneByEmail(managedUserVM.getEmail())
                        .map(user -> new ResponseEntity<>("e-mail address already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
                        .orElseGet(() -> {
                            User user = userService
                                    .createUser(managedUserVM.getLogin(), managedUserVM.getPassword(),
                                            managedUserVM.getFirstName(), managedUserVM.getLastName(),
                                            managedUserVM.getEmail().toLowerCase(), managedUserVM.getImageUrl(), managedUserVM.getLangKey(), true);

                            emailSendingService.sendActivationEmail(user);
                            return new ResponseEntity<>(HttpStatus.CREATED);
                        })
                );
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @return the ResponseEntity with status 200 (OK) and the activated user in body, or status 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping(RestRouter.User.ACTIVATE)
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return userService.activateRegistration(key)
                .map(user -> new ResponseEntity<String>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping(RestRouter.User.AUTHENTICATE)
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    @PostMapping(RestRouter.User.AUTHENTICATE)
    public ResponseEntity authenticate(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(ApiJWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping(RestRouter.Account.BASE)
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
                .map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping(Account.MOBILE_BASE)
    public ResponseEntity<UserDTO> getAccount(final @RequestParam String login) {
        final Optional<User> userByLogin = userService.getUserWithAuthoritiesByLogin(login);
        return userByLogin.map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) or 500 (Internal Server Error) if the user couldn't be updated
     */
    @PostMapping(RestRouter.Account.BASE)
    public ResponseEntity<String> saveAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request,
                                              HttpServletResponse response) {
        Locale locale = localeResolver.resolveLocale(request);
        Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userDTO.getLogin()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
        }
        return userRepository
                .findOneByLogin(SecurityUtils.getCurrentUserLogin())
                .map(u -> {
                    User user = userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(),
                            userDTO.getLangKey(), userDTO.getUpdates());
                    response.addHeader("ql-locale-header", user.getLangKey());
                    return new ResponseEntity<>(messageSource.getMessage("info.save_success", null, locale),
                            HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @PostMapping(path = RestRouter.Account.CHANGE_PASS,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity changePassword(@RequestBody @Valid ChangePasswordVM password, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        Optional<User> oneByLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (oneByLogin.isPresent()) {
            String oldPassFromDb = oneByLogin.get().getPassword();

            if (!passwordEncoder.matches(password.getOldPassword(), oldPassFromDb)) {
                return new ResponseEntity<>(messageSource.getMessage("page.account.profile.incorrect_old_password", null,
                        locale), HttpStatus.BAD_REQUEST);
            }
            if (!password.getNewPassword().equals(password.getNewPasswordRepeat())) {
                return new ResponseEntity<>(messageSource.getMessage("info.save_success", null,
                        locale), HttpStatus.BAD_REQUEST);
            }
            userService.changePassword(password.getNewPassword());
            log.debug("Changed password for User: {}", oneByLogin.get());
        }

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * POST   /account/reset_password/init : Send an e-mail to reset the password of the user
     *
     * @param mail the mail of the user
     * @return the ResponseEntity with status 200 (OK) if the e-mail was sent, or status 400 (Bad Request) if the e-mail address is not registered
     */
    @PostMapping(path = RestRouter.Account.RESET_PASS_INIT,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        return userService.requestPasswordReset(mail)
                .map(user -> {
                    emailSendingService.sendPasswordResetMail(user);
                    return new ResponseEntity<>(messageSource.getMessage("account.reset_password.email_was_sent", null,
                            locale), HttpStatus.OK);
                }).orElse(new ResponseEntity<>(messageSource.getMessage("account.reset_password.email_address_not_registered", null,
                        locale), HttpStatus.BAD_REQUEST));
    }

    /**
     * POST   /account/reset_password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been reset,
     * or status 400 (Bad Request) or 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = RestRouter.Account.RESET_PASS_FINISH,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword,
                                                      HttpServletRequest request) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        Locale locale = localeResolver.resolveLocale(request);
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
                .map(user -> new ResponseEntity<>(messageSource.getMessage("account.reset_password.success", null,
                        locale), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH);
    }
}
