package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.PostMeta;
import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.service.post.dto.Author;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingField;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DlListingMapper {

    private final Logger log = LoggerFactory.getLogger(DlListingMapper.class);

    private DlCategoryMapper dlCategoryMapper;
    private DlLocationMapper dlLocationMapper;
    private AttachmentMapper attachmentMapper;

    public DlListingMapper(DlCategoryMapper dlCategoryMapper, DlLocationMapper dlLocationMapper, AttachmentMapper attachmentMapper) {
        this.dlCategoryMapper = dlCategoryMapper;
        this.dlLocationMapper = dlLocationMapper;
        this.attachmentMapper = attachmentMapper;
    }

    public DlListingDTO dlListingToDlListingDTO(DlListing dlListing) {
        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setId(dlListing.getId());
        dlListingDTO.setTitle(dlListing.getTitle());
        dlListingDTO.setName(dlListing.getName());
        dlListingDTO.setContent(dlListing.getContent());
        dlListingDTO.setCreated(dlListing.getCreated());
        dlListingDTO.setModified(dlListing.getModified());
        dlListingDTO.setStatus(dlListing.getStatus());
        dlListingDTO.setViews(dlListing.getPostMetaValue(PostMeta.META_KEY_POST_VIEWS_COUNT));
        dlListingDTO.setLanguageCode(dlListing.getTranslation().getLanguageCode());

        setExpirationDate(dlListing, dlListingDTO);

        setDlCategories(dlListing, dlListingDTO);

        setDlLocations(dlListing, dlListingDTO);

        setAttachments(dlListing, dlListingDTO);

        setPostMetadata(dlListing, dlListingDTO);

        setAuthor(dlListing, dlListingDTO);

        return dlListingDTO;
    }

    private void setPostMetadata(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<PostMeta> postMeta = dlListing.getPostMeta();
        if (postMeta != null) {
            for (PostMeta meta : postMeta) {
                if (meta.getKey().startsWith("content_field_")) {
                    String contentFieldId = meta.getKey().split("content_field_")[1];
                    dlListingDTO.addDlListingField(new DlListingField(Long.valueOf(contentFieldId), meta.getValue()));
                }
            }
        }
    }

    private void setAttachments(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<Attachment> attachments = dlListing.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                dlListingDTO.addAttachmentDto(attachmentMapper.attachmentToAttachmentDTO(attachment));
            }
        }
    }

    private void setDlLocations(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<DlLocation> dlLocations = dlListing.getDlLocations();
        if (dlLocations != null && !dlLocations.isEmpty()) {
            for (DlLocation dlLocation : dlLocations) {
                dlListingDTO.addDlLocationDto(dlLocationMapper.dlLocationToDlLocationDTO(dlLocation));
            }
        }
    }

    private void setDlCategories(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<DlCategory> dlCategories = dlListing.getDlCategories();
        if (dlCategories != null && !dlCategories.isEmpty()) {
            for (DlCategory dlCategory : dlCategories) {
                dlListingDTO.addDlCategoryDto(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory));
            }
        }
    }

    private void setAuthor(DlListing dlListing, DlListingDTO dlListingDTO) {
        User user = dlListing.getUser();

        dlListingDTO.setAuthor(new Author(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName()));
    }

    private void setExpirationDate(DlListing dlListing, DlListingDTO dlListingDTO) {
        String expirationDate = dlListing.getPostMetaValue(PostMeta.META_KEY_EXPIRATION_DATE);
        if (expirationDate != null) {
            try {
                dlListingDTO.setExpirationDate(expirationDate);
            } catch (NumberFormatException ex) {
                log.error("Expiration date : {} not a number", expirationDate, ex);
            } catch (IllegalArgumentException ex) {
                log.error("Expiration date : {} is a invalid argument", expirationDate, ex);
            }
        }
    }


}
