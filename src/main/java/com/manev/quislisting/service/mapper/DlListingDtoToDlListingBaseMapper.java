package com.manev.quislisting.service.mapper;

import com.manev.quislisting.service.model.DlListingBaseModel;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import org.springframework.stereotype.Component;

@Component
public class DlListingDtoToDlListingBaseMapper {

    private DlAttachmentModelMapper dlAttachmentModelMapper;

    public DlListingDtoToDlListingBaseMapper(DlAttachmentModelMapper dlAttachmentModelMapper) {
        this.dlAttachmentModelMapper = dlAttachmentModelMapper;
    }

    public DlListingBaseModel convert(DlListingDTO dlListing, DlListingBaseModel baseModel) {
        baseModel.setId(dlListing.getId());
        baseModel.setTitle(dlListing.getTitle());
        baseModel.setName(dlListing.getName());
        baseModel.setCreated(dlListing.getCreated());
        baseModel.setModified(dlListing.getModified());
        baseModel.setLanguageCode(dlListing.getLanguageCode());
        baseModel.setSourceLanguageCode(dlListing.getSourceLanguageCode());
        baseModel.setFeaturedAttachment(dlListing.getFeaturedAttachment() != null ? dlAttachmentModelMapper.convert(dlListing.getFeaturedAttachment()) : null);
        baseModel.setStatus(dlListing.getStatus());
        baseModel.setPrice(dlListing.getPrice());
        baseModel.setPriceCurrency(dlListing.getPriceCurrency());

        return baseModel;
    }

}
