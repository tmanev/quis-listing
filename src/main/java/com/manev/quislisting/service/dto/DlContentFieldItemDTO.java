package com.manev.quislisting.service.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Set;

public class DlContentFieldItemDTO {

    private Long id;
    private String value;
    private DlContentFieldDTO dlContentFieldDTO;

//    @JsonBackReference(value = "dl_content_field_item_dto_parent_reference")
    private DlContentFieldItemDTO parent;

    @JsonBackReference(value = "dl_content_field_item_dto_children_reference")
    private Set<DlContentFieldItemDTO> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DlContentFieldItemDTO id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DlContentFieldItemDTO value(String value) {
        this.value = value;
        return this;
    }

    public DlContentFieldDTO getDlContentFieldDTO() {
        return dlContentFieldDTO;
    }

    public void setDlContentFieldDTO(DlContentFieldDTO dlContentFieldDTO) {
        this.dlContentFieldDTO = dlContentFieldDTO;
    }

    public DlContentFieldItemDTO getParent() {
        return parent;
    }

    public void setParent(DlContentFieldItemDTO parent) {
        this.parent = parent;
    }

    public DlContentFieldItemDTO parent(DlContentFieldItemDTO parent) {
        this.parent = parent;
        return this;
    }

    public Set<DlContentFieldItemDTO> getChildren() {
        return children;
    }

    public void setChildren(Set<DlContentFieldItemDTO> children) {
        this.children = children;
    }
}
