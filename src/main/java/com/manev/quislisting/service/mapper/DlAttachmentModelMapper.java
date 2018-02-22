package com.manev.quislisting.service.mapper;

import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.model.AttachmentModel;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import org.springframework.stereotype.Component;

@Component
public class DlAttachmentModelMapper {

    public AttachmentModel convert(AttachmentDTO attachmentDTO) {
        AttachmentModel attachmentModel = new AttachmentModel();
        AttachmentMetadata attachmentMetadata = attachmentDTO.getAttachmentMetadata();
        attachmentModel.setSmallImage(attachmentMetadata.getSmallImageResizeMeta() != null ? attachmentMetadata.getSmallImageResizeMeta().getDetail().getFile() : null);
        attachmentModel.setOriginalImage(attachmentMetadata.getOriginalImageResizeMeta() != null ? attachmentMetadata.getOriginalImageResizeMeta().getDetail().getFile() : null);
        return attachmentModel;
    }

}
