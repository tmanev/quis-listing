package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.DlContentFieldItemGroup;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import com.manev.quislisting.service.dto.DlContentFieldItemGroupDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DlContentFieldItemGroupMapper {

    private DlContentFieldItemMapper dlContentFieldItemMapper;

    public DlContentFieldItemGroupMapper(DlContentFieldItemMapper dlContentFieldItemMapper) {
        this.dlContentFieldItemMapper = dlContentFieldItemMapper;
    }

    public DlContentFieldItemGroup map(DlContentFieldItemGroup dlContentFieldGroup, DlContentFieldItemGroupDTO dlContentFieldGroupDTO) {
        dlContentFieldGroup.setId(dlContentFieldGroupDTO.getId());
        dlContentFieldGroup.setName(dlContentFieldGroupDTO.getName());
        dlContentFieldGroup.setDescription(dlContentFieldGroupDTO.getDescription());
        dlContentFieldGroup.setOrderNum(dlContentFieldGroupDTO.getOrderNum());
        return dlContentFieldGroup;
    }

    public DlContentFieldItemGroupDTO mapDto(DlContentFieldItemGroup dlContentFieldItemGroup, String languageCode) {
        DlContentFieldItemGroupDTO dlContentFieldGroupDTO = new DlContentFieldItemGroupDTO();

        dlContentFieldGroupDTO.setId(dlContentFieldItemGroup.getId());
        dlContentFieldGroupDTO.setName(dlContentFieldItemGroup.getName());
        String translatedName = TranslateUtil.getTranslatedString(dlContentFieldItemGroup, languageCode);
        dlContentFieldGroupDTO.setTranslatedName(translatedName);
        dlContentFieldGroupDTO.setDescription(dlContentFieldItemGroup.getDescription());
        dlContentFieldGroupDTO.setOrderNum(dlContentFieldItemGroup.getOrderNum());
        Set<DlContentFieldItem> dlContentFieldItems = dlContentFieldItemGroup.getDlContentFieldItems();
        List<DlContentFieldItemDTO> items = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dlContentFieldItems)) {
            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                items.add(dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem, languageCode));
            }
        }
        dlContentFieldGroupDTO.setDlContentFieldItems(items);

        return dlContentFieldGroupDTO;
    }

    public DlContentFieldItemGroup map(DlContentFieldItemGroupDTO dlContentFieldGroupDTO) {
        return map(new DlContentFieldItemGroup(), dlContentFieldGroupDTO);
    }
}
