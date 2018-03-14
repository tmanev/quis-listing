package com.manev.quislisting.service.form;

import javax.validation.constraints.NotNull;

public class DlWriteMessageForm {
    @NotNull
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
