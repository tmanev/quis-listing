package com.manev.quislisting.service.mapper;


import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import org.springframework.stereotype.Component;

@Component
public class DlContentFieldItemMapper {

    public DlContentFieldItem dlContentFieldItemDTOToDlContentFieldItem(DlContentFieldItemDTO dlContentFieldItemDTO) {
        return new DlContentFieldItem()
                .id(dlContentFieldItemDTO.getId())
                .value(dlContentFieldItemDTO.getValue())
                .orderNum(dlContentFieldItemDTO.getOrderNum());
    }

    public DlContentFieldItemDTO dlContentFieldItemToDlContentFieldItemDTO(DlContentFieldItem dlContentFieldItem, String languageCode) {
        String translatedValue = TranslateUtil.getTranslatedString(dlContentFieldItem, languageCode);
        return new DlContentFieldItemDTO()
                .id(dlContentFieldItem.getId())
                .value(dlContentFieldItem.getValue())
                .translatedValue(translatedValue)
                .parent(dlContentFieldItem.getParent() != null ? dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem.getParent(), languageCode) : null)
                .orderNum(dlContentFieldItem.getOrderNum())
                .dlContentFieldItemGroupId(dlContentFieldItem.getDlContentFieldItemGroup() != null ? dlContentFieldItem.getDlContentFieldItemGroup().getId() : null);
    }

    public DlContentFieldItem dlContentFieldItemDTOToDlContentFieldItem(DlContentFieldItem dlContentFieldItem, DlContentFieldItemDTO dlContentFieldItemDTO) {
        dlContentFieldItem.setValue(dlContentFieldItemDTO.getValue());
        dlContentFieldItem.setOrderNum(dlContentFieldItemDTO.getOrderNum());
        return dlContentFieldItem;
    }

}
