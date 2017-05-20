package com.manev.quislisting.web;

import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    private PostRepository<AbstractPost> postRepository;

    public HomeController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                          PostRepository<AbstractPost> postRepository, LanguageRepository languageRepository,
                          LocaleResolver localeResolver) {
        super(navMenuRepository, qlConfigRepository, languageRepository, localeResolver);
        this.postRepository = postRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model) throws IOException {
        AbstractPost post = postRepository.findOneByName("/");

        model.addAttribute("title", post.getTitle());
        model.addAttribute("view", "client/default");
        return "client/index";
    }

}
