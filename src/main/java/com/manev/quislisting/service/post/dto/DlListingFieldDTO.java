package com.manev.quislisting.service.post.dto;

import java.util.ArrayList;
import java.util.List;

public class DlListingFieldDTO {

    private Long id;
    private String type;
    private String name;
    private String translatedName;
    private String value;
    private String selectedValue;
    private String translatedValue;
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

    public String getTranslatedName() {
        return translatedName;
    }

    public void setTranslatedName(String translatedName) {
        this.translatedName = translatedName;
    }

    public DlListingFieldDTO translatedName(String translatedName) {
        this.translatedName = translatedName;
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

    public String getTranslatedValue() {
        return translatedValue;
    }

    public void setTranslatedValue(String translatedValue) {
        this.translatedValue = translatedValue;
    }

    public DlListingFieldDTO translatedValue(String translatedValue) {
        this.translatedValue = translatedValue;
        return this;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public DlListingFieldDTO selectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
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
