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

        List<String> filePaths = new ArrayList<>();
        filePaths.add(attachmentMetadata.getDetail().getFile());

        AttachmentMetadata.ImageResizeMeta thumbnailImageResizeMeta = attachmentMetadata.getThumbnailImageResizeMeta();
        AttachmentMetadata.ImageResizeMeta mediumImageResizeMeta = attachmentMetadata.getMediumImageResizeMeta();
        AttachmentMetadata.ImageResizeMeta bigImageResizeMeta = attachmentMetadata.getBigImageResizeMeta();

        addIfNotNull(filePaths, thumbnailImageResizeMeta);
        addIfNotNull(filePaths, mediumImageResizeMeta);
        addIfNotNull(filePaths, bigImageResizeMeta);

        return filePaths;
    }

    private static void addIfNotNull(List<String> filePaths, AttachmentMetadata.ImageResizeMeta imageResizeMeta) {
        if (imageResizeMeta != null) {
            filePaths.add(imageResizeMeta.getDetail().getFile());
        }
    }

    public static List<String> getFilePaths(Attachment attachment) throws IOException {
        String attachmentMetadataStr = attachment.getPostMetaValue(Attachment.QL_ATTACHMENT_METADATA);
        List<String> filePaths = new ArrayList<>();
        if (attachmentMetadataStr != null) {
            AttachmentMetadata attachmentMetadata = new ObjectMapper().readValue(attachmentMetadataStr,
                    AttachmentMetadata.class);
            filePaths.add(attachmentMetadata.getDetail().getFile());
            AttachmentMetadata.ImageResizeMeta thumbnailImageResizeMeta = attachmentMetadata.getThumbnailImageResizeMeta();
            AttachmentMetadata.ImageResizeMeta mediumImageResizeMeta = attachmentMetadata.getMediumImageResizeMeta();
            AttachmentMetadata.ImageResizeMeta bigImageResizeMeta = attachmentMetadata.getBigImageResizeMeta();

            addIfNotNull(filePaths, thumbnailImageResizeMeta);
            addIfNotNull(filePaths, mediumImageResizeMeta);
            addIfNotNull(filePaths, bigImageResizeMeta);
        }
        return filePaths;
    }

}
