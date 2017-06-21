package com.manev.quislisting.web;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.exception.MissingConfigurationException;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.security.jwt.TokenProvider;
import com.manev.quislisting.web.rest.vm.LoginVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Locale;

import static com.manev.quislisting.security.jwt.JWTConfigurer.AUTHORIZATION_HEADER;

@Controller
@RequestMapping("/sign-in")
public class SignInController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SignInController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final MessageSource messageSource;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public SignInController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                            TokenProvider tokenProvider, AuthenticationManager authenticationManager,
                            LanguageRepository languageRepository, LocaleResolver localeResolver,
                            LanguageTranslationRepository languageTranslationRepository,
                            PostRepository<AbstractPost> postRepository, MessageSource messageSource) {
        super(navMenuRepository, qlConfigRepository, languageRepository, languageTranslationRepository, localeResolver,
                postRepository);
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, Locale locale) {
        model.addAttribute("title", "Sign In");
        model.addAttribute("view", "client/signin");

        QlConfig forgotPasswordPageIdConfig = qlConfigRepository.findOneByKey("forgot-password-page-id");
        if (forgotPasswordPageIdConfig == null) {
            throw new MissingConfigurationException("Forgot Password Page configuration expected");
        }
        AbstractPost forgotPasswordPage = retrievePage(locale.getLanguage(), forgotPasswordPageIdConfig.getValue());
        model.addAttribute("forgotPasswordName", forgotPasswordPage.getName());
        return "client/index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String authorize(@Valid @ModelAttribute("loginVM") LoginVM loginVM, HttpServletRequest request,
                            HttpServletResponse response, ModelMap model) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        model.addAttribute("title", "Sign In");
        model.addAttribute("view", "client/signin");
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(AUTHORIZATION_HEADER, "Bearer " + jwt);
            response.addCookie(new Cookie("ql-auth", "Bearer:" + jwt));

            redirectStrategy.sendRedirect(request, response, "/");

        } catch (AuthenticationException exception) {
            logger.warn("User : {} cannot login. Reason : {}", loginVM.getUsername(), exception.getMessage());
            // send message login error
            model.addAttribute("errMsg", messageSource.getMessage("page.signin.error.wrong_email_password",null, locale));
        }
        return "client/index";
    }

}
