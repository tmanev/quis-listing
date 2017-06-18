package com.manev.quislisting.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.manev.quislisting.web.rest.vm.ManagedUserVM.PASSWORD_MAX_LENGTH;
import static com.manev.quislisting.web.rest.vm.ManagedUserVM.PASSWORD_MIN_LENGTH;

public class ChangePasswordVM {

    @NotNull
    private String oldPassword;
    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String newPassword;
    @NotNull
    private String newPasswordRepeat;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRepeat() {
        return newPasswordRepeat;
    }

    public void setNewPasswordRepeat(String newPasswordRepeat) {
        this.newPasswordRepeat = newPasswordRepeat;
    }
}
