package com.manev.quislisting.web;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.UserService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
public class PostController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(PostController.class);
    private final UserService userService;
    private PostRepository<AbstractPost> postRepository;

    public PostController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                          PostRepository<AbstractPost> postRepository, LanguageRepository languageRepository,
                          LocaleResolver localeResolver,
                          LanguageTranslationRepository languageTranslationRepository, UserService userService) {
        super(navMenuRepository, qlConfigRepository, languageRepository, languageTranslationRepository, localeResolver,
                postRepository);
        this.postRepository = postRepository;
        this.userService = userService;
    }


    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String showPost(@PathVariable String name, final ModelMap modelMap, HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
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
                return "redirect:/" + URLEncoder.encode(translatedPost.getName(), "UTF-8");
            }
        }

        modelMap.addAttribute("title", post.getTitle());

        if (post instanceof QlPage) {
            // handle page
            String content = post.getContent();
            if (content.equals("[contact-form]")) {
                modelMap.addAttribute("view", "client/contacts");
            } else if (content.equals("[not-found-page]")) {
                QlConfig contactPageIdConfig = qlConfigRepository.findOneByKey("contact-page-id");
                if (contactPageIdConfig == null) {
                    throw new RuntimeException("Contact Page configuration expected");
                }
                AbstractPost contactPage = retrievePage(language, contactPageIdConfig.getValue());
                modelMap.addAttribute("contactPageName", contactPage.getName());
                modelMap.addAttribute("view", "client/page-not-found");
            } else if (content.equals("[forgot-password-page]")) {
                modelMap.addAttribute("view", "client/forgot-password");
            } else {
                modelMap.addAttribute("content", content);
                modelMap.addAttribute("view", "client/post");
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
                           HttpServletResponse response,
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
                return "redirect:/" + URLEncoder.encode(firstPart, "UTF-8") + "/"
                        + URLEncoder.encode(secondPart, "UTF-8");
            }
        }

        modelMap.addAttribute("title", post.getTitle());

        if (post instanceof QlPage) {
            // handle page
            String content = post.getContent();
            if (content.equals("[activation-link-page]")) {
                String key = allRequestParams.get("key");
                if (key == null) {
                    return redirectToPageNotFound();
                } else {
                    Optional<User> activatedUser = userService.activateRegistration(key);
                    if (activatedUser.isPresent()) {
                        modelMap.addAttribute("view", "client/account/activation");
                    } else {
                        return redirectToPageNotFound();
                    }
                }
            }
        }

        return "client/index";
    }

    private String redirectToPageNotFound() throws UnsupportedEncodingException {
        QlConfig notFoundPageConfig = qlConfigRepository.findOneByKey("not-found-page-id");
        if (notFoundPageConfig == null) {
            throw new RuntimeException("Not Found Page configuration expected");
        }
        AbstractPost notFoundPage = postRepository.findOne(Long.valueOf(notFoundPageConfig.getValue()));
        return "redirect:/" + URLEncoder.encode(notFoundPage.getName(), "UTF-8");
    }


}
