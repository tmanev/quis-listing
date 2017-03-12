package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.service.mapper.DlContentFieldMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DlContentFieldService {

    private final Logger log = LoggerFactory.getLogger(DlContentFieldService.class);

    private DlContentFieldRepository dlContentFieldRepository;
    private DlContentFieldMapper dlContentFieldMapper;

    public DlContentFieldService(DlContentFieldRepository dlContentFieldRepository,
                                 DlContentFieldMapper dlContentFieldMapper) {
        this.dlContentFieldRepository = dlContentFieldRepository;
        this.dlContentFieldMapper = dlContentFieldMapper;
    }

    public DlContentFieldDTO save(DlContentFieldDTO dlContentFieldDTO) {
        log.debug("Request to save DlContentFieldDTO : {}", dlContentFieldDTO);

        DlContentField dlContentField = dlContentFieldMapper.dlContentFieldDTOToDlContentField(dlContentFieldDTO);
        dlContentField = dlContentFieldRepository.save(dlContentField);
        dlContentField = saveQlString(dlContentField);
        return dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(dlContentField);
    }

    private DlContentField saveQlString(DlContentField dlContentField) {
        if (dlContentField.getQlString() == null) {
            dlContentField.setQlString(new QlString().languageCode("en").context("dl-content-field").name("dl-content-field-#" + dlContentField.getId()).value(dlContentField.getName()).status(0));
        } else {
            QlString qlString = dlContentField.getQlString();
            if (!qlString.getValue().equals(dlContentField.getName())) {
                qlString.setValue(dlContentField.getName());
                qlString.setStatus(0);
            }
        }
        dlContentField = dlContentFieldRepository.save(dlContentField);
        return dlContentField;
    }

    public Page<DlContentFieldDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DlContentFieldDTO");
        Page<DlContentField> result = dlContentFieldRepository.findAll(pageable);
        return result.map(dlContentFieldMapper::dlContentFieldToDlContentFieldDTO);
    }

    public DlContentFieldDTO findOne(Long id) {
        log.debug("Request to get DlContentFieldDTO: {}", id);
        DlContentField result = dlContentFieldRepository.findOne(id);
        return result != null ? dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlContentFieldDTO : {}", id);
        dlContentFieldRepository.delete(id);
    }
}
