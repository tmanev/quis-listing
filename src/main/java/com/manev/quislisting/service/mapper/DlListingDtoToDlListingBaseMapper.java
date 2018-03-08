package com.manev.quislisting.service.mapper;

import com.manev.quislisting.service.model.DlListingBaseModel;
import com.manev.quislisting.service.model.DlListingModel;
import com.manev.quislisting.service.model.DlLocationModel;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.dto.TranslatedTermDTO;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class DlListingDtoToDlListingBaseMapper {

    private final DlAttachmentModelMapper dlAttachmentModelMapper;

    public DlListingDtoToDlListingBaseMapper(final DlAttachmentModelMapper dlAttachmentModelMapper) {
        this.dlAttachmentModelMapper = dlAttachmentModelMapper;
    }

    public DlListingBaseModel convert(final DlListingDTO dlListing, final DlListingBaseModel baseModel,
            final String languageCode) {
        baseModel.setId(dlListing.getId());
        baseModel.setTitle(dlListing.getTitle());
        baseModel.setName(dlListing.getName());
        baseModel.setCreated(dlListing.getCreated());
        baseModel.setModified(dlListing.getModified());
        baseModel.setLanguageCode(dlListing.getLanguageCode());
        baseModel.setSourceLanguageCode(dlListing.getSourceLanguageCode());
        baseModel.setFeaturedAttachment(dlListing.getFeaturedAttachment() != null
                ? dlAttachmentModelMapper.convert(dlListing.getFeaturedAttachment()) : null);
        baseModel.setStatus(dlListing.getStatus());
        baseModel.setPrice(dlListing.getPrice());
        baseModel.setPriceCurrency(dlListing.getPriceCurrency());
        baseModel.setDlLocations(setDlLocations(dlListing, baseModel, languageCode));

        return baseModel;
    }

    private List<DlLocationModel> setDlLocations(final DlListingDTO dlListingDTO, final DlListingBaseModel baseModel,
            final String languageCode) {
        setDlLocation(dlListingDTO, baseModel, languageCode);

        return baseModel.getDlLocations();
    }

    public void setDlLocation(final DlListingDTO dlListingDTO, final DlListingBaseModel model,
            final String languageCode) {
        final List<DlLocationDTO> dlLocations = dlListingDTO.getDlLocations();
        final List<TranslatedTermDTO> translatedLocations = dlListingDTO.getTranslatedLocations();
        if (!CollectionUtils.isEmpty(dlLocations)) {
            for (final DlLocationDTO dlLocationDTO : dlLocations) {
                model.addDlLocation(createDlLocationModelParent(dlLocationDTO, translatedLocations, languageCode));
            }
        }
    }

    public DlLocationModel createDlLocationModelParent(final DlLocationDTO dlLocationDTO,
            final List<TranslatedTermDTO> translatedLocations, final String languageCode) {
        final TranslatedTermDTO translatedTerm = findTranslatedTerm(dlLocationDTO.getTranslationGroupId(),
                languageCode, translatedLocations);

        if (translatedTerm != null) {
            final DlLocationModel dlLocationModel = new DlLocationModel();
            if (dlLocationDTO.getParent() != null) {
                dlLocationModel.setParent(createDlLocationModelParent(dlLocationDTO.getParent(), translatedLocations,
                        languageCode));
            }

            dlLocationModel.setId(translatedTerm.getId());
            dlLocationModel.setLocation(translatedTerm.getName());

            return dlLocationModel;
        }
        return null;
    }

    public TranslatedTermDTO findTranslatedTerm(final Long translationGroupId, final String languageCode,
            final List<TranslatedTermDTO> translatedTermDTOS) {
        for (final TranslatedTermDTO translatedTermDTO : translatedTermDTOS) {
            if (translatedTermDTO.getTranslationGroupId().equals(translationGroupId)
                    && translatedTermDTO.getLangKey().equals(languageCode)) {
                return translatedTermDTO;
            }
        }

        return null;
    }

}
