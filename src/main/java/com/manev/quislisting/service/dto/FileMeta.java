package com.manev.quislisting.service.dto;

public class FileMeta {

    private String name;
    private Long size;
    private String url;
    private String thumbnailUrl;
    private String deleteUrl;
    private String deleteType;

    public FileMeta(String name, Long size, String url, String thumbnailUrl, String deleteUrl, String deleteType) {
        this.name = name;
        this.size = size;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.deleteUrl = deleteUrl;
        this.deleteType = deleteType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }
}
