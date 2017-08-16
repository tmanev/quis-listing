package com.manev.quislisting.web.mvc;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.domain.NavMenuItem;
import com.manev.quislisting.web.model.ActiveLanguageBean;

import java.util.List;
import java.util.Set;

public class BaseModel {

    private Set<NavMenuItem> topHeaderMenus;
    private Set<NavMenuItem> footerMenus;
    private List<ActiveLanguageBean> activeLanguages;
    private StaticPage profilePage;
    private String baseUrl;

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

    public List<ActiveLanguageBean> getActiveLanguages() {
        return activeLanguages;
    }

    public void setActiveLanguages(List<ActiveLanguageBean> activeLanguages) {
        this.activeLanguages = activeLanguages;
    }

    public BaseModel activeLanugages(List<ActiveLanguageBean> activeLanguages) {
        this.activeLanguages = activeLanguages;
        return this;
    }

    public StaticPage getProfilePage() {
        return profilePage;
    }

    public void setProfilePage(StaticPage profilePage) {
        this.profilePage = profilePage;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
