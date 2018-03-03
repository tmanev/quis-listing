package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItemGroup;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.repository.DlContentFieldItemGroupRepository;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.service.dto.DlContentFieldItemGroupDTO;
import com.manev.quislisting.service.mapper.DlContentFieldItemGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DlContentFieldItemGroupService {

    private final Logger log = LoggerFactory.getLogger(DlContentFieldItemGroupService.class);

    private DlContentFieldItemGroupRepository contentFieldItemGroupRepository;
    private DlContentFieldItemGroupMapper dlContentFieldItemGroupMapper;
    private DlContentFieldRepository dlContentFieldRepository;

    public DlContentFieldItemGroupService(DlContentFieldItemGroupRepository dlContentFieldItemGroupRepository, DlContentFieldItemGroupMapper dlContentFieldItemGroupMapper, DlContentFieldRepository dlContentFieldRepository) {
        this.contentFieldItemGroupRepository = dlContentFieldItemGroupRepository;
        this.dlContentFieldItemGroupMapper = dlContentFieldItemGroupMapper;
        this.dlContentFieldRepository = dlContentFieldRepository;
    }

    public DlContentFieldItemGroupDTO save(DlContentFieldItemGroupDTO dlContentFieldGroupDTO, Long dlContentFieldId) {
        log.debug("Request to save DlContentFieldItemGroupDTO : {}", dlContentFieldGroupDTO);

        DlContentFieldItemGroup dlContentFieldItemGroup;
        if (dlContentFieldGroupDTO.getId() != null) {
            DlContentFieldItemGroup one = contentFieldItemGroupRepository.findOne(dlContentFieldGroupDTO.getId());
            dlContentFieldItemGroup = dlContentFieldItemGroupMapper.map(one, dlContentFieldGroupDTO);
        } else {
            dlContentFieldItemGroup = dlContentFieldItemGroupMapper.map(dlContentFieldGroupDTO);
            DlContentField dlContentField = dlContentFieldRepository.findOne(dlContentFieldId);
            dlContentFieldItemGroup.setDlContentField(dlContentField);
        }

        contentFieldItemGroupRepository.save(dlContentFieldItemGroup);
        saveQlString(dlContentFieldItemGroup);


        return dlContentFieldItemGroupMapper.mapDto(dlContentFieldItemGroup, null);
    }

    public Page<DlContentFieldItemGroupDTO> findAll(Pageable pageable, Long dlContentFieldId) {
        log.debug("Request to get all DlContentFieldItemGroupDTO");
        DlContentField dlContentField = dlContentFieldRepository.findOne(dlContentFieldId);
        Page<DlContentFieldItemGroup> result = contentFieldItemGroupRepository.findAllByDlContentField(pageable, dlContentField);
        return result.map(dlContentFieldGroup -> dlContentFieldItemGroupMapper.mapDto(dlContentFieldGroup, null));
    }

    public DlContentFieldItemGroupDTO findOne(Long id) {
        log.debug("Request to get DlContentFieldItemGroupDTO: {}", id);
        DlContentFieldItemGroup result = contentFieldItemGroupRepository.findOne(id);
        return result != null ? dlContentFieldItemGroupMapper.mapDto(result, null) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlContentFieldItemGroupDTO : {}", id);
        contentFieldItemGroupRepository.delete(id);
    }

    private void saveQlString(DlContentFieldItemGroup dlContentFieldItemGroup) {
        if (dlContentFieldItemGroup.getQlString() == null) {
            dlContentFieldItemGroup.setQlString(new QlString()
                    .languageCode("en")
                    .context("dl-content-field-item-group")
                    .name("dl-content-field-item-group#" + dlContentFieldItemGroup.getId())
                    .value(dlContentFieldItemGroup.getName())
                    .status(0));
        } else {
            QlString qlString = dlContentFieldItemGroup.getQlString();
            if (!qlString.getValue().equals(dlContentFieldItemGroup.getName())) {
                qlString.setValue(dlContentFieldItemGroup.getName());
                qlString.setStatus(0);
            }
        }
        contentFieldItemGroupRepository.save(dlContentFieldItemGroup);
    }
}
