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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DlListingMapper {

    private final Logger log = LoggerFactory.getLogger(DlListingMapper.class);

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
        long start = System.currentTimeMillis();
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

        log.info("DlListing to DlListingDTO with id: {}, took: {} ms", dlListing.getId(), System.currentTimeMillis() - start);
        return dlListingDTO;
    }

    private void setDlListingContentFields(DlListing dlListing, DlListingDTO dlListingDTO, String languageCode) {
        Set<DlListingContentFieldRel> dlListingContentFieldRels = dlListing.getDlListingContentFieldRels();
        if (dlListingContentFieldRels != null) {
            for (DlListingContentFieldRel dlListingContentFieldRel : dlListingContentFieldRels) {
                DlContentField dlContentField = dlListingContentFieldRel.getDlContentField();
                setContentField(dlListingDTO, languageCode, dlListingContentFieldRel, dlContentField);
            }
        }
    }

    private void setContentField(DlListingDTO dlListingDTO, String languageCode, DlListingContentFieldRel dlListingContentFieldRel, DlContentField dlContentField) {
        if (dlContentField.getEnabled()) {

            DlContentFieldValue dlContentFieldValue;
            switch (dlContentField.getType()) {
                case CHECKBOX:
                    dlContentFieldValue = buildCheckboxValue(dlListingContentFieldRel, languageCode);
                    break;
                case SELECT:
                    dlContentFieldValue = buildSelectValue(dlListingContentFieldRel, languageCode);
                    break;
                case DEPENDENT_SELECT:
                    dlContentFieldValue = buildDependentSelectValue(dlListingContentFieldRel, languageCode);
                    break;
                case NUMBER_UNIT:
                    dlContentFieldValue = buildNumberUnitValue(dlListingContentFieldRel, languageCode);
                    break;
                default:
                    dlContentFieldValue = buildDefaultValue(dlListingContentFieldRel);
                    break;
            }

            dlListingDTO.addDlListingField(new DlListingFieldDTO()
                    .id(dlContentField.getId())
                    .type(dlContentField.getType().name())
                    .name(dlContentField.getName())
                    .translatedName(TranslateUtil.getTranslatedString(dlContentField, languageCode))
                    .value(dlContentFieldValue.getValue())
                    .selectedValue(dlContentFieldValue.getSelectedValue())
                    .translatedValue(dlContentFieldValue.getTranslatedValue())
                    .dlListingFieldItemDTOs(dlContentFieldValue.getDlContentFieldItemDTOS())
                    .dlContentFieldGroup(dlContentField.getDlContentFieldGroup() != null ? dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentField.getDlContentFieldGroup()) : null));
        }
    }

    private DlContentFieldValue buildCheckboxValue(DlListingContentFieldRel dlListingContentFieldRel, String languageCode) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
        List<Long> selectionIds = new ArrayList<>();
        if (dlContentFieldItems != null) {
            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                selectionIds.add(dlContentFieldItem.getId());
                dlContentFieldValue.addDlContentFieldItemDTOS(new DlListingFieldItemDTO()
                        .id(dlContentFieldItem.getId())
                        .value(dlContentFieldItem.getQlString().getValue())
                        .translatedValue(TranslateUtil.getTranslatedString(dlContentFieldItem, languageCode)));
            }
        }
        try {
            dlContentFieldValue.setSelectedValue(new ObjectMapper().writeValueAsString(selectionIds));
        } catch (JsonProcessingException e) {
            log.error("Error parsing selected value: {}, for DlListingContentFieldRel with id: {}", selectionIds, dlListingContentFieldRel.getId(), e);
        }

        return dlContentFieldValue;
    }

    private DlContentFieldValue buildSelectValue(DlListingContentFieldRel dlListingContentFieldRel, String languageCode) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
        if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
            dlContentFieldValue.setSelectedValue(String.valueOf(dlContentFieldItems.iterator().next().getId()));
            dlContentFieldValue.setTranslatedValue(TranslateUtil.getTranslatedString(dlContentFieldItems.iterator().next(), languageCode));
        }

        return dlContentFieldValue;
    }

    private DlContentFieldValue buildDependentSelectValue(DlListingContentFieldRel dlListingContentFieldRel, String languageCode) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
        if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
            DlContentFieldItem dlContentFieldItem = dlContentFieldItems.iterator().next();
            DlContentFieldItem parentDlContentFieldItem = dlContentFieldItem.getParent();
            String parentTranslatedValue = TranslateUtil.getTranslatedString(parentDlContentFieldItem, languageCode);
            dlContentFieldValue.setSelectedValue(String.valueOf(dlContentFieldItem.getId()));
            dlContentFieldValue.setTranslatedValue(parentTranslatedValue + " / " + TranslateUtil.getTranslatedString(dlContentFieldItem, languageCode));
        }

        return dlContentFieldValue;
    }

    private DlContentFieldValue buildNumberUnitValue(DlListingContentFieldRel dlListingContentFieldRel, String languageCode) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
        String value = dlListingContentFieldRel.getValue();
        dlContentFieldValue.setValue(value);

        if (value != null) {
            StringBuilder translatedValue = new StringBuilder(value);
            if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
                dlContentFieldValue.setSelectedValue(String.valueOf(dlContentFieldItems.iterator().next().getId()));
                translatedValue.append(" ").append(TranslateUtil.getTranslatedString(dlContentFieldItems.iterator().next(), languageCode));
            }

            dlContentFieldValue.setTranslatedValue(translatedValue.toString());
        }

        return dlContentFieldValue;
    }

    private DlContentFieldValue buildDefaultValue(DlListingContentFieldRel dlListingContentFieldRel) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        dlContentFieldValue.setValue(dlListingContentFieldRel.getValue());
        dlContentFieldValue.setTranslatedValue(dlListingContentFieldRel.getValue());

        return dlContentFieldValue;
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
                List<DlLocation> allByTranslationGroup = dlLocationRepository.findAllByTranslation_translationGroup(translationGroup);
                dlListingDTO.setTranslatedLocations(fillLocationTranslationsWithParents(allByTranslationGroup));
                setTranslatedTitle(dlLocationDTO, allByTranslationGroup, languageCode);
            }
        }
    }

    private void setTranslatedTitle(DlLocationDTO dlLocationDTO, List<DlLocation> allByTranslationGroup, String languageCode) {
        if (languageCode != null) {
            for (DlLocation dlLocation : allByTranslationGroup) {
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
                List<DlCategory> allByTranslationGroup = dlCategoryRepository.findAllByTranslation_translationGroup(translationGroup);
                dlListingDTO.setTranslatedCategories(fillCategoryTranslationsWithParents(allByTranslationGroup));
                setTranslatedTitle(dlCategoryDTO, allByTranslationGroup, languageCode);
            }
        }
    }

    private void setTranslatedTitle(DlCategoryDTO dlCategoryDTO, List<DlCategory> allByTranslationGroup, String languageCode) {
        if (languageCode != null) {
            for (DlCategory dlCategory : allByTranslationGroup) {
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

    class DlContentFieldValue {
        private String value = "";
        private String selectedValue = "";
        private String translatedValue = "";
        private List<DlListingFieldItemDTO> dlContentFieldItemDTOS = new ArrayList<>();

        String getValue() {
            return value;
        }

        void setValue(String value) {
            this.value = value;
        }

        String getSelectedValue() {
            return selectedValue;
        }

        void setSelectedValue(String selectedValue) {
            this.selectedValue = selectedValue;
        }

        String getTranslatedValue() {
            return translatedValue;
        }

        void setTranslatedValue(String translatedValue) {
            this.translatedValue = translatedValue;
        }

        List<DlListingFieldItemDTO> getDlContentFieldItemDTOS() {
            return dlContentFieldItemDTOS;
        }

        void addDlContentFieldItemDTOS(DlListingFieldItemDTO dlListingFieldItemDTO) {
            dlContentFieldItemDTOS.add(dlListingFieldItemDTO);
        }
    }

}
