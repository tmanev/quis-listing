package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.DlContentFieldGroup;
import com.manev.quislisting.service.dto.DlContentFieldGroupDTO;
import org.springframework.stereotype.Component;

@Component
public class DlContentFieldGroupMapper {

    public DlContentFieldGroup dlContentFieldGroupDTOToDlContentFieldGroup(DlContentFieldGroup dlContentFieldGroup, DlContentFieldGroupDTO dlContentFieldGroupDTO) {
        dlContentFieldGroup.setId(dlContentFieldGroupDTO.getId());
        dlContentFieldGroup.setName(dlContentFieldGroupDTO.getName());
        dlContentFieldGroup.setSlug(dlContentFieldGroupDTO.getSlug());
        dlContentFieldGroup.setDescription(dlContentFieldGroupDTO.getDescription());
        dlContentFieldGroup.setOrderNum(dlContentFieldGroupDTO.getOrderNum());

        return dlContentFieldGroup;
    }

    public DlContentFieldGroupDTO dlContentFieldGroupToDlContentFieldGroupDTO(DlContentFieldGroup dlContentFieldGroup) {
        DlContentFieldGroupDTO dlContentFieldGroupDTO = new DlContentFieldGroupDTO();

        dlContentFieldGroupDTO.setId(dlContentFieldGroup.getId());
        dlContentFieldGroupDTO.setName(dlContentFieldGroup.getName());
        dlContentFieldGroupDTO.setSlug(dlContentFieldGroup.getSlug());
        dlContentFieldGroupDTO.setDescription(dlContentFieldGroup.getDescription());
        dlContentFieldGroupDTO.setOrderNum(dlContentFieldGroup.getOrderNum());

        return dlContentFieldGroupDTO;
    }

    public DlContentFieldGroup dlContentFieldGroupDTOToDlContentFieldGroup(DlContentFieldGroupDTO dlContentFieldGroupDTO) {
        return dlContentFieldGroupDTOToDlContentFieldGroup(new DlContentFieldGroup(), dlContentFieldGroupDTO);
    }
}
