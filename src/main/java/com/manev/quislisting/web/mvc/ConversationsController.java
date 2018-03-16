package com.manev.quislisting.web.mvc;

import com.manev.quislisting.web.mvc.MvcRouter.MessageCenter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Base controller for the messages interface.
 */
@Controller
public class ConversationsController extends BaseController {

    @RequestMapping(value = MessageCenter.CONVERSATIONS, method = RequestMethod.GET)
    public String showMyMessagesPage(final ModelMap modelMap, final HttpServletRequest request) {
        final Locale locale = localeResolver.resolveLocale(request);

        modelMap.addAttribute("jsTranslations", getJsTranslations(locale));

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.message_center.conversations.title", null, locale));
        modelMap.addAttribute("view", "client/message-center/conversations");

        return PAGE_CLIENT_INDEX;
    }

}
