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
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.mapper.DlContentFieldGroupMapper;
import com.manev.quislisting.service.mapper.TranslateUtil;
import com.manev.quislisting.service.model.QlStringTranslationModel;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
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
import org.springframework.util.CollectionUtils;

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

    public DlListingDTO dlListingToDlListingDTO(DlListing dlListing) {
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
        dlListingDTO.setPrice(dlListing.getPrice());
        dlListingDTO.setPriceCurrency(dlListing.getPriceCurrency());
        dlListingDTO.setLanguageCode(dlListing.getTranslation().getLanguageCode());

        setDlCategories(dlListing, dlListingDTO);

        setDlLocations(dlListing, dlListingDTO);

        setFeaturedAttachment(dlListingDTO, dlListing);
        setAttachments(dlListing, dlListingDTO);

        setAuthor(dlListing, dlListingDTO);

        setDlListingContentFields(dlListing, dlListingDTO);

        log.info("DlListing to DlListingDTO with id: {}, took: {} ms", dlListing.getId(), System.currentTimeMillis() - start);
        return dlListingDTO;
    }

    private void setDlListingContentFields(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<DlListingContentFieldRel> dlListingContentFieldRels = dlListing.getDlListingContentFieldRels();
        if (!CollectionUtils.isEmpty(dlListingContentFieldRels)) {
            for (DlListingContentFieldRel dlListingContentFieldRel : dlListingContentFieldRels) {
                DlContentField dlContentField = dlListingContentFieldRel.getDlContentField();
                setContentField(dlListingDTO, dlListingContentFieldRel, dlContentField);
            }
        }
    }

    private void setContentField(DlListingDTO dlListingDTO, DlListingContentFieldRel dlListingContentFieldRel, DlContentField dlContentField) {
        if (dlContentField.getEnabled()) {

            DlContentFieldValue dlContentFieldValue;
            switch (dlContentField.getType()) {
                case CHECKBOX:
                    dlContentFieldValue = buildCheckboxValue(dlListingContentFieldRel);
                    break;
                case SELECT:
                    dlContentFieldValue = buildSelectValue(dlListingContentFieldRel);
                    break;
                case DEPENDENT_SELECT:
                    dlContentFieldValue = buildDependentSelectValue(dlListingContentFieldRel);
                    break;
                case NUMBER_UNIT:
                    dlContentFieldValue = buildNumberUnitValue(dlListingContentFieldRel);
                    break;
                default:
                    dlContentFieldValue = buildDefaultValue(dlListingContentFieldRel);
                    break;
            }

            DlListingFieldDTO dlListingFieldDTO = new DlListingFieldDTO()
                    .id(dlContentField.getId())
                    .type(dlContentField.getType().name())
                    .name(dlContentField.getName())
                    .value(dlContentFieldValue.getValue())
                    .selectedValue(dlContentFieldValue.getSelectedValue())
                    .dlListingFieldItemDTOs(dlContentFieldValue.getDlContentFieldItemDTOS())
                    .dlContentFieldGroup(dlContentField.getDlContentFieldGroup() != null ? dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentField.getDlContentFieldGroup()) : null);
            setTranslatedNames(dlListingFieldDTO, dlContentField);
            dlListingFieldDTO.setTranslatedValues(dlContentFieldValue.getTranslatedValues());
            dlListingDTO.addDlListingField(dlListingFieldDTO);
        }
    }

    private void setTranslatedNames(DlListingFieldDTO dlListingFieldDTO, DlContentField dlContentField) {
        QlString qlString = dlContentField.getQlString();
        dlListingFieldDTO.setTranslatedNames(getTranslations(qlString));
    }

    private DlContentFieldValue buildCheckboxValue(DlListingContentFieldRel dlListingContentFieldRel) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
        List<Long> selectionIds = new ArrayList<>();
        if (dlContentFieldItems != null) {
            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                selectionIds.add(dlContentFieldItem.getId());
                QlString qlString = dlContentFieldItem.getQlString();
                DlListingFieldItemDTO dlListingFieldItemDTO = new DlListingFieldItemDTO()
                        .id(dlContentFieldItem.getId())
                        .value(qlString.getValue());
                dlContentFieldValue.addDlContentFieldItemDTOS(dlListingFieldItemDTO);
                dlListingFieldItemDTO.setTranslatedValues(getTranslations(qlString));
            }
        }
        try {
            dlContentFieldValue.setSelectedValue(new ObjectMapper().writeValueAsString(selectionIds));
        } catch (JsonProcessingException e) {
            log.error("Error parsing selected value: {}, for DlListingContentFieldRel with id: {}", selectionIds, dlListingContentFieldRel.getId(), e);
        }

        return dlContentFieldValue;
    }

    private DlContentFieldValue buildSelectValue(DlListingContentFieldRel dlListingContentFieldRel) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
        if (!CollectionUtils.isEmpty(dlContentFieldItems)) {
            DlContentFieldItem dlContentFieldItem = dlContentFieldItems.iterator().next();
            QlString qlString = dlContentFieldItem.getQlString();
            DlListingFieldItemDTO dlListingFieldItemDTO = new DlListingFieldItemDTO()
                    .id(dlContentFieldItem.getId())
                    .value(qlString.getValue());
            dlListingFieldItemDTO.setTranslatedValues(getTranslations(qlString));
            dlContentFieldValue.addDlContentFieldItemDTOS(dlListingFieldItemDTO);
            dlContentFieldValue.setSelectedValue(String.valueOf(dlContentFieldItems.iterator().next().getId()));
            dlContentFieldValue.setTranslatedValues(new ArrayList<>());
        }

        return dlContentFieldValue;
    }

    private DlContentFieldValue buildDependentSelectValue(DlListingContentFieldRel dlListingContentFieldRel) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
        if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
            DlContentFieldItem dlContentFieldItem = dlContentFieldItems.iterator().next();
            DlContentFieldItem parentDlContentFieldItem = dlContentFieldItem.getParent();
            dlContentFieldValue.setSelectedValue(String.valueOf(dlContentFieldItem.getId()));

            QlString qlString = dlContentFieldItem.getQlString();

            List<QlStringTranslationModel> qlStringModels = new ArrayList<>();
            Set<StringTranslation> stringTranslation = qlString.getStringTranslation();
            if (!CollectionUtils.isEmpty(stringTranslation)) {
                for (StringTranslation translation : stringTranslation) {
                    QlStringTranslationModel qlStringTranslationModel = new QlStringTranslationModel();
                    qlStringTranslationModel.setId(translation.getId());
                    qlStringTranslationModel.setLanguageCode(translation.getLanguageCode());
                    qlStringTranslationModel.setValue(TranslateUtil.getTranslatedString(parentDlContentFieldItem, translation.getLanguageCode()) + " / " + translation.getValue());

                    qlStringModels.add(qlStringTranslationModel);
                }
            }

            DlListingFieldItemDTO dlListingFieldItemDTO = new DlListingFieldItemDTO()
                    .id(dlContentFieldItem.getId())
                    .value(parentDlContentFieldItem.getQlString().getValue() + " / " + qlString.getValue());
            dlListingFieldItemDTO.setTranslatedValues(qlStringModels);
            dlContentFieldValue.addDlContentFieldItemDTOS(dlListingFieldItemDTO);

            dlContentFieldValue.setTranslatedValues(new ArrayList<>());
        }

        return dlContentFieldValue;
    }

    private DlContentFieldValue buildNumberUnitValue(DlListingContentFieldRel dlListingContentFieldRel) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        Set<DlContentFieldItem> dlContentFieldItems = dlListingContentFieldRel.getDlContentFieldItems();
        String value = dlListingContentFieldRel.getValue();

        if (!CollectionUtils.isEmpty(dlContentFieldItems)) {
            DlContentFieldItem dlContentFieldItem = dlContentFieldItems.iterator().next();
            dlContentFieldValue.setSelectedValue(String.valueOf(dlContentFieldItems.iterator().next().getId()));

            DlListingFieldItemDTO dlListingFieldItemDTO = new DlListingFieldItemDTO();
            dlListingFieldItemDTO.setId(dlContentFieldItem.getId());
            dlListingFieldItemDTO.setValue(dlContentFieldItem.getValue());
            dlListingFieldItemDTO.setTranslatedValues(getTranslations(dlContentFieldItem.getQlString()));

            dlContentFieldValue.addDlContentFieldItemDTOS(dlListingFieldItemDTO);
        }

        dlContentFieldValue.setValue(value);
        dlContentFieldValue.setTranslatedValues(new ArrayList<>());

        return dlContentFieldValue;
    }

    private DlContentFieldValue buildDefaultValue(DlListingContentFieldRel dlListingContentFieldRel) {
        DlContentFieldValue dlContentFieldValue = new DlContentFieldValue();

        dlContentFieldValue.setValue(dlListingContentFieldRel.getValue());

        return dlContentFieldValue;
    }

    private void setAttachments(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<DlAttachment> dlAttachments = dlListing.getDlAttachments();
        List<AttachmentDTO> attachmentDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dlAttachments)) {
            for (DlAttachment attachment : dlAttachments) {
                attachmentDTOS.add(attachmentMapper.attachmentToAttachmentDTO(attachment));
            }
        }
        dlListingDTO.setAttachments(attachmentDTOS);
    }

    private void setDlLocations(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<DlListingLocationRel> dlLocations = dlListing.getDlListingLocationRels();
        if (!CollectionUtils.isEmpty(dlLocations)) {
            for (DlListingLocationRel dlListingLocationRel : dlLocations) {
                DlLocationDTO dlLocationDTO = dlLocationMapper.dlLocationToDlLocationDTO(dlListingLocationRel.getDlLocation());
                dlListingDTO.addDlLocationDto(dlLocationDTO);
                TranslationGroup translationGroup = dlListingLocationRel.getDlLocation().getTranslation().getTranslationGroup();
                List<DlLocation> allByTranslationGroup = dlLocationRepository.findAllByTranslation_translationGroup(translationGroup);
                List<TranslatedTermDTO> translatedLocations = fillLocationTranslationsWithParents(allByTranslationGroup);
                for (TranslatedTermDTO translatedLocation : translatedLocations) {
                    dlListingDTO.addTranslatedLocation(translatedLocation);
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


    private void setDlCategories(DlListing dlListing, DlListingDTO dlListingDTO) {
        Set<DlCategory> dlCategories = dlListing.getDlCategories();
        if (dlCategories != null && !dlCategories.isEmpty()) {
            for (DlCategory dlCategory : dlCategories) {
                DlCategoryDTO dlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);
                dlListingDTO.addDlCategoryDto(dlCategoryDTO);
                TranslationGroup translationGroup = dlCategory.getTranslation().getTranslationGroup();
                List<DlCategory> allByTranslationGroup = dlCategoryRepository.findAllByTranslation_translationGroup(translationGroup);
                List<TranslatedTermDTO> translatedCategories = fillCategoryTranslationsWithParents(allByTranslationGroup);
                for (TranslatedTermDTO translatedCategory : translatedCategories) {
                    dlListingDTO.addTranslatedCategory(translatedCategory);
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

    private void setFeaturedAttachment(DlListingDTO dlListingDTO, DlListing dlListing) {
        DlAttachment featuredAttachment = dlListing.getFeaturedAttachment();
        if (featuredAttachment != null) {
            dlListingDTO.setFeaturedAttachment(attachmentMapper.attachmentToAttachmentDTO(dlListing.getFeaturedAttachment()));
        } else {
            Set<DlAttachment> dlAttachments = dlListing.getDlAttachments();
            if (!CollectionUtils.isEmpty(dlAttachments)) {
                dlListingDTO.setFeaturedAttachment(attachmentMapper.attachmentToAttachmentDTO(dlAttachments.iterator().next()));
            }
        }
    }

    class DlContentFieldValue {
        private String value = "";
        private String selectedValue = "";
        private List<QlStringTranslationModel> translatedValues;
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

        List<DlListingFieldItemDTO> getDlContentFieldItemDTOS() {
            return dlContentFieldItemDTOS;
        }

        void addDlContentFieldItemDTOS(DlListingFieldItemDTO dlListingFieldItemDTO) {
            dlContentFieldItemDTOS.add(dlListingFieldItemDTO);
        }

        List<QlStringTranslationModel> getTranslatedValues() {
            return translatedValues;
        }

        void setTranslatedValues(List<QlStringTranslationModel> translatedValues) {
            this.translatedValues = translatedValues;
        }

    }

    private List<QlStringTranslationModel> getTranslations(QlString qlString) {
        List<QlStringTranslationModel> qlStringModels = new ArrayList<>();

        Set<StringTranslation> stringTranslation = qlString.getStringTranslation();
        if (!CollectionUtils.isEmpty(stringTranslation)) {
            for (StringTranslation translation : stringTranslation) {
                QlStringTranslationModel qlStringTranslationModel = new QlStringTranslationModel();
                qlStringTranslationModel.setId(translation.getId());
                qlStringTranslationModel.setLanguageCode(translation.getLanguageCode());
                qlStringTranslationModel.setValue(translation.getValue());

                qlStringModels.add(qlStringTranslationModel);
            }
        }

        return qlStringModels;
    }
}
