package com.manev.quislisting.service.mapper;

import com.manev.quislisting.service.model.AttachmentModel;
import com.manev.quislisting.service.model.DlListingFieldItemModel;
import com.manev.quislisting.service.model.DlListingFieldModel;
import com.manev.quislisting.service.model.DlListingModel;
import com.manev.quislisting.service.model.DlLocationModel;
import com.manev.quislisting.service.model.QlStringTranslationModel;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingFieldDTO;
import com.manev.quislisting.service.post.dto.DlListingFieldItemDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.TranslatedTermDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class DlListingDtoToDlListingModelMapper {

    private DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper;
    private DlAttachmentModelMapper dlAttachmentModelMapper;

    public DlListingDtoToDlListingModelMapper(DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper, DlAttachmentModelMapper dlAttachmentModelMapper) {
        this.dlListingDtoToDlListingBaseMapper = dlListingDtoToDlListingBaseMapper;
        this.dlAttachmentModelMapper = dlAttachmentModelMapper;
    }

    public DlListingModel convert(DlListingDTO dlListingDTO, String languageCode) {
        DlListingModel model = (DlListingModel) dlListingDtoToDlListingBaseMapper.convert(dlListingDTO, new DlListingModel());

        model.setContent(dlListingDTO.getContent());

        setDlLocation(dlListingDTO, model, languageCode);
        setDlCategories(dlListingDTO, model, languageCode);
        setDlListingContentFields(dlListingDTO, model, languageCode);

        setDlAttachments(dlListingDTO, model);

        return model;
    }

    private void setDlAttachments(DlListingDTO dlListingDTO, DlListingModel model) {
        List<AttachmentModel> attachmentModels = new ArrayList<>();
        List<AttachmentDTO> attachments = dlListingDTO.getAttachments();
        for (AttachmentDTO attachment : attachments) {
            attachmentModels.add(dlAttachmentModelMapper.convert(attachment));
        }
        model.setAttachments(attachmentModels);
    }

    private void setDlListingContentFields(DlListingDTO dlListingDTO, DlListingModel model, String languageCode) {
        List<DlListingFieldDTO> dlListingFields = dlListingDTO.getDlListingFields();
        List<DlListingFieldModel> dlListingFieldModels = new ArrayList<>();

        if (!CollectionUtils.isEmpty(dlListingFields)) {
            for (DlListingFieldDTO dlListingField : dlListingFields) {
                DlListingFieldModel dlListingFieldModel = new DlListingFieldModel();

                dlListingFieldModel.setId(dlListingField.getId());
                dlListingFieldModel.setType(dlListingField.getType());
                dlListingFieldModel.setDlContentFieldGroup(dlListingField.getDlContentFieldGroup());

                List<QlStringTranslationModel> translatedNames = dlListingField.getTranslatedNames();

                String translatedName = findTranslation(translatedNames, languageCode);
                dlListingFieldModel.setName(translatedName != null ? translatedName : dlListingField.getName());

                List<QlStringTranslationModel> translatedValues = dlListingField.getTranslatedValues();
                String translatedValue = findTranslation(translatedValues, languageCode);
                dlListingFieldModel.setValue(translatedValue != null ? translatedValue : dlListingField.getValue());

                setItems(languageCode, dlListingField, dlListingFieldModel);

                dlListingFieldModels.add(dlListingFieldModel);
            }
        }

        model.setDlListingFields(dlListingFieldModels);
    }

    private void setItems(String languageCode, DlListingFieldDTO dlListingField, DlListingFieldModel dlListingFieldModel) {
        List<DlListingFieldItemDTO> dlListingFieldItemDTOs = dlListingField.getDlListingFieldItemDTOs();
        List<DlListingFieldItemModel> items = new ArrayList<>();
        for (DlListingFieldItemDTO dlListingFieldItemDTO : dlListingFieldItemDTOs) {
            DlListingFieldItemModel dlListingFieldItemModel = new DlListingFieldItemModel();

            dlListingFieldItemModel.setId(dlListingFieldItemDTO.getId());
            List<QlStringTranslationModel> translatedValues = dlListingFieldItemDTO.getTranslatedValues();
            String translatedValue = findTranslation(translatedValues, languageCode);
            dlListingFieldItemModel.setValue(translatedValue != null ? translatedValue : dlListingFieldItemDTO.getValue());

            items.add(dlListingFieldItemModel);
        }
        dlListingFieldModel.setItems(items);
    }

    private String findTranslation(List<QlStringTranslationModel> translationModels, String languageCode) {
        if (!CollectionUtils.isEmpty(translationModels)) {
            for (QlStringTranslationModel qlStringModel : translationModels) {
                if (qlStringModel.getLanguageCode().equals(languageCode)) {
                    return qlStringModel.getValue();
                }
            }
        }

        return null;
    }

    private void setDlLocation(DlListingDTO dlListingDTO, DlListingModel model, String languageCode) {
        List<DlLocationDTO> dlLocations = dlListingDTO.getDlLocations();
        List<TranslatedTermDTO> translatedLocations = dlListingDTO.getTranslatedLocations();
        if (!CollectionUtils.isEmpty(dlLocations)) {
            for (DlLocationDTO dlLocationDTO : dlLocations) {
                model.addDlLocation(createDlLocationModelParent(dlLocationDTO, translatedLocations, languageCode));
            }
        }
    }

    private DlLocationModel createDlLocationModelParent(DlLocationDTO dlLocationDTO, List<TranslatedTermDTO> translatedLocations, String languageCode) {
        TranslatedTermDTO translatedTerm = findTranslatedTerm(dlLocationDTO.getTranslationGroupId(), languageCode, translatedLocations);

        if (translatedTerm != null) {
            DlLocationModel dlLocationModel = new DlLocationModel();
            if (dlLocationDTO.getParent() != null) {
                dlLocationModel.setParent(createDlLocationModelParent(dlLocationDTO.getParent(), translatedLocations, languageCode));
            }

            dlLocationModel.setId(translatedTerm.getId());
            dlLocationModel.setLocation(translatedTerm.getName());

            return dlLocationModel;
        }
        return null;
    }

    private void setDlCategories(DlListingDTO dlListingDTO, DlListingModel model, String languageCode) {
        List<DlCategoryDTO> dlCategories = dlListingDTO.getDlCategories();
        List<TranslatedTermDTO> translatedCategories = dlListingDTO.getTranslatedCategories();
        for (DlCategoryDTO dlCategory : dlCategories) {
            TranslatedTermDTO translatedTerm = findTranslatedTerm(dlCategory.getTranslationGroupId(), languageCode, translatedCategories);
            if (translatedTerm != null) {
                model.addDlCategory(translatedTerm.getName());
            }
        }
    }

    private TranslatedTermDTO findTranslatedTerm(Long translationGroupId, String languageCode, List<TranslatedTermDTO> translatedTermDTOS) {
        for (TranslatedTermDTO translatedTermDTO : translatedTermDTOS) {
            if (translatedTermDTO.getTranslationGroupId().equals(translationGroupId)
                    && translatedTermDTO.getLangKey().equals(languageCode)) {
                return translatedTermDTO;
            }
        }

        return null;
    }
}
