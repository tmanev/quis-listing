package com.manev.quislisting.web.mvc;

import com.manev.quislisting.web.model.JsTranslations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping(value = MvcRouter.CONTACT)
public class ContactController extends BaseController {

    private static final String PAGE_CONTACT_MESSAGE_SENT_SUCCESS = "page.contact.message.sent_success";

    @GetMapping
    public String contactPage(final ModelMap modelMap, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        JsTranslations jsTranslations = new JsTranslations();
        jsTranslations.addTranslation(PAGE_CONTACT_MESSAGE_SENT_SUCCESS,
                messageSource.getMessage(PAGE_CONTACT_MESSAGE_SENT_SUCCESS, null, locale));
        modelMap.addAttribute("jsTranslations", jsTranslations.getTranslations());
        modelMap.addAttribute("title", messageSource.getMessage("page.contact.title", null, locale));
        modelMap.addAttribute("view", "client/contacts");

        return "client/index";
    }

}
