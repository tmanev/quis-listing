package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.domain.DlAttachmentResize;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import org.springframework.stereotype.Component;

@Component
public class DlAttachmentResizeMapper {

    DlAttachmentResize mapToDlAttachmentResize(AttachmentMetadata.ImageResizeMeta bigImageResizeMeta, DlAttachmentResize.SizeType sizeType, DlAttachment attachment) {
        DlAttachmentResize dlAttachmentResize = new DlAttachmentResize();
        dlAttachmentResize.setSizeType(sizeType);
        dlAttachmentResize.setPath(bigImageResizeMeta.getDetail().getFile());
        dlAttachmentResize.setMimeType(bigImageResizeMeta.getDetail().getMimeType());
        dlAttachmentResize.setWidth(bigImageResizeMeta.getDetail().getWidth());
        dlAttachmentResize.setHeight(bigImageResizeMeta.getDetail().getHeight());
        dlAttachmentResize.setSize(bigImageResizeMeta.getDetail().getSize());

        dlAttachmentResize.setDlAttachment(attachment);

        return dlAttachmentResize;
    }
}
