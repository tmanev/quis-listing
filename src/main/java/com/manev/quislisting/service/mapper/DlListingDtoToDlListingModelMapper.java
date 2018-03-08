package com.manev.quislisting.service.mapper;

import com.manev.quislisting.service.model.AttachmentModel;
import com.manev.quislisting.service.model.DlListingFieldItemGroupModel;
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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class DlListingDtoToDlListingModelMapper {

    private final DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper;
    private final DlAttachmentModelMapper dlAttachmentModelMapper;

    public DlListingDtoToDlListingModelMapper(final DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper,
            final DlAttachmentModelMapper dlAttachmentModelMapper) {
        this.dlListingDtoToDlListingBaseMapper = dlListingDtoToDlListingBaseMapper;
        this.dlAttachmentModelMapper = dlAttachmentModelMapper;
    }

    public DlListingModel convert(final DlListingDTO dlListingDTO, final String languageCode) {
        final DlListingModel model = (DlListingModel) dlListingDtoToDlListingBaseMapper.convert(dlListingDTO,
                new DlListingModel(), languageCode);

        model.setContent(dlListingDTO.getContent());
        model.setContactInfo(dlListingDTO.getContactInfo());

        dlListingDtoToDlListingBaseMapper.setDlLocation(dlListingDTO, model, languageCode);
        setDlCategories(dlListingDTO, model, languageCode);
        setDlListingContentFields(dlListingDTO, model, languageCode);

        setDlAttachments(dlListingDTO, model);

        return model;
    }

    private void setDlAttachments(final DlListingDTO dlListingDTO, final DlListingModel model) {
        final List<AttachmentModel> attachmentModels = new ArrayList<>();
        final List<AttachmentDTO> attachments = dlListingDTO.getAttachments();
        for (final AttachmentDTO attachment : attachments) {
            attachmentModels.add(dlAttachmentModelMapper.convert(attachment));
        }
        model.setAttachments(attachmentModels);
    }

    private void setDlListingContentFields(final DlListingDTO dlListingDTO, final DlListingModel model,
            final String languageCode) {
        final List<DlListingFieldDTO> dlListingFields = dlListingDTO.getDlListingFields();
        final List<DlListingFieldModel> dlListingFieldModels = new ArrayList<>();

        if (!CollectionUtils.isEmpty(dlListingFields)) {
            for (final DlListingFieldDTO dlListingField : dlListingFields) {
                final DlListingFieldModel dlListingFieldModel = new DlListingFieldModel();

                dlListingFieldModel.setId(dlListingField.getId());
                dlListingFieldModel.setType(dlListingField.getType());
                dlListingFieldModel.setDlContentFieldGroup(dlListingField.getDlContentFieldGroup());

                final List<QlStringTranslationModel> translatedNames = dlListingField.getTranslatedNames();

                final String translatedName = findTranslation(translatedNames, languageCode);
                dlListingFieldModel.setName(!StringUtils.isEmpty(translatedName) ? translatedName
                        : dlListingField.getName());

                final List<QlStringTranslationModel> translatedValues = dlListingField.getTranslatedValues();
                final String translatedValue = findTranslation(translatedValues, languageCode);
                dlListingFieldModel.setValue(!StringUtils.isEmpty(translatedValue) ? translatedValue
                        : dlListingField.getValue());

                setItems(languageCode, dlListingField, dlListingFieldModel);
                setDlContentFieldItemGroups(dlListingField, dlListingFieldModel);

                dlListingFieldModels.add(dlListingFieldModel);
            }
        }

        model.setDlListingFields(dlListingFieldModels);
    }

    private void setItems(final String languageCode, final DlListingFieldDTO dlListingField,
            final DlListingFieldModel dlListingFieldModel) {
        final List<DlListingFieldItemDTO> dlListingFieldItemDTOs = dlListingField.getDlListingFieldItemDTOs();
        final List<DlListingFieldItemModel> items = new ArrayList<>();
        for (final DlListingFieldItemDTO dlListingFieldItemDTO : dlListingFieldItemDTOs) {
            final DlListingFieldItemModel dlListingFieldItemModel = new DlListingFieldItemModel();

            dlListingFieldItemModel.setId(dlListingFieldItemDTO.getId());
            final List<QlStringTranslationModel> translatedValues = dlListingFieldItemDTO.getTranslatedValues();
            final String translatedValue = findTranslation(translatedValues, languageCode);
            dlListingFieldItemModel.setValue(!StringUtils.isEmpty(translatedValue) ? translatedValue
                    : dlListingFieldItemDTO.getValue());

            items.add(dlListingFieldItemModel);
        }
        dlListingFieldModel.setItems(items);
    }

    private void setDlContentFieldItemGroups(final DlListingFieldDTO dlListingField,
            final DlListingFieldModel dlListingFieldModel) {
        final List<DlListingFieldItemGroupModel> dlListingFieldItemGroups = dlListingField.getDlListingFieldItemGroups();
        dlListingFieldModel.setDlListingFieldItemGroups(dlListingFieldItemGroups);
    }

    private String findTranslation(final List<QlStringTranslationModel> translationModels, final String languageCode) {
        if (!CollectionUtils.isEmpty(translationModels)) {
            for (final QlStringTranslationModel qlStringModel : translationModels) {
                if (qlStringModel.getLanguageCode().equals(languageCode)) {
                    return qlStringModel.getValue();
                }
            }
        }

        return null;
    }

    private void setDlCategories(final DlListingDTO dlListingDTO, final DlListingModel model,
            final String languageCode) {
        final List<DlCategoryDTO> dlCategories = dlListingDTO.getDlCategories();
        final List<TranslatedTermDTO> translatedCategories = dlListingDTO.getTranslatedCategories();
        for (final DlCategoryDTO dlCategory : dlCategories) {
            final TranslatedTermDTO translatedTerm = dlListingDtoToDlListingBaseMapper
                    .findTranslatedTerm(dlCategory.getTranslationGroupId(), languageCode, translatedCategories);
            if (translatedTerm != null) {
                model.addDlCategory(translatedTerm.getName());
            }
        }
    }
}
