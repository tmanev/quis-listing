package com.manev.quislisting.service.model;

import com.manev.quislisting.service.post.dto.DlListingFieldItemDTO;

import java.util.List;

public class DlListingFieldItemGroupModel {

    private Long id;
    private String value;
    private List<DlListingFieldItemDTO> dlListingFieldItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DlListingFieldItemDTO> getDlListingFieldItems() {
        return dlListingFieldItems;
    }

    public void setDlListingFieldItems(List<DlListingFieldItemDTO> dlListingFieldItems) {
        this.dlListingFieldItems = dlListingFieldItems;
    }
}
