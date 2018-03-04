package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.service.dto.DlContentFieldGroupDTO;
import com.manev.quislisting.service.model.DlListingFieldItemGroupModel;
import com.manev.quislisting.service.model.QlStringTranslationModel;

import java.util.ArrayList;
import java.util.List;

public class DlListingFieldDTO {

    private Long id;
    private String type;
    private String name;
    private List<QlStringTranslationModel> translatedNames;
    private String value;
    private String selectedValue;
    private List<QlStringTranslationModel> translatedValues;
    private List<DlListingFieldItemDTO> dlListingFieldItemDTOs;
    private List<DlListingFieldItemGroupModel> dlListingFieldItemGroups;
    private DlContentFieldGroupDTO dlContentFieldGroup;

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

    public List<DlListingFieldItemGroupModel> getDlListingFieldItemGroups() {
        return dlListingFieldItemGroups;
    }

    public void setDlListingFieldItemGroups(List<DlListingFieldItemGroupModel> dlListingFieldItemGroups) {
        this.dlListingFieldItemGroups = dlListingFieldItemGroups;
    }

    public DlListingFieldDTO dlListingFieldItemGroups(List<DlListingFieldItemGroupModel> dlListingFieldItemGroups) {
        this.dlListingFieldItemGroups = dlListingFieldItemGroups;
        return this;
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

    public DlContentFieldGroupDTO getDlContentFieldGroup() {
        return dlContentFieldGroup;
    }

    public void setDlContentFieldGroup(DlContentFieldGroupDTO dlContentFieldGroup) {
        this.dlContentFieldGroup = dlContentFieldGroup;
    }

    public DlListingFieldDTO dlContentFieldGroup(DlContentFieldGroupDTO dlContentFieldGroup) {
        this.dlContentFieldGroup = dlContentFieldGroup;
        return this;
    }

    public List<QlStringTranslationModel> getTranslatedNames() {
        return translatedNames;
    }

    public void setTranslatedNames(List<QlStringTranslationModel> translatedNames) {
        this.translatedNames = translatedNames;
    }

    public List<QlStringTranslationModel> getTranslatedValues() {
        return translatedValues;
    }

    public void setTranslatedValues(List<QlStringTranslationModel> translatedValues) {
        this.translatedValues = translatedValues;
    }
}
