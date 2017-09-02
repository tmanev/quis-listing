package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.service.dto.QlConfigDTO;
import org.springframework.stereotype.Component;

/**
 * Created by Стефан on 05.04.2017.
 */

@Component
public class QlConfigMapper {

    public QlConfigDTO qlConfigToQlConfigDTO(QlConfig qlConfig) {

        QlConfigDTO qlConfigDTO = new QlConfigDTO();
        qlConfigDTO.setId(qlConfig.getId());
        qlConfigDTO.setQlKey(qlConfig.getQlKey());
        qlConfigDTO.setValue(qlConfig.getValue());

        return qlConfigDTO;
    }

    public QlConfig qlConfigDTOtoQlConfig(QlConfigDTO qlConfigDTO){

        QlConfig qlConfig = new QlConfig();
        qlConfig.setId(qlConfigDTO.getId());
        qlConfig.setQlKey(qlConfigDTO.getQlKey());
        qlConfig.setValue(qlConfigDTO.getValue());

        return qlConfig;
    }

}
