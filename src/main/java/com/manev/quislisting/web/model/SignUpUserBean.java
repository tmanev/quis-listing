package com.manev.quislisting.web.model;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

import static com.manev.quislisting.web.rest.vm.ManagedUserVM.PASSWORD_MAX_LENGTH;
import static com.manev.quislisting.web.rest.vm.ManagedUserVM.PASSWORD_MIN_LENGTH;

public class SignUpUserBean {

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private Boolean updates = Boolean.TRUE;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getUpdates() {
        return updates;
    }

    public void setUpdates(Boolean updates) {
        this.updates = updates;
    }
}
