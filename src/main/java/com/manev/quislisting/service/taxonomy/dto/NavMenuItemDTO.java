package com.manev.quislisting.service.taxonomy.dto;

import com.manev.quislisting.service.dto.RefItem;

public class NavMenuItemDTO {
    private Long id;
    private String title;
    private Integer order;
    private RefItem refItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public RefItem getRefItem() {
        return refItem;
    }

    public void setRefItem(RefItem refItem) {
        this.refItem = refItem;
    }
}
