package com.manev.quislisting.service.post.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.PostMeta;
import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.Author;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.manev.quislisting.domain.post.discriminator.Attachment.QL_ATTACHED_FILE;
import static com.manev.quislisting.domain.post.discriminator.Attachment.QL_ATTACHMENT_METADATA;

@Component
public class AttachmentMapper {

    private final Logger log = LoggerFactory.getLogger(AttachmentMapper.class);

    public Attachment attachmentDTOToAttachment(AttachmentDTO attachmentDTO) {
        Attachment attachment = null;
        try {
            attachment = new Attachment();
            attachment.setId(attachmentDTO.getId());
            attachment.setName(attachmentDTO.getName());
            attachment.setTitle(attachmentDTO.getTitle());
            attachment.setMimeType(attachmentDTO.getMimeType());
            attachment.setStatus(attachmentDTO.getStatus());
            attachment.setCreated(attachmentDTO.getCreated());
            attachment.setModified(attachmentDTO.getModified());

            attachment.addPostMeta(new PostMeta(attachment, QL_ATTACHED_FILE, attachmentDTO.getAttachmentMetadata().getFile()));
            attachment.addPostMeta(new PostMeta(attachment, QL_ATTACHMENT_METADATA, new ObjectMapper().writeValueAsString(attachmentDTO.getAttachmentMetadata())));
        } catch (IOException e) {
            log.error("Error writing Obj to Json", e);
        }

        return attachment;
    }

    public AttachmentDTO attachmentToAttachmentDTO(Attachment attachment) {
        AttachmentDTO attachmentDTO = null;
        try {
            attachmentDTO = new AttachmentDTO();
            attachmentDTO.setId(attachment.getId());
            attachmentDTO.setName(attachment.getName());
            attachmentDTO.setTitle(attachment.getTitle());
            attachmentDTO.setCreated(attachment.getCreated());
            attachmentDTO.setModified(attachment.getModified());
            attachmentDTO.setMimeType(attachment.getMimeType());
            attachmentDTO.setStatus(attachment.getStatus());

            String attachmentMetadataStr = attachment.getPostMetaValue(QL_ATTACHMENT_METADATA);
            if (attachmentMetadataStr != null) {
                AttachmentMetadata attachmentMetadata = new ObjectMapper().readValue(attachmentMetadataStr,
                        AttachmentMetadata.class);
                attachmentDTO.setAttachmentMetadata(attachmentMetadata);
            }

            setAuthor(attachment, attachmentDTO);
        } catch (IOException e) {
            log.error("Error reading Json string to Obj", e);
        }

        return attachmentDTO;
    }

    private void setAuthor(Attachment attachment, AttachmentDTO attachmentDTO) {
        User user = attachment.getUser();

        attachmentDTO.setAuthor(new Author(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName()));
    }
}
