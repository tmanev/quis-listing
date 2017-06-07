package com.manev.quislisting.web.model;

public class SignUpUserBean {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Boolean updates;

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
