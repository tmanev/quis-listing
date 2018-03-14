package com.manev.quislisting.web.rest.validator;

import com.manev.quislisting.service.form.DlListingMessageForm;
import com.manev.quislisting.web.rest.exception.InvalidDlListingMessageFormException;
import org.apache.commons.validator.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DlMessageFormValidator {

    public void validate(DlListingMessageForm form, String currentUserLogin) {
        if (StringUtils.isEmpty(currentUserLogin) &&
                (StringUtils.isEmpty(form.getSenderName()) || StringUtils.isEmpty(form.getSenderEmail()))) {
            throw new InvalidDlListingMessageFormException();
        }

        if (StringUtils.isEmpty(currentUserLogin) && !EmailValidator.getInstance().isValid(form.getSenderEmail())) {
            throw new InvalidDlListingMessageFormException();
        }

        if (StringUtils.isEmpty(form.getText())) {
            throw new InvalidDlListingMessageFormException();
        }

        if (StringUtils.isEmpty(form.getLanguageCode())) {
            throw new InvalidDlListingMessageFormException();
        }
    }

}
