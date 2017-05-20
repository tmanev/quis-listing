package com.manev.quislisting.web;

import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class PostController extends BaseController {

    private PostRepository<AbstractPost> postRepository;

    public PostController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                          PostRepository<AbstractPost> postRepository, LanguageRepository languageRepository,
                          LocaleResolver localeResolver) {
        super(navMenuRepository, qlConfigRepository, languageRepository, localeResolver);
        this.postRepository = postRepository;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String showPost(@PathVariable String name, final ModelMap modelMap, HttpServletRequest request) throws IOException {
        AbstractPost post = postRepository.findOneByName(name);

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
