package com.manev.quislisting.web;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Set;

@Controller
public class PostController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(PostController.class);

    private PostRepository<AbstractPost> postRepository;

    public PostController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                          PostRepository<AbstractPost> postRepository, LanguageRepository languageRepository,
                          LocaleResolver localeResolver,
                          LanguageTranslationRepository languageTranslationRepository) {
        super(navMenuRepository, qlConfigRepository, languageRepository, languageTranslationRepository, localeResolver);
        this.postRepository = postRepository;
    }

    private Translation translationExists(String languageCode, Set<Translation> translationList) {
        for (Translation translation : translationList) {
            if (translation.getLanguageCode().equals(languageCode)) {
                return translation;
            }
        }
        return null;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String showPost(@PathVariable String name, final ModelMap modelMap, HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        log.debug("Language from cookie: {}", language);

        AbstractPost post = postRepository.findOneByName(name);

        Translation translation = post.getTranslation();
        if (!translation.getLanguageCode().equals(language)){
            // find post related to the translation
            // and redirect it to the page url

            // check if there is a translation
            Translation translationForLanguage = translationExists(language, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage!=null) {
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

}
