package com.manev.quislisting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "ql_authority")
public class Authority {

    @Id
    @NotNull
    @Size(min = 0, max = 50)
    @Column(length = 50)
    private String role;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(length = 50)
    private String name;

    @Column
    private String capabilities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority = (Authority) o;

        if (role != null ? !role.equals(authority.role) : authority.role != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return role != null ? role.hashCode() : 0;
    }

}
