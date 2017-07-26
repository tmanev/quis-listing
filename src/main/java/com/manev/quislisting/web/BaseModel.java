package com.manev.quislisting.web;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.domain.StaticPageNavMenuRel;
import com.manev.quislisting.web.model.ActiveLanguageBean;

import java.util.List;
import java.util.Set;

public class BaseModel {

    private Set<StaticPageNavMenuRel> topHeaderMenus;
    private Set<StaticPageNavMenuRel> footerMenus;
    private List<ActiveLanguageBean> activeLanguages;
    private StaticPage profilePage;
    private String baseUrl;

    public Set<StaticPageNavMenuRel> getTopHeaderMenus() {
        return topHeaderMenus;
    }

    public void setTopHeaderMenus(Set<StaticPageNavMenuRel> topHeaderMenus) {
        this.topHeaderMenus = topHeaderMenus;
    }

    public BaseModel topHeaderMenus(Set<StaticPageNavMenuRel> topHeaderMenus) {
        this.topHeaderMenus = topHeaderMenus;
        return this;
    }

    public Set<StaticPageNavMenuRel> getFooterMenus() {
        return footerMenus;
    }

    public void setFooterMenus(Set<StaticPageNavMenuRel> footerMenus) {
        this.footerMenus = footerMenus;
    }

    public BaseModel footerMenus(Set<StaticPageNavMenuRel> footerMenus) {
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
