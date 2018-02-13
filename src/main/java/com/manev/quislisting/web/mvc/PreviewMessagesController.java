package com.manev.quislisting.web.mvc;

import com.manev.quislisting.service.dto.DlMessageDTO;
import com.manev.quislisting.service.mapper.DlMessagesMapper;
import com.manev.quislisting.service.post.DlMessagesService;
import com.manev.quislisting.web.model.JsTranslations;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the messages preview interface.
 */
@Controller
public class PreviewMessagesController extends BaseController {

    private static final String MESSAGE_PREVIEW_PAGE_TITLE = "page.my_message_preview.title";
    private static final String MESSAGE_SUCCESS_TEXT = "page.my_message_preview.label.sendmessage.success";
    private static final String MESSAGE_FAILURE_TEXT = "page.my_message_preview.label.sendmessage.failure";
    private static final Long ZERO_VALUE = 0L;

    private final DlMessagesService dlMessagesService;

    private final DlMessagesMapper dlMessagesMapper;

    public PreviewMessagesController(final DlMessagesService dlMessagesService,
            final DlMessagesMapper dlMessagesMapper) {
        this.dlMessagesService = dlMessagesService;
        this.dlMessagesMapper = dlMessagesMapper;
    }

    @RequestMapping(value = MvcRouter.MyMessages.PREVIEW, method = RequestMethod.GET)
    public String showPreviewMessagesPage(final @PathVariable String id, final ModelMap modelMap,
            final HttpServletRequest request, final Pageable pageable) throws IOException {
        final DlMessageDTO dlMessageDTO = dlMessagesService.findOne(Long.valueOf(id));

        if (dlMessageDTO == null) {
            return redirectToPageNotFound();
        }

        final Locale locale = localeResolver.resolveLocale(request);

        final Page<DlMessageDTO> page = dlMessagesService.findAllMessagesForListingId(pageable,
                dlMessageDTO.getListingId(), dlMessageDTO.getReceiverId(), dlMessageDTO.getSenderId());

        final List<DlMessageDTO> dlMessagesDTOList = page.getContent();
        modelMap.addAttribute("dlMessagesDTOList", dlMessagesDTOList);

        final JsTranslations jsTranslations = new JsTranslations();
        jsTranslations.addTranslation(MESSAGE_SUCCESS_TEXT, messageSource.getMessage(MESSAGE_SUCCESS_TEXT,
                null, locale));
        jsTranslations.addTranslation(MESSAGE_FAILURE_TEXT, messageSource.getMessage(MESSAGE_FAILURE_TEXT,
                null, locale));

        modelMap.addAttribute("jsTranslations", jsTranslations.getTranslations());
        modelMap.addAttribute(ATTRIBUTE_TITLE, messageSource.getMessage(MESSAGE_PREVIEW_PAGE_TITLE, null, locale));
        modelMap.addAttribute("dlMessageDTO", prepareMessageDTOForPreviewPage(dlMessagesDTOList));
        modelMap.addAttribute("receiverId", getReceiverId(dlMessagesDTOList));
        modelMap.addAttribute("view", "client/message");

        return PAGE_CLIENT_INDEX;
    }

    private DlMessageDTO prepareMessageDTOForPreviewPage(final List<DlMessageDTO> dlMessageDTOList) {
        if (CollectionUtils.isNotEmpty(dlMessageDTOList)) {
            final DlMessageDTO dlMessageDTO = dlMessageDTOList.get(0);

            if (dlMessageDTO != null) {
                return dlMessagesMapper.prepareMessageDTOForFrontend(dlMessageDTO);
            }
        }

        return null;
    }

    private Long getReceiverId(final List<DlMessageDTO> dlMessageDTOList) {
        if (CollectionUtils.isNotEmpty(dlMessageDTOList)) {
            final DlMessageDTO dlMessageDTO = dlMessageDTOList.get(0);

            if (dlMessageDTO != null) {
                return dlMessageDTO.getReceiverId();
            }
        }

        return ZERO_VALUE;
    }
}
