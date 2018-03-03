package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.DlContentFieldItemGroup;
import com.manev.quislisting.service.model.DlListingFieldItemGroupModel;
import org.springframework.stereotype.Component;

@Component
public class DlListingFieldItemGroupModelMapper {

    public DlListingFieldItemGroupModel map(DlContentFieldItemGroup dlContentFieldItemGroup) {
        DlListingFieldItemGroupModel dlListingFieldItemGroupModel = new DlListingFieldItemGroupModel();

        dlListingFieldItemGroupModel.setId(dlContentFieldItemGroup.getId());
        dlListingFieldItemGroupModel.setValue(dlContentFieldItemGroup.getName());

        return dlListingFieldItemGroupModel;
    }

}
