package com.manev.quislisting.service.util;

import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;

import java.util.ArrayList;
import java.util.List;

public class AttachmentUtil {

    public static List<String> getFilePaths(AttachmentDTO attachmentDTO) {
        AttachmentMetadata attachmentMetadata = attachmentDTO.getAttachmentMetadata();
        List<AttachmentMetadata.ImageResizeMeta> imageResizeMetas = attachmentMetadata.getImageResizeMetas();

        List<String> filePaths = new ArrayList<>();
        filePaths.add(attachmentMetadata.getFile());
        for (AttachmentMetadata.ImageResizeMeta imageResizeMeta : imageResizeMetas) {
            filePaths.add(imageResizeMeta.getDetail().getFile());
        }
        return filePaths;
    }

}
