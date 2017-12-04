package com.manev.quislisting.service.post.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.DlListingContentFieldRel;
import com.manev.quislisting.domain.DlListingLocationRel;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.mapper.TranslateUtil;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingFieldDTO;
import com.manev.quislisting.service.post.dto.DlListingFieldItemDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DlListingMapper {

    private DlCategoryMapper dlCategoryMapper;
    private DlLocationMapper dlLocationMapper;
    private AttachmentMapper attachmentMapper;

    public DlListingMapper(DlCategoryMapper dlCategoryMapper, DlLocationMapper dlLocationMapper, AttachmentMapper attachmentMapper) {
        this.dlCategoryMapper = dlCategoryMapper;
        this.dlLocationMapper = dlLocationMapper;
        this.attachmentMapper = attachmentMapper;
    }

    public DlListingDTO dlListingToDlListingDTO(DlListing dlListing, String languageCode) {
        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setId(dlListing.getId());
        dlListingDTO.setTitle(dlListing.getTitle());
        dlListingDTO.setName(dlListing.getName());
        dlListingDTO.setContent(dlListing.getContent());
        dlListingDTO.setCreated(dlListing.getCreated());
        dlListingDTO.setModified(dlListing.getModified());
        dlListingDTO.setStatus(dlListing.getStatus());
        dlListingDTO.setApproved(dlListing.getApproved());
        dlListingDTO.setLanguageCode(dlListing.getTranslation().getLanguageCode());

        setDlCategories(dlListing, dlListingDTO);

        setDlLocations(dlListing, dlListingDTO);

        dlListingDTO.setFeaturedAttachment(dlListing.getFeaturedAttachment() != null ? attachmentMapper.attachmentToAttachmentDTO(dlListing.getFeaturedAttachment()) : null);
        setAttachments(dlListing, dlListingDTO);

        setAuthor(dlListing, dlListingDTO);

        setDlListingContentFields(dlListing, dlListingDTO, languageCode);

        return dlListingDTO;
    }

    private void setDlListingContentFields(DlListing dlListing, DlListingDTO dlListingDTO, String languageCode) {
        try {
            Set<DlListingContentFieldRel> dlListingContentFieldRels = dlListing.getDlListingContentFieldRels();
            if (dlListingContentFieldRels != null) {
                for (DlListingContentFieldRel dlListingContentFieldRel : dlListingContentFieldRels) {
                    DlContentField dlContentField = dlListingContentFieldRel.getDlContentField();
                    String value = "";
                    String translatedValue = "";
                    List<DlListingFieldItemDTO> dlContentFieldItemDTOS = new ArrayList<>();
                    if (dlContentField.getType().equals(DlContentField.Type.CHECKBOX)) {
                        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
                        List<Long> selectionIds = new ArrayList<>();
                        if (dlContentFieldItems != null) {
                            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                                selectionIds.add(dlContentFieldItem.getId());
                                dlContentFieldItemDTOS.add(new DlListingFieldItemDTO()
                                        .id(dlContentFieldItem.getId())
                                        .value(dlContentFieldItem.getQlString().getValue())
                                        .translatedValue(TranslateUtil.getTranslatedString(dlContentFieldItem, languageCode)));
                            }
                        }
                        value = new ObjectMapper().writeValueAsString(selectionIds);
                    } else if (dlContentField.getType().equals(DlContentField.Type.SELECT)) {
                        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
                        if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
                            value = String.valueOf(dlContentFieldItems.iterator().next().getId());
                            translatedValue = TranslateUtil.getTranslatedString(dlContentFieldItems.iterator().next(), languageCode);
                        }
                    } else if (dlContentField.getType().equals(DlContentField.Type.DEPENDENT_SELECT)) {
                        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
                        if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
                            DlContentFieldItem dlContentFieldItem = dlContentFieldItems.iterator().next();
                            DlContentFieldItem parentDlContentFieldItem = dlContentFieldItem.getParent();
                            String parentTranslatedValue = TranslateUtil.getTranslatedString(parentDlContentFieldItem, languageCode);
                            value = String.valueOf(dlContentFieldItem.getId());
                            translatedValue = parentTranslatedValue + " / " + TranslateUtil.getTranslatedString(dlContentFieldItem, languageCode);
                        }
                    } else {
                        value = dlListingContentFieldRel.getValue();
                        translatedValue = dlListingContentFieldRel.getValue();
                    }
                    dlListingDTO.addDlListingField(new DlListingFieldDTO()
                            .id(dlContentField.getId())
                            .type(dlContentField.getType().name())
                            .name(dlContentField.getName())
                            .translatedName(TranslateUtil.getTranslatedString(dlContentField, languageCode))
                            .value(value)
                            .translatedValue(translatedValue)
                            .dlListingFieldItemDTOs(dlContentFieldItemDTOS));

                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void setAttachments(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<DlAttachment> dlAttachments = dlListing.getDlAttachments();
        if (dlAttachments != null && !dlAttachments.isEmpty()) {
            for (DlAttachment attachment : dlAttachments) {
                dlListingDTO.addAttachmentDto(attachmentMapper.attachmentToAttachmentDTO(attachment));
            }
        }
    }

    private void setDlLocations(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<DlListingLocationRel> dlLocations = dlListing.getDlListingLocationRels();
        if (dlLocations != null && !dlLocations.isEmpty()) {
            for (DlListingLocationRel dlListingLocationRel : dlLocations) {
                dlListingDTO.addDlLocationDto(dlLocationMapper.dlLocationToDlLocationDTO(dlListingLocationRel.getDlLocation()));
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
        dlListingDTO.setAuthor(new UserDTO(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName()));
    }


}
