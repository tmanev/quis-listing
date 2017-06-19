package com.manev.quislisting.web;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.exception.MissingConfigurationException;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UserService;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
public class PostController extends BaseController {

    private static final String REDIRECT = "redirect:/";
    private final Logger log = LoggerFactory.getLogger(PostController.class);
    private final UserService userService;
    private final UserRepository userRepository;

    public PostController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                          PostRepository<AbstractPost> postRepository, LanguageRepository languageRepository,
                          LocaleResolver localeResolver,
                          LanguageTranslationRepository languageTranslationRepository, UserService userService, UserRepository userRepository) {
        super(navMenuRepository, qlConfigRepository, languageRepository, languageTranslationRepository, localeResolver,
                postRepository);
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String showPost(@PathVariable String name, final ModelMap modelMap, HttpServletRequest request) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        log.debug("Language from cookie: {}", language);

        AbstractPost post = postRepository.findOneByName(name);
        if (post == null) {
            return redirectToPageNotFound();
        }

        Translation translation = post.getTranslation();
        if (!translation.getLanguageCode().equals(language)) {
            // find post related to the translation
            // and redirect it to the page url

            // check if there is a translation
            Translation translationForLanguage = translationExists(language, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                AbstractPost translatedPost = postRepository.findOneByTranslation(translationForLanguage);
                return REDIRECT + URLEncoder.encode(translatedPost.getName(), CharEncoding.UTF_8);
            }
        }

        modelMap.addAttribute("title", post.getTitle());

        if (post instanceof QlPage) {
            // handle page
            String content = post.getContent();
            switch (content) {
                case "[contact-form]":
                    modelMap.addAttribute("view", "client/contacts");
                    break;
                case "[not-found-page]":
                    QlConfig contactPageIdConfig = qlConfigRepository.findOneByKey("contact-page-id");
                    if (contactPageIdConfig == null) {
                        throw new MissingConfigurationException("Contact Page configuration expected");
                    }
                    AbstractPost contactPage = retrievePage(language, contactPageIdConfig.getValue());
                    modelMap.addAttribute("contactPageName", contactPage.getName());
                    modelMap.addAttribute("view", "client/page-not-found");
                    break;
                case "[forgot-password-page]":
                    modelMap.addAttribute("view", "client/forgot-password");
                    break;
                default:
                    modelMap.addAttribute("content", content);
                    modelMap.addAttribute("view", "client/post");
                    break;
            }
        } else if (post instanceof DlListing) {
            // handle listing
            modelMap.addAttribute("view", "client/dl-listing");
        }

        return "client/index";
    }

    @RequestMapping(value = "/{name}/{secondName}", method = RequestMethod.GET)
    public String showPage(@PathVariable String name,
                           @PathVariable String secondName,
                           final ModelMap modelMap, HttpServletRequest request,
                           @RequestParam Map<String, String> allRequestParams) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        log.debug("Language from cookie: {}", language);

        String slugName = name + "/" + secondName;
        AbstractPost post = postRepository.findOneByName(slugName);
        if (post == null) {
            return redirectToPageNotFound();
        }

        Translation translation = post.getTranslation();
        if (!translation.getLanguageCode().equals(language)) {
            // find post related to the translation
            // and redirect it to the page url

            // check if there is a translation
            Translation translationForLanguage = translationExists(language, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                AbstractPost translatedPost = postRepository.findOneByTranslation(translationForLanguage);
                String firstPart = translatedPost.getName().split("/")[0];
                String secondPart = translatedPost.getName().split("/")[1];
                return REDIRECT + URLEncoder.encode(firstPart, CharEncoding.UTF_8) + "/"
                        + URLEncoder.encode(secondPart, CharEncoding.UTF_8);
            }
        }

        modelMap.addAttribute("title", post.getTitle());

        if (post instanceof QlPage) {
            // handle page
            String content = post.getContent();
            switch (content) {
                case "[activation-link-page]":
                    if (handleActivationLinkPage(modelMap, allRequestParams)) return redirectToPageNotFound();
                    break;
                case "[reset-password-finish-page]":
                    if (handleResetPasswordFinishPage(modelMap, allRequestParams)) return redirectToPageNotFound();
                    break;
                case "[profile-page]":
                    handleProfilePage(modelMap);
                    break;
                default:
                    modelMap.addAttribute("content", content);
                    modelMap.addAttribute("view", "client/post");
                    break;
            }
        }

        return "client/index";
    }

    private void handleProfilePage(ModelMap modelMap) throws UnsupportedEncodingException {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> oneByLogin = userRepository.findOneByLogin(currentUserLogin);
        if (oneByLogin.isPresent()) {
            modelMap.addAttribute("user", oneByLogin.get());
            modelMap.addAttribute("view", "client/account/profile");
        } else {
            redirectToPageNotFound();
        }
    }

    private boolean handleResetPasswordFinishPage(ModelMap modelMap, @RequestParam Map<String, String> allRequestParams) {
        String key = allRequestParams.get("key");
        if (key == null) {
            return true;
        } else {
            Optional<User> oneByResetKey = userRepository.findOneByResetKey(key);
            if (!oneByResetKey.isPresent()) {
                return true;
            }
            modelMap.addAttribute("user", new User());
            modelMap.addAttribute("view", "client/account/password-reset-finish");
        }
        return false;
    }

    private boolean handleActivationLinkPage(ModelMap modelMap, @RequestParam Map<String, String> allRequestParams) {
        String key = allRequestParams.get("key");
        if (key == null) {
            return true;
        } else {
            Optional<User> activatedUser = userService.activateRegistration(key);
            if (activatedUser.isPresent()) {
                modelMap.addAttribute("view", "client/account/activation");
            } else {
                return true;
            }
        }
        return false;
    }

    private String redirectToPageNotFound() throws UnsupportedEncodingException {
        QlConfig notFoundPageConfig = qlConfigRepository.findOneByKey("not-found-page-id");
        if (notFoundPageConfig == null) {
            throw new MissingConfigurationException("Not Found Page configuration expected");
        }
        AbstractPost notFoundPage = postRepository.findOne(Long.valueOf(notFoundPageConfig.getValue()));
        return REDIRECT + URLEncoder.encode(notFoundPage.getName(), "UTF-8");
    }


}
