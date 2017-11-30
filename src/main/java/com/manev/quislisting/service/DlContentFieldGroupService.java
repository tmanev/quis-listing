package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlContentFieldGroup;
import com.manev.quislisting.repository.DlContentFieldGroupRepository;
import com.manev.quislisting.service.dto.DlContentFieldGroupDTO;
import com.manev.quislisting.service.mapper.DlContentFieldGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DlContentFieldGroupService {

    private final Logger log = LoggerFactory.getLogger(DlContentFieldGroupService.class);

    private DlContentFieldGroupRepository dlContentFieldGroupRepository;
    private DlContentFieldGroupMapper dlContentFieldGroupMapper;

    public DlContentFieldGroupService(DlContentFieldGroupRepository dlContentFieldGroupRepository, DlContentFieldGroupMapper dlContentFieldGroupMapper) {
        this.dlContentFieldGroupRepository = dlContentFieldGroupRepository;
        this.dlContentFieldGroupMapper = dlContentFieldGroupMapper;
    }


    public DlContentFieldGroupDTO save(DlContentFieldGroupDTO dlContentFieldGroupDTO) {
        log.debug("Request to save DlContentFieldGroupDTO : {}", dlContentFieldGroupDTO);

        DlContentFieldGroup dlContentFieldGroup;
        if (dlContentFieldGroupDTO.getId() != null) {
            DlContentFieldGroup one = dlContentFieldGroupRepository.findOne(dlContentFieldGroupDTO.getId());
            dlContentFieldGroup = dlContentFieldGroupMapper.dlContentFieldGroupDTOToDlContentFieldGroup(one, dlContentFieldGroupDTO);
        } else {
            dlContentFieldGroup = dlContentFieldGroupMapper.dlContentFieldGroupDTOToDlContentFieldGroup(dlContentFieldGroupDTO);
        }

        dlContentFieldGroupRepository.save(dlContentFieldGroup);

        return dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentFieldGroup);
    }

    public Page<DlContentFieldGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DlContentFieldGroupDTO");
        Page<DlContentFieldGroup> result = dlContentFieldGroupRepository.findAll(pageable);
        return result.map(dlContentFieldGroup -> dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentFieldGroup));
    }

    public DlContentFieldGroupDTO findOne(Long id) {
        log.debug("Request to get DlContentFieldGroupDTO: {}", id);
        DlContentFieldGroup result = dlContentFieldGroupRepository.findOne(id);
        return result != null ? dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlContentFieldGroupDTO : {}", id);
        dlContentFieldGroupRepository.delete(id);
    }

}
