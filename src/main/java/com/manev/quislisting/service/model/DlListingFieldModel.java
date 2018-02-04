package com.manev.quislisting.service.model;

import com.manev.quislisting.service.dto.DlContentFieldGroupDTO;

import java.util.List;

public class DlListingFieldModel {

    private Long id;
    private String type;
    private String name;
    private String value;
    private List<DlListingFieldItemModel> items;
    private DlContentFieldGroupDTO dlContentFieldGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DlListingFieldItemModel> getItems() {
        return items;
    }

    public void setItems(List<DlListingFieldItemModel> items) {
        this.items = items;
    }

    public DlContentFieldGroupDTO getDlContentFieldGroup() {
        return dlContentFieldGroup;
    }

    public void setDlContentFieldGroup(DlContentFieldGroupDTO dlContentFieldGroup) {
        this.dlContentFieldGroup = dlContentFieldGroup;
    }
}
