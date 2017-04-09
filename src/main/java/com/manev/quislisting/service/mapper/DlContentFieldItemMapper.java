package com.manev.quislisting.service.mapper;


import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import org.springframework.stereotype.Component;

@Component
public class DlContentFieldItemMapper {

    public DlContentFieldItem dlContentFieldItemDTOToDlContentFieldItem(DlContentFieldItemDTO dlContentFieldItemDTO) {
        return new DlContentFieldItem()
                .id(dlContentFieldItemDTO.getId())
                .value(dlContentFieldItemDTO.getValue());
    }

    public DlContentFieldItemDTO dlContentFieldItemToDlContentFieldItemDTO(DlContentFieldItem dlContentFieldItem) {
        return new DlContentFieldItemDTO()
                .id(dlContentFieldItem.getId())
                .value(dlContentFieldItem.getValue());
    }

}
