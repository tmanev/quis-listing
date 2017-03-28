package com.manev.quislisting.service.post.dto;

public class PostMetaDTO {
    private Long id;
    private String key;
    private String value;

    public PostMetaDTO() {
        // empty
    }

    public PostMetaDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public PostMetaDTO(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
