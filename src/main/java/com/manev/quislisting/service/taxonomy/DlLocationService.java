package com.manev.quislisting.service.taxonomy;

import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.TranslationGroupRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.mapper.ActiveLanguageMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class DlLocationService {

    private final Logger log = LoggerFactory.getLogger(DlLocationService.class);

    private DlLocationRepository dlLocationRepository;
    private LanguageRepository languageRepository;

    private TranslationGroupRepository translationGroupRepository;

    private DlLocationMapper dlLocationMapper;

    private ActiveLanguageMapper activeLanguageMapper;

    public DlLocationService(DlLocationRepository dlLocationRepository, DlLocationMapper dlLocationMapper,
                             TranslationGroupRepository translationGroupRepository,
                             LanguageRepository languageRepository,
                             ActiveLanguageMapper activeLanguageMapper) {
        this.dlLocationRepository = dlLocationRepository;
        this.dlLocationMapper = dlLocationMapper;
        this.translationGroupRepository = translationGroupRepository;
        this.languageRepository = languageRepository;
        this.activeLanguageMapper = activeLanguageMapper;
    }

    public DlLocationDTO save(DlLocationDTO dlLocationDTO) {
        log.debug("Request to save DlLocationDTO : {}", dlLocationDTO);

        DlLocation dlLocation;
        if (dlLocationDTO.getId()!=null) {
            DlLocation existingDlLocation = dlLocationRepository.findOne(dlLocationDTO.getId());
            dlLocation = dlLocationMapper.dlLocationDTOTodlLocation(existingDlLocation, dlLocationDTO);
        } else {
            dlLocation = dlLocationMapper.dlLocationDTOTodlLocation(dlLocationDTO);
        }

        if (dlLocationDTO.getParentId() != null) {
            dlLocation.setParent(dlLocationRepository.findOne(dlLocationDTO.getParentId()));
        }
        dlLocation = dlLocationRepository.save(dlLocation);
        return dlLocationMapper.dlLocationToDlLocationDTO(dlLocation);
    }

    public Page<DlLocationDTO> findAll(Pageable pageable, Map<String, String> allRequestParams) {
        log.debug("Request to get all DlLocationDTO");
        String languageCode = allRequestParams.get("languageCode");
        Page<DlLocation> result = dlLocationRepository.findAllByTranslation_languageCode(pageable, languageCode);
        List<DlLocationDTO> dlLocationDTOS = dlLocationMapper.dlLocationToDlLocationDtoFlat(result.getContent());
        return new PageImpl<>(dlLocationDTOS, pageable, result.getTotalElements());
    }

    @Transactional(readOnly = true)
    public DlLocationDTO findOne(Long id) {
        log.debug("Request to get DlLocationDTO : {}", id);
        DlLocation result = dlLocationRepository.findOne(id);
        return result != null ? dlLocationMapper.dlLocationToDlLocationDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlLocationDTO : {}", id);
        dlLocationRepository.delete(id);
    }

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");
        List<ActiveLanguageDTO> result = new ArrayList<>();

        List<Language> allByActive = languageRepository.findAllByActive(true);
        for (Language language : allByActive) {
            Long count = dlLocationRepository.countByTranslation_languageCode(language.getCode());
            result.add(activeLanguageMapper.toActiveLanguageDTO(language, count));
        }

        return result;
    }

    public List<DlLocationDTO> findAllByParentId(String parentId, String language) {
        DlLocation parent = null;
        if (parentId !=null) {
            parent = dlLocationRepository.findOne(Long.valueOf(parentId));
        }
        List<DlLocation> dlLocations = dlLocationRepository.findAllByParentAndTranslation_languageCode(parent, language);
        return dlLocations.stream().map(dlLocationMapper::dlLocationToDlLocationDTO).collect(Collectors.toList());
    }
}
