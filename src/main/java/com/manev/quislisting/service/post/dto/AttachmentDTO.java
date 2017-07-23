package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.service.dto.AttachmentMetadata;

public class AttachmentDTO {

    private Long id;
    private String fileName;
    private String fileNameSlug;
    private AttachmentMetadata attachmentMetadata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameSlug() {
        return fileNameSlug;
    }

    public void setFileNameSlug(String fileNameSlug) {
        this.fileNameSlug = fileNameSlug;
    }

    public AttachmentMetadata getAttachmentMetadata() {
        return attachmentMetadata;
    }

    public void setAttachmentMetadata(AttachmentMetadata attachmentMetadata) {
        this.attachmentMetadata = attachmentMetadata;
    }

}
