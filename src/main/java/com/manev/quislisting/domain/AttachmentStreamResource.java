package com.manev.quislisting.domain;

import org.springframework.core.io.Resource;

public class AttachmentStreamResource {
    private Resource resource;
    private String mimeType;
    private final String fileName;

    public AttachmentStreamResource(Resource resource, String mimeType, String fileName) {
        this.resource = resource;
        this.mimeType = mimeType;
        this.fileName = fileName;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }
}
