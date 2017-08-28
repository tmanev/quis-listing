package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.EmailSendingService;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.post.StaticPageService;
import com.manev.quislisting.web.model.SignUpUserBean;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/sign-up")
public class SignUpController extends BaseController {

    private UserRepository userRepository;
    private UserService userService;
    private EmailSendingService emailSendingService;

    public SignUpController(NavMenuRepository navMenuRepository, QlConfigService qlConfigService,
                            LanguageRepository languageRepository, LanguageTranslationRepository languageTranslationRepository,
                            LocaleResolver localeResolver, MessageSource messageSource,
                            UserRepository userRepository, UserService userService,
                            StaticPageService staticPageService, EmailSendingService emailSendingService) {
        super(navMenuRepository, qlConfigService, languageRepository, languageTranslationRepository, localeResolver,
                staticPageService, messageSource);
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailSendingService = emailSendingService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        setSignUpPageInModel(model, locale);

        setTermsAndConditionPageInModel(model, locale);

        model.addAttribute("signUpUserBean", new SignUpUserBean());

        return "client/index";
    }

    private void setTermsAndConditionPageInModel(ModelMap model, Locale locale) {
        // request to load terms and conditions page
        QlConfig termsAndConditionsPageIdConfig = qlConfigService.findOneByKey("terms-and-conditions-page-id");
        if (termsAndConditionsPageIdConfig != null) {
            String language = locale.getLanguage();
            model.addAttribute("termsAndConditionsPage", staticPageService.retrievePost(language,
                    termsAndConditionsPageIdConfig.getValue()));
        }

        QlConfig sineNameConfig = qlConfigService.findOneByKey("site-name");
        if (sineNameConfig != null) {
            model.addAttribute("siteName", sineNameConfig.getValue());
        }
    }

    private void setSignUpPageInModel(ModelMap model, Locale locale) {
        messageSource.getMessage("page.signup.title", null, locale);
        model.addAttribute("title", "Sign Up");
        model.addAttribute("view", "client/signup");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String signUp(@Valid @ModelAttribute SignUpUserBean signUpUserBean, final ModelMap model, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        setSignUpPageInModel(model, locale);

        // proceed with registration
        Optional<User> oneByEmail = userRepository.findOneByEmail(signUpUserBean.getEmail());
        if (oneByEmail.isPresent()) {
            // email address already in use
            model.addAttribute("errMsg", messageSource.getMessage("page.signup.error.email_already_registered", null, locale));
        } else {
            User user = userService.createUser(signUpUserBean.getEmail(), signUpUserBean.getPassword(),
                    signUpUserBean.getFirstName(), signUpUserBean.getLastName(),
                    signUpUserBean.getEmail().toLowerCase(), null, locale.getLanguage(), signUpUserBean.getUpdates());
            emailSendingService.sendActivationEmail(user);

            model.addAttribute("success", "success");
            // redirect info for registration and verification email is sent
        }

        return "client/index";
    }

}
