package com.manev.quislisting.service.mapper;

import com.manev.quislisting.service.model.DlListingBaseModel;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import org.springframework.stereotype.Component;

@Component
public class DlListingDtoToDlListingBaseMapper {

    public DlListingBaseModel convert(DlListingDTO dlListing, DlListingBaseModel baseModel) {
        baseModel.setId(dlListing.getId());
        baseModel.setTitle(dlListing.getTitle());
        baseModel.setName(dlListing.getName());
        baseModel.setCreated(dlListing.getCreated());
        baseModel.setModified(dlListing.getModified());
        baseModel.setLanguageCode(dlListing.getLanguageCode());
        baseModel.setSourceLanguageCode(dlListing.getSourceLanguageCode());
        baseModel.setFeaturedAttachment(dlListing.getFeaturedAttachment());
        baseModel.setStatus(dlListing.getStatus());

        return baseModel;
    }

}
