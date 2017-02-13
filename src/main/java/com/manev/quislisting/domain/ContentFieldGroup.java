package com.manev.quislisting.domain;

import javax.persistence.*;

@Entity
@Table(name="ql_dl_content_fields_group")
public class ContentFieldGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Boolean onTab;

    @Column
    private Boolean hideAnonymous;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOnTab() {
        return onTab;
    }

    public void setOnTab(Boolean onTab) {
        this.onTab = onTab;
    }

    public Boolean getHideAnonymous() {
        return hideAnonymous;
    }

    public void setHideAnonymous(Boolean hideAnonymous) {
        this.hideAnonymous = hideAnonymous;
    }
}
