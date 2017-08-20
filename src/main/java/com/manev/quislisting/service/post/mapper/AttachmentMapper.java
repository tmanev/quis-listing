package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.domain.DlAttachmentResize;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AttachmentMapper {

    private DlAttachmentResizeMapper dlAttachmentResizeMapper;

    public AttachmentMapper(DlAttachmentResizeMapper dlAttachmentResizeMapper) {
        this.dlAttachmentResizeMapper = dlAttachmentResizeMapper;
    }

    public DlAttachment attachmentDTOToAttachment(AttachmentDTO attachmentDTO) {
        DlAttachment attachment = new DlAttachment();
        attachment.setId(attachmentDTO.getId());
        attachment.setFileName(attachmentDTO.getFileName());
        attachment.setFileNameSlug(attachmentDTO.getFileNameSlug());
        attachment.setMimeType(attachmentDTO.getAttachmentMetadata().getDetail().getMimeType());
        attachment.setSize(attachmentDTO.getAttachmentMetadata().getDetail().getSize());
        attachment.setWidth(attachmentDTO.getAttachmentMetadata().getDetail().getWidth());
        attachment.setHeight(attachmentDTO.getAttachmentMetadata().getDetail().getHeight());
        attachment.setPath(attachmentDTO.getAttachmentMetadata().getDetail().getFile());

        AttachmentMetadata attachmentMetadata = attachmentDTO.getAttachmentMetadata();

        if (attachmentMetadata.getLargeImageResizeMeta() != null) {
            DlAttachmentResize dlAttachmentResizeBig = dlAttachmentResizeMapper.mapToDlAttachmentResize(attachmentMetadata.getLargeImageResizeMeta(), DlAttachmentResize.SizeType.BIG, attachment);
            attachment.addDlAttachmentResize(dlAttachmentResizeBig);
        }

        if (attachmentMetadata.getMediumImageResizeMeta() != null) {
            DlAttachmentResize dlAttachmentResizeMedium = dlAttachmentResizeMapper.mapToDlAttachmentResize(attachmentMetadata.getMediumImageResizeMeta(), DlAttachmentResize.SizeType.MEDIUM, attachment);
            attachment.addDlAttachmentResize(dlAttachmentResizeMedium);
        }

        if (attachmentMetadata.getSmallImageResizeMeta() != null) {
            DlAttachmentResize dlAttachmentResizeSmall = dlAttachmentResizeMapper.mapToDlAttachmentResize(attachmentMetadata.getSmallImageResizeMeta(), DlAttachmentResize.SizeType.SMALL, attachment);
            attachment.addDlAttachmentResize(dlAttachmentResizeSmall);
        }

        return attachment;
    }

    public AttachmentDTO attachmentToAttachmentDTO(DlAttachment attachment) {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        AttachmentMetadata attachmentMetadata = new AttachmentMetadata();
        AttachmentMetadata.DetailSize detailSize = new AttachmentMetadata.DetailSize();
        attachmentMetadata.setDetail(detailSize);

        attachmentDTO.setId(attachment.getId());
        attachmentDTO.setFileName(attachment.getFileName());
        attachmentDTO.setFileNameSlug(attachment.getFileNameSlug());

        detailSize.setMimeType(attachment.getMimeType());
        detailSize.setWidth(attachment.getWidth());
        detailSize.setHeight(attachment.getHeight());
        detailSize.setSize(attachment.getSize());
        detailSize.setFile(attachment.getPath());

        Set<DlAttachmentResize> dlAttachmentResizes = attachment.getDlAttachmentResizes();
        if (dlAttachmentResizes != null) {
            for (DlAttachmentResize dlAttachmentResize : dlAttachmentResizes) {
                switch (dlAttachmentResize.getSizeType()) {
                    case BIG:
                        attachmentMetadata.setLargeImageResizeMeta(createImageResizeMeta(DlAttachmentResize.SizeType.BIG.name(), dlAttachmentResize));
                        break;
                    case MEDIUM:
                        attachmentMetadata.setMediumImageResizeMeta(createImageResizeMeta(DlAttachmentResize.SizeType.MEDIUM.name(), dlAttachmentResize));
                        break;
                    case SMALL:
                        attachmentMetadata.setSmallImageResizeMeta(createImageResizeMeta(DlAttachmentResize.SizeType.SMALL.name(), dlAttachmentResize));
                        break;
                    default:
                        throw new RuntimeException("This should not happen");
                }
            }
        }
        attachmentDTO.setAttachmentMetadata(attachmentMetadata);

        return attachmentDTO;
    }

    private AttachmentMetadata.ImageResizeMeta createImageResizeMeta(String name, DlAttachmentResize dlAttachmentResize) {
        AttachmentMetadata.ImageResizeMeta imageResizeMeta = new AttachmentMetadata.ImageResizeMeta();
        imageResizeMeta.setName(name);
        AttachmentMetadata.DetailSize detail = new AttachmentMetadata.DetailSize();
        detail.setFile(dlAttachmentResize.getPath());
        detail.setWidth(dlAttachmentResize.getWidth());
        detail.setHeight(dlAttachmentResize.getHeight());
        detail.setSize(dlAttachmentResize.getSize());
        detail.setMimeType(dlAttachmentResize.getMimeType());
        imageResizeMeta.setDetail(detail);
        return imageResizeMeta;
    }

}
