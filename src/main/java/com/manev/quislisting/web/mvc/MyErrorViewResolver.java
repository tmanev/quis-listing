package com.manev.quislisting.web.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class MyErrorViewResolver implements ErrorViewResolver {

    private final Logger log = LoggerFactory.getLogger(MyErrorViewResolver.class);

    private final PageNotFoundController pageNotFoundController;

    public MyErrorViewResolver(PageNotFoundController pageNotFoundController) {
        this.pageNotFoundController = pageNotFoundController;
    }

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest httpServletRequest, HttpStatus httpStatus, Map<String, Object> map) {
        if (httpStatus.equals(HttpStatus.NOT_FOUND)) {
            try {
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject("baseModel", pageNotFoundController.baseModel(httpServletRequest));
                modelAndView.setViewName(pageNotFoundController.pageNotFound(modelAndView.getModelMap(), httpServletRequest));
                return modelAndView;
            } catch (IOException e) {
                log.error("Page Not Found could not be loaded", e);
            }
        }
        return null;
    }
}
