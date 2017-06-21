package com.manev.quislisting.web;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.PageRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.EmailSendingService;
import com.manev.quislisting.service.UserService;
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

    private PageRepository pageRepository;
    private MessageSource messageSource;
    private UserRepository userRepository;
    private UserService userService;
    private EmailSendingService emailSendingService;

    public SignUpController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                            LanguageRepository languageRepository, LanguageTranslationRepository languageTranslationRepository,
                            LocaleResolver localeResolver, PageRepository pageRepository, MessageSource messageSource,
                            UserRepository userRepository, UserService userService,
                            PostRepository<AbstractPost> postRepository, EmailSendingService emailSendingService) {
        super(navMenuRepository, qlConfigRepository, languageRepository, languageTranslationRepository, localeResolver,
                postRepository);
        this.pageRepository = pageRepository;
        this.messageSource = messageSource;
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
        QlConfig siteNameConfig = qlConfigRepository.findOneByKey("site-name");
        if (siteNameConfig != null) {
            model.addAttribute("siteName", siteNameConfig.getValue());
        }

        // request to load terms and conditions page
        QlConfig termsAndConditionsPageIdConfig = qlConfigRepository.findOneByKey("terms-and-conditions-page-id");
        if (termsAndConditionsPageIdConfig != null) {
            String language = locale.getLanguage();

            model.addAttribute("termsAndConditionsPage", retrieveTermsAndConditionsPage(language,
                    termsAndConditionsPageIdConfig.getValue()));
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
            model.addAttribute("error", "some error");
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

    private QlPage retrieveTermsAndConditionsPage(String languageCode, String pageId) {
        QlPage qlPage;

        qlPage = pageRepository.findOne(Long.valueOf(pageId));
        Translation translation = qlPage.getTranslation();
        if (!translation.getLanguageCode().equals(languageCode)) {
            Translation translationForLanguage = translationExists(languageCode, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                qlPage = pageRepository.findOneByTranslation(translationForLanguage);
            }
        }

        return qlPage;
    }

}
