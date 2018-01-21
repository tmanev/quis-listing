package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.service.post.exception.PostDifferentLanguageException;
import com.manev.quislisting.service.post.exception.PostNotFoundException;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;

@Controller
public class StaticPageController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(StaticPageController.class);

    @RequestMapping(value = MvcRouter.STATIC_PAGE, method = RequestMethod.GET)
    public String showPost(@PathVariable String name, final ModelMap modelMap, HttpServletRequest request) throws IOException {
        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        log.debug("Language from cookie: {}", language);

        try {
            StaticPage post = staticPageService.findOneByName(name, language);
            modelMap.addAttribute("title", post.getTitle());

            // handle page
            String content = post.getContent();
            modelMap.addAttribute("content", content);
            modelMap.addAttribute("view", "client/post");
        } catch (PostNotFoundException e) {
            return redirectToPageNotFound();
        } catch (PostDifferentLanguageException e) {
            return REDIRECT + URLEncoder.encode(e.getPostNameDifferentLanguage(), CharEncoding.UTF_8);
        }

        return "client/index";
    }

}
