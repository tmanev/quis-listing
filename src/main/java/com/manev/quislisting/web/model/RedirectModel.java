package com.manev.quislisting.web.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class RedirectModel {
    @Pattern(regexp="^/([^/].*)?$")
    @NotBlank
    private String continueUrl;

    public void setContinue(String continueUrl) {
        this.continueUrl = continueUrl;
    }

    public String getContinue() {
        return continueUrl;
    }
}
