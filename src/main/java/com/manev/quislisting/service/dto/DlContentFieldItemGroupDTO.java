package com.manev.quislisting.service.dto;

import java.util.List;

public class DlContentFieldItemGroupDTO {

    private Long id;
    private String name;
    private String translatedName;
    private String description;
    private Integer orderNum;
    private List<DlContentFieldItemDTO> dlContentFieldItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslatedName() {
        return translatedName;
    }

    public void setTranslatedName(String translatedName) {
        this.translatedName = translatedName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<DlContentFieldItemDTO> getDlContentFieldItems() {
        return dlContentFieldItems;
    }

    public void setDlContentFieldItems(List<DlContentFieldItemDTO> dlContentFieldItems) {
        this.dlContentFieldItems = dlContentFieldItems;
    }

}
