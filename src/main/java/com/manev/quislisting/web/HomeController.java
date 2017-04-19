package com.manev.quislisting.web;

import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.post.NavMenuItemRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    private PostRepository<AbstractPost> postRepository;

    public HomeController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository, PostRepository<AbstractPost> postRepository) {
        super(navMenuRepository, qlConfigRepository);
        this.postRepository = postRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model) throws IOException {
        loadMenus(model);
        AbstractPost post = postRepository.findOneByName("/");

        model.addAttribute("title", post.getTitle());
        model.addAttribute("view", "client/default");
        return "client/index";
    }

}
