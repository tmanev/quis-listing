package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.service.mapper.DlContentFieldMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DlContentFieldService {

    private final Logger log = LoggerFactory.getLogger(DlContentFieldService.class);

    private DlContentFieldRepository dlContentFieldRepository;
    private DlContentFieldMapper dlContentFieldMapper;
    private DlContentFieldItemService dlContentFieldItemService;
    private DlCategoryRepository dlCategoryRepository;

    public DlContentFieldService(DlContentFieldRepository dlContentFieldRepository,
                                 DlContentFieldMapper dlContentFieldMapper, DlContentFieldItemService dlContentFieldItemService, DlCategoryRepository dlCategoryRepository) {
        this.dlContentFieldRepository = dlContentFieldRepository;
        this.dlContentFieldMapper = dlContentFieldMapper;
        this.dlContentFieldItemService = dlContentFieldItemService;
        this.dlCategoryRepository = dlCategoryRepository;
    }

    public DlContentFieldDTO save(DlContentFieldDTO dlContentFieldDTO) {
        log.debug("Request to save DlContentFieldDTO : {}", dlContentFieldDTO);

        DlContentField dlContentField;
        if (dlContentFieldDTO.getId() != null) {
            DlContentField one = dlContentFieldRepository.findOne(dlContentFieldDTO.getId());
            dlContentField = dlContentFieldMapper.dlContentFieldDTOToDlContentField(one, dlContentFieldDTO);
        } else {
            dlContentField = dlContentFieldMapper.dlContentFieldDTOToDlContentField(dlContentFieldDTO);
        }

        dlContentFieldRepository.save(dlContentField);

        saveQlString(dlContentField);

        return dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(dlContentField, null);
    }

    private void saveQlString(DlContentField dlContentField) {
        if (dlContentField.getQlString() == null) {
            dlContentField.setQlString(new QlString()
                    .languageCode("en")
                    .context("dl-content-field")
                    .name("dl-content-field-#" + dlContentField.getId())
                    .value(dlContentField.getName())
                    .status(0));
        } else {
            QlString qlString = dlContentField.getQlString();
            if (!qlString.getValue().equals(dlContentField.getName())) {
                qlString.setValue(dlContentField.getName());
                qlString.setStatus(0);
            }
        }

        if (dlContentField.getQlStringDescription() == null) {
            dlContentField.setQlStringDescription(new QlString()
                    .languageCode("en")
                    .context("dl-content-field-description")
                    .name("dl-content-field-description-#" + dlContentField.getId())
                    .value(dlContentField.getDescription())
                    .status(0));
        } else {
            QlString qlStringDescription = dlContentField.getQlStringDescription();
            if (!qlStringDescription.getValue().equals(dlContentField.getDescription())) {
                qlStringDescription.setValue(dlContentField.getDescription());
                qlStringDescription.setStatus(0);
            }
        }

        dlContentFieldRepository.save(dlContentField);
    }

    public Page<DlContentFieldDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DlContentFieldDTO");
        Page<DlContentField> result = dlContentFieldRepository.findAll(pageable);
        return result.map(dlContentField -> dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(dlContentField, null));
    }

    public List<DlContentFieldDTO> findAllByCategoryId(Long categoryId, String language) {
        long start = System.currentTimeMillis();
        DlCategory dlCategory = dlCategoryRepository.findOne(categoryId);
        long startQuery = System.currentTimeMillis();
        List<DlContentField> dlContentFields = dlContentFieldRepository.findAllByDlCategoriesOrDlCategoriesIsNullAndEnabledOrderByOrderNum(dlCategory, Boolean.TRUE);
        log.info("findAllByDlCategoriesOrDlCategoriesIsNullAndEnabledOrderByOrderNum for categoryId: {}, took: {} ms", categoryId, System.currentTimeMillis()-startQuery);
        List<DlContentFieldDTO> result = new ArrayList<>();
        long startMapping = System.currentTimeMillis();
        dlContentFields.forEach(dlContentField ->
                result.add(dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(dlContentField, language))
        );
        log.info("dlContentFieldMapper.dlContentFieldToDlContentFieldDTO, took: {} ms", System.currentTimeMillis() - startMapping);
        log.info("findAllByCategoryId categoryId: {}, language: {}, took: {} ms", categoryId, language, System.currentTimeMillis() - start);
        return result;
    }

    public DlContentFieldDTO findOne(Long id) {
        log.debug("Request to get DlContentFieldDTO: {}", id);
        DlContentField result = dlContentFieldRepository.findOne(id);
        return result != null ? dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(result, null) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlContentFieldDTO : {}", id);
        dlContentFieldRepository.delete(id);
    }
}
