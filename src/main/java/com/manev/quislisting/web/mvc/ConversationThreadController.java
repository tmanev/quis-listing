package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.DlMessageDTO;
import com.manev.quislisting.service.post.DlMessagesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controller for the messages preview interface.
 */
@Controller
public class ConversationThreadController extends BaseController {

    private final DlMessagesService dlMessagesService;
    private final UserService userService;

    public ConversationThreadController(final DlMessagesService dlMessagesService, UserService userService) {
        this.dlMessagesService = dlMessagesService;
        this.userService = userService;
    }

    @RequestMapping(value = MvcRouter.MessageCenter.CONVERSATION_THREAD, method = RequestMethod.GET)
    public String showConversationThreadPage(final @PathVariable String messageOverviewId, final ModelMap modelMap,
                                          final HttpServletRequest request) throws IOException {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userService.findOneByLogin(currentUserLogin);
        if (!currentUser.isPresent()) {
            return redirectToPageNotFound();
        }

        final DlMessageDTO dlMessageOverviewDTO = dlMessagesService.findMessageOverviewById(Long.valueOf(messageOverviewId));

        if (dlMessageOverviewDTO == null) {
            return redirectToPageNotFound();
        }

        if (!dlMessageOverviewDTO.getReceiver().getId().equals(currentUser.get().getId())) {
            return redirectToPageNotFound();
        }

        final Locale locale = localeResolver.resolveLocale(request);

        final Page<DlMessageDTO> page = dlMessagesService.findConversationMessages(new PageRequest(0, 6), dlMessageOverviewDTO.getListingId(), dlMessageOverviewDTO.getReceiver().getId(), dlMessageOverviewDTO.getSender().getId());

        final List<DlMessageDTO> dlMessagesDTOList = page.getContent();
        modelMap.addAttribute("dlMessageOverview", dlMessageOverviewDTO);
        modelMap.addAttribute("dlMessages", dlMessagesDTOList);
        modelMap.addAttribute("totalDlMessages", page.getTotalElements());
        modelMap.addAttribute("loadedDlMessages", page.getNumberOfElements());

        modelMap.addAttribute("jsTranslations", getJsTranslations(locale));

        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage("page.message_center.conversation_thread.title", null, locale));
        modelMap.addAttribute("view", "client/message-center/conversation-thread");

        return PAGE_CLIENT_INDEX;
    }

}
