package com.manev.quislisting.service.model;

public class DlLocationModel {

    private DlLocationModel parent;
    private String location;
    private Long id;

    public DlLocationModel getParent() {
        return parent;
    }

    public void setParent(DlLocationModel parent) {
        this.parent = parent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
