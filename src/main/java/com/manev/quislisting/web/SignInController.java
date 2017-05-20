package com.manev.quislisting.web;

import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.security.jwt.TokenProvider;
import com.manev.quislisting.web.rest.vm.LoginVM;
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

import static com.manev.quislisting.security.jwt.JWTConfigurer.AUTHORIZATION_HEADER;

@Controller
@RequestMapping("/sign-in")
public class SignInController extends BaseController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public SignInController(NavMenuRepository navMenuRepository, QlConfigRepository qlConfigRepository,
                            TokenProvider tokenProvider, AuthenticationManager authenticationManager,
                            LanguageRepository languageRepository, LocaleResolver localeResolver) {
        super(navMenuRepository, qlConfigRepository, languageRepository, localeResolver);
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(final ModelMap model) throws IOException {
        model.addAttribute("title", "Sign In");
        model.addAttribute("view", "client/signin");
        return "client/index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public void authorize(@Valid @ModelAttribute("loginVM") LoginVM loginVM, HttpServletRequest request,
                            HttpServletResponse response, ModelMap model) throws IOException {
//        loadMenus(model);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(AUTHORIZATION_HEADER, "Bearer " + jwt);
            response.addCookie(new Cookie("ql-auth", "Bearer:" + jwt));
//            model.addAttribute("view", "client/default");

            redirectStrategy.sendRedirect(request, response, "/");

//            return "client/index";
        } catch (AuthenticationException exception) {
//            return "client/index";
        }
    }

}
