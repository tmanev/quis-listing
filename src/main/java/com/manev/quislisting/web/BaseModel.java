package com.manev.quislisting.web;

import com.manev.quislisting.domain.post.discriminator.NavMenuItem;
import com.manev.quislisting.domain.qlml.Language;

import java.util.List;
import java.util.Set;

public class BaseModel {

    private Set<NavMenuItem> topHeaderMenus;
    private Set<NavMenuItem> footerMenus;
    private List<Language> activeLanguages;

    public Set<NavMenuItem> getTopHeaderMenus() {
        return topHeaderMenus;
    }

    public void setTopHeaderMenus(Set<NavMenuItem> topHeaderMenus) {
        this.topHeaderMenus = topHeaderMenus;
    }

    public BaseModel topHeaderMenus(Set<NavMenuItem> topHeaderMenus) {
        this.topHeaderMenus = topHeaderMenus;
        return this;
    }

    public Set<NavMenuItem> getFooterMenus() {
        return footerMenus;
    }

    public void setFooterMenus(Set<NavMenuItem> footerMenus) {
        this.footerMenus = footerMenus;
    }

    public BaseModel footerMenus(Set<NavMenuItem> footerMenus) {
        this.footerMenus = footerMenus;
        return this;
    }

    public List<Language> getActiveLanguages() {
        return activeLanguages;
    }

    public void setActiveLanguages(List<Language> activeLanguages) {
        this.activeLanguages = activeLanguages;
    }

    public BaseModel activeLanugages(List<Language> activeLanguages) {
        this.activeLanguages = activeLanguages;
        return this;
    }
}
