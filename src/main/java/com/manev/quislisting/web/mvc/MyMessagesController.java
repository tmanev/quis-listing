package com.manev.quislisting.web.mvc;

import com.manev.quislisting.web.model.JsTranslations;
import com.manev.quislisting.web.mvc.MvcRouter.MyMessages;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Base controller for the messages interface.
 */
@Controller
public class MyMessagesController extends BaseController {

    private static final String DELETE_MESSAGE_TEXT = "page.my_messages.notifications.delete_listing_success";


    @RequestMapping(value = MyMessages.BASE, method = RequestMethod.GET)
    public String showMyMessagesPage(final ModelMap modelMap, final HttpServletRequest request) {
        final Locale locale = localeResolver.resolveLocale(request);

        final JsTranslations jsTranslations = new JsTranslations();
        jsTranslations.addTranslation(DELETE_MESSAGE_TEXT, messageSource.getMessage(DELETE_MESSAGE_TEXT,
                null, locale));

        modelMap.addAttribute("jsTranslations", jsTranslations.getTranslations());

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.my_messages.title", null, locale));
        modelMap.addAttribute("view", "client/my-messages/my-messages");

        return PAGE_CLIENT_INDEX;
    }

}
