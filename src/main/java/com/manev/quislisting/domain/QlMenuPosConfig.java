package com.manev.quislisting.domain;

public class QlMenuPosConfig {

    private String languageCode;
    private Long topMenuRefId;
    private Long topHeaderMenuRefId;
    private Long footerMenuRefId;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Long getTopMenuRefId() {
        return topMenuRefId;
    }

    public void setTopMenuRefId(Long topMenuRefId) {
        this.topMenuRefId = topMenuRefId;
    }

    public Long getTopHeaderMenuRefId() {
        return topHeaderMenuRefId;
    }

    public void setTopHeaderMenuRefId(Long topHeaderMenuRefId) {
        this.topHeaderMenuRefId = topHeaderMenuRefId;
    }

    public Long getFooterMenuRefId() {
        return footerMenuRefId;
    }

    public void setFooterMenuRefId(Long footerMenuRefId) {
        this.footerMenuRefId = footerMenuRefId;
    }
}
