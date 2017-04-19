package com.manev.quislisting.domain;

public class QlMenuPosConfig {

    private Long topMenuRefId;
    private Long topHeaderMenuRefId;
    private Long footerMenuRefId;

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
