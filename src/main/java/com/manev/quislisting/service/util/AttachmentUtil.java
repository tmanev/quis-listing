package com.manev.quislisting.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttachmentUtil {

    private AttachmentUtil() {
        // hide the public constructor
    }

    public static List<String> getFilePaths(AttachmentDTO attachmentDTO) {
        AttachmentMetadata attachmentMetadata = attachmentDTO.getAttachmentMetadata();
        List<AttachmentMetadata.ImageResizeMeta> imageResizeMetas = attachmentMetadata.getImageResizeMetas();

        List<String> filePaths = new ArrayList<>();
        filePaths.add(attachmentMetadata.getDetail().getFile());
        for (AttachmentMetadata.ImageResizeMeta imageResizeMeta : imageResizeMetas) {
            filePaths.add(imageResizeMeta.getDetail().getFile());
        }
        return filePaths;
    }

    public static List<String> getFilePaths(Attachment attachment) throws IOException {
        String attachmentMetadataStr = attachment.getPostMetaValue(Attachment.QL_ATTACHMENT_METADATA);
        List<String> filePaths = new ArrayList<>();
        if (attachmentMetadataStr != null) {
            AttachmentMetadata attachmentMetadata = new ObjectMapper().readValue(attachmentMetadataStr,
                    AttachmentMetadata.class);
            filePaths.add(attachmentMetadata.getDetail().getFile());
            List<AttachmentMetadata.ImageResizeMeta> imageResizeMetas = attachmentMetadata.getImageResizeMetas();
            for (AttachmentMetadata.ImageResizeMeta imageResizeMeta : imageResizeMetas) {
                filePaths.add(imageResizeMeta.getDetail().getFile());
            }
        }
        return filePaths;
    }

}
