package com.manev.quislisting.service.dto;

import java.util.List;

public class FileUploadResponse {
    private List<FileMeta> files;

    public FileUploadResponse(List<FileMeta> files) {
        this.files = files;
    }

    public List<FileMeta> getFiles() {
        return files;
    }

    public void setFiles(List<FileMeta> files) {
        this.files = files;
    }
}
