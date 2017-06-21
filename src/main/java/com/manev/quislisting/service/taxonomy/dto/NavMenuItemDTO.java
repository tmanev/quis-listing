package com.manev.quislisting.service.taxonomy.dto;

import com.manev.quislisting.service.dto.RefItem;
import com.manev.quislisting.service.post.dto.AbstractPostDTO;

public class NavMenuItemDTO extends AbstractPostDTO {
    private Integer order;
    private RefItem refItem;

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
