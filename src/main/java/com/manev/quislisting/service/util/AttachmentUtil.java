package com.manev.quislisting.service.util;

import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.domain.DlAttachmentResize;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.util.CollectionUtils;

public class AttachmentUtil {

    private AttachmentUtil() {
        // hide the public constructor
    }

    public static List<String> getFilePaths(AttachmentDTO attachmentDTO) {
        AttachmentMetadata attachmentMetadata = attachmentDTO.getAttachmentMetadata();

        List<String> filePaths = new ArrayList<>();
        filePaths.add(attachmentMetadata.getDetail().getFile());

        AttachmentMetadata.ImageResizeMeta thumbnailImageResizeMeta = attachmentMetadata.getSmallImageResizeMeta();
        AttachmentMetadata.ImageResizeMeta bigImageResizeMeta = attachmentMetadata.getOriginalImageResizeMeta();

        addIfNotNull(filePaths, thumbnailImageResizeMeta);
        addIfNotNull(filePaths, bigImageResizeMeta);

        return filePaths;
    }

    private static void addIfNotNull(List<String> filePaths, AttachmentMetadata.ImageResizeMeta imageResizeMeta) {
        if (imageResizeMeta != null) {
            filePaths.add(imageResizeMeta.getDetail().getFile());
        }
    }

    public static List<String> getFilePaths(DlAttachment attachment) {
        Set<DlAttachmentResize> dlAttachmentResizes = attachment.getDlAttachmentResizes();
        List<String> filePaths = new ArrayList<>();
        filePaths.add(attachment.getPath());
        if (!CollectionUtils.isEmpty(dlAttachmentResizes)) {
            for (DlAttachmentResize dlAttachmentResize : dlAttachmentResizes) {
                filePaths.add(dlAttachmentResize.getPath());
            }
        }
        return filePaths;
    }

}
