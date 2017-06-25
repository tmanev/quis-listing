package com.manev.quislisting.service;

import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.exception.MissingConfigurationException;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.service.dto.QlConfigDTO;
import com.manev.quislisting.service.mapper.QlConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Стефан on 04.04.2017.
 */
@Service
@Transactional
public class QlConfigService {

    private final Logger log = LoggerFactory.getLogger(QlConfigService.class);

    private QlConfigRepository qlConfigRepository;
    private QlConfigMapper qlConfigMapper;


    public QlConfigService(QlConfigRepository qlConfigRepository, QlConfigMapper qlConfigMapper) {
        this.qlConfigRepository = qlConfigRepository;
        this.qlConfigMapper = qlConfigMapper;
    }

    public QlConfigDTO save(QlConfigDTO qlConfigDTO) {
        log.debug("Request to save QlConfigDTO : {}", qlConfigDTO);

        QlConfig qlConfig = qlConfigMapper.qlConfigDTOtoQlConfig(qlConfigDTO);
        qlConfig = qlConfigRepository.save(qlConfig);

        return qlConfigMapper.qlConfigToQlConfigDTO(qlConfig);
    }

    public Page<QlConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QlConfigDTO");
        Page<QlConfig> result = qlConfigRepository.findAll(pageable);
        return result.map(qlConfigMapper::qlConfigToQlConfigDTO);

    }

    public QlConfigDTO findOne(Long id) {

        log.debug("Request to get QlConfig : {}", id);
        QlConfig result = qlConfigRepository.findOne(id);
        return result != null ? qlConfigMapper.qlConfigToQlConfigDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete QlConfigDTO : {}", id);
        qlConfigRepository.delete(id);
    }

    public QlConfig findOneByKey(String key) {
        QlConfig qlConfig = qlConfigRepository.findOneByKey(key);
        if (qlConfig == null) {
            throw new MissingConfigurationException(String.format("Missing configuration for key: %s", key));
        }
        return qlConfig;
    }
}
