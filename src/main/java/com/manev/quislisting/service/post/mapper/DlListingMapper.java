package com.manev.quislisting.service.post.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.DlListingContentFieldRel;
import com.manev.quislisting.domain.DlListingLocationRel;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.mapper.DlContentFieldGroupMapper;
import com.manev.quislisting.service.mapper.TranslateUtil;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingFieldDTO;
import com.manev.quislisting.service.post.dto.DlListingFieldItemDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.TranslatedTermDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DlListingMapper {

    private final DlCategoryRepository dlCategoryRepository;
    private final DlLocationRepository dlLocationRepository;
    private final ConversionService conversionService;
    private DlCategoryMapper dlCategoryMapper;
    private DlLocationMapper dlLocationMapper;
    private AttachmentMapper attachmentMapper;
    private DlContentFieldGroupMapper dlContentFieldGroupMapper;

    @Autowired
    public DlListingMapper(DlCategoryMapper dlCategoryMapper, DlLocationMapper dlLocationMapper, AttachmentMapper attachmentMapper, DlContentFieldGroupMapper dlContentFieldGroupMapper, DlCategoryRepository dlCategoryRepository, DlLocationRepository dlLocationRepository, ConversionService conversionService) {
        this.dlCategoryMapper = dlCategoryMapper;
        this.dlLocationMapper = dlLocationMapper;
        this.attachmentMapper = attachmentMapper;
        this.dlContentFieldGroupMapper = dlContentFieldGroupMapper;
        this.dlCategoryRepository = dlCategoryRepository;
        this.dlLocationRepository = dlLocationRepository;
        this.conversionService = conversionService;
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

        setDlCategories(dlListing, dlListingDTO, languageCode);

        setDlLocations(dlListing, dlListingDTO, languageCode);

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
                    if (dlContentField.getEnabled()) {
                        String value = "";
                        String selectedValue = "";
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
                            selectedValue = new ObjectMapper().writeValueAsString(selectionIds);
                        } else if (dlContentField.getType().equals(DlContentField.Type.SELECT)) {
                            Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
                            if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
                                selectedValue = String.valueOf(dlContentFieldItems.iterator().next().getId());
                                translatedValue = TranslateUtil.getTranslatedString(dlContentFieldItems.iterator().next(), languageCode);
                            }
                        } else if (dlContentField.getType().equals(DlContentField.Type.DEPENDENT_SELECT)) {
                            Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
                            if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
                                DlContentFieldItem dlContentFieldItem = dlContentFieldItems.iterator().next();
                                DlContentFieldItem parentDlContentFieldItem = dlContentFieldItem.getParent();
                                String parentTranslatedValue = TranslateUtil.getTranslatedString(parentDlContentFieldItem, languageCode);
                                selectedValue = String.valueOf(dlContentFieldItem.getId());
                                translatedValue = parentTranslatedValue + " / " + TranslateUtil.getTranslatedString(dlContentFieldItem, languageCode);
                            }
                        } else if (dlContentField.getType().equals(DlContentField.Type.NUMBER_UNIT)) {
                            Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
                            value = dlListingContentFieldRel.getValue();
                            translatedValue = dlListingContentFieldRel.getValue();
                            if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
                                selectedValue = String.valueOf(dlContentFieldItems.iterator().next().getId());
                                translatedValue += " " + TranslateUtil.getTranslatedString(dlContentFieldItems.iterator().next(), languageCode);
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
                                .selectedValue(selectedValue)
                                .translatedValue(translatedValue)
                                .dlListingFieldItemDTOs(dlContentFieldItemDTOS)
                                .dlContentFieldGroup(dlContentField.getDlContentFieldGroup() != null ? dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentField.getDlContentFieldGroup()) : null));
                    }
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

    private void setDlLocations(DlListing dlListing, DlListingDTO dlListingDTO, String languageCode) {
        Set<DlListingLocationRel> dlLocations = dlListing.getDlListingLocationRels();
        if (dlLocations != null && !dlLocations.isEmpty()) {
            for (DlListingLocationRel dlListingLocationRel : dlLocations) {
                DlLocationDTO dlLocationDTO = dlLocationMapper.dlLocationToDlLocationDTO(dlListingLocationRel.getDlLocation());
                dlListingDTO.addDlLocationDto(dlLocationDTO);
                TranslationGroup translationGroup = dlListingLocationRel.getDlLocation().getTranslation().getTranslationGroup();
                List<DlLocation> allByTranslation_translationGroup = dlLocationRepository.findAllByTranslation_translationGroup(translationGroup);
                dlListingDTO.setTranslatedLocations(fillLocationTranslationsWithParents(allByTranslation_translationGroup));
                setTranslatedTitle(dlLocationDTO, allByTranslation_translationGroup, languageCode);
            }
        }
    }

    private void setTranslatedTitle(DlLocationDTO dlLocationDTO, List<DlLocation> allByTranslation_translationGroup, String languageCode) {
        if (languageCode != null) {
            for (DlLocation dlLocation : allByTranslation_translationGroup) {
                if (dlLocation.getTranslation().getLanguageCode().equals(languageCode)) {
                    dlLocationDTO.setTranslatedName(dlLocation.getName());
                    break;
                }
            }
        }
    }

    private List<TranslatedTermDTO> fillLocationTranslationsWithParents(List<DlLocation> dlLocations) {
        Set<TranslatedTermDTO> result = new HashSet<>();

        for (DlLocation dlLocation : dlLocations) {
            addTranslatedLocation(dlLocation, result);
        }

        return new ArrayList<>(result);
    }

    private void addTranslatedLocation(DlLocation dlLocation, Set<TranslatedTermDTO> result) {
        if (dlLocation.getParent() != null) {
            addTranslatedLocation(dlLocation.getParent(), result);
        }
        result.add(conversionService.convert(dlLocation, TranslatedTermDTO.class));
    }


    private void setDlCategories(DlListing dlListing, DlListingDTO dlListingDTO, String languageCode) {
        Set<DlCategory> dlCategories = dlListing.getDlCategories();
        if (dlCategories != null && !dlCategories.isEmpty()) {
            for (DlCategory dlCategory : dlCategories) {
                DlCategoryDTO dlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);
                dlListingDTO.addDlCategoryDto(dlCategoryDTO);
                TranslationGroup translationGroup = dlCategory.getTranslation().getTranslationGroup();
                List<DlCategory> allByTranslation_translationGroup = dlCategoryRepository.findAllByTranslation_translationGroup(translationGroup);
                dlListingDTO.setTranslatedCategories(fillCategoryTranslationsWithParents(allByTranslation_translationGroup));
                setTranslatedTitle(dlCategoryDTO, allByTranslation_translationGroup, languageCode);
            }
        }
    }

    private void setTranslatedTitle(DlCategoryDTO dlCategoryDTO, List<DlCategory> allByTranslation_translationGroup, String languageCode) {
        if (languageCode != null) {
            for (DlCategory dlCategory : allByTranslation_translationGroup) {
                if (dlCategory.getTranslation().getLanguageCode().equals(languageCode)) {
                    dlCategoryDTO.setTranslatedName(dlCategory.getName());
                    break;
                }
            }
        }
    }

    private List<TranslatedTermDTO> fillCategoryTranslationsWithParents(List<DlCategory> dlCategories) {
        Set<TranslatedTermDTO> result = new HashSet<>();

        for (DlCategory dlCategory : dlCategories) {
            addTranslatedCategory(dlCategory, result);
        }

        return new ArrayList<>(result);
    }

    private void addTranslatedCategory(DlCategory dlCategory, Set<TranslatedTermDTO> result) {
        if (dlCategory.getParent() != null) {
            addTranslatedCategory(dlCategory.getParent(), result);
        }
        result.add(conversionService.convert(dlCategory, TranslatedTermDTO.class));
    }

    private void setAuthor(DlListing dlListing, DlListingDTO dlListingDTO) {
        User user = dlListing.getUser();
        dlListingDTO.setAuthor(new UserDTO(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName()));
    }

}
