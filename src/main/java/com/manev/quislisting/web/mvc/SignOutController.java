package com.manev.quislisting.web.mvc;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.manev.quislisting.security.jwt.ApiJWTFilter.QL_AUTH;

@Controller
@RequestMapping(MvcRouter.SIGN_OUT)
public class SignOutController extends BaseController {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @RequestMapping(method = RequestMethod.GET)
    public void authorize(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie(QL_AUTH, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        redirectStrategy.sendRedirect(request, response, "/");
    }

}
