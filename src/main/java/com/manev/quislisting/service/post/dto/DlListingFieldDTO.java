package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.domain.DlContentField;

import java.util.ArrayList;
import java.util.List;

public class DlListingFieldDTO {

    private Long id;
    private String type;
    private String name;
    private String value;
    private String previewValue;
    private List<DlListingFieldItemDTO> dlListingFieldItemDTOs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DlListingFieldDTO name(String name) {
        this.name = name;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DlListingFieldDTO id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DlListingFieldDTO value(String value) {
        this.value = value;
        return this;
    }

    public String getPreviewValue() {
        return previewValue;
    }

    public void setPreviewValue(String previewValue) {
        this.previewValue = previewValue;
    }

    public DlListingFieldDTO previewValue(String previewValue) {
        this.previewValue = previewValue;
        return this;
    }

    public List<DlListingFieldItemDTO> getDlListingFieldItemDTOs() {
        return dlListingFieldItemDTOs;
    }

    public void setDlListingFieldItemDTOs(List<DlListingFieldItemDTO> dlListingFieldItemDTOs) {
        this.dlListingFieldItemDTOs = dlListingFieldItemDTOs;
    }

    public DlListingFieldDTO dlListingFieldItemDTOs(List<DlListingFieldItemDTO> dlListingFieldItemDTOs) {
        this.dlListingFieldItemDTOs = dlListingFieldItemDTOs;
        return this;
    }

    public void addDlListingFieldItemDto(DlListingFieldItemDTO dlListingFieldItemDTO) {
        if (dlListingFieldItemDTOs == null) {
            dlListingFieldItemDTOs = new ArrayList<>();
        }
        dlListingFieldItemDTOs.add(dlListingFieldItemDTO);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DlListingFieldDTO type(String type) {
        this.type = type;
        return this;
    }
}
