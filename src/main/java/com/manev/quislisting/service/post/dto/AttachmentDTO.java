package com.manev.quislisting.service.post.dto;

import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.service.dto.AttachmentMetadata;

public class AttachmentDTO extends AbstractPostDTO {

    private String mimeType;
    private Attachment.Status status;
    private AttachmentMetadata attachmentMetadata;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Attachment.Status getStatus() {
        return status;
    }

    public void setStatus(Attachment.Status status) {
        this.status = status;
    }

    public AttachmentMetadata getAttachmentMetadata() {
        return attachmentMetadata;
    }

    public void setAttachmentMetadata(AttachmentMetadata attachmentMetadata) {
        this.attachmentMetadata = attachmentMetadata;
    }

}
