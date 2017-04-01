package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.mapper.ActiveLanguageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class DlListingService {

    private final Logger log = LoggerFactory.getLogger(DlListingService.class);

    private DlListingRepository dlListingRepository;
    private LanguageRepository languageRepository;

    private DlListingMapper dlListingMapper;
    private ActiveLanguageMapper activeLanguageMapper;

    public DlListingService(DlListingRepository dlListingRepository, LanguageRepository languageRepository, DlListingMapper dlListingMapper, ActiveLanguageMapper activeLanguageMapper) {
        this.dlListingRepository = dlListingRepository;
        this.languageRepository = languageRepository;
        this.dlListingMapper = dlListingMapper;
        this.activeLanguageMapper = activeLanguageMapper;
    }

    public DlListingDTO save(DlListingDTO dlListingDTO) {
        log.debug("Request to save DlListingDTO : {}", dlListingDTO);

        DlListing dlListing = dlListingMapper.dlListingDTOToDlListing(dlListingDTO);
        dlListing = dlListingRepository.save(dlListing);
        return dlListingMapper.dlListingToDlListingDTO(dlListing);
    }

    public Page<DlListingDTO> findAll(Pageable pageable, Map<String, String> allRequestParams) {
        log.debug("Request to get all DlListingDTO");
        String languageCode = allRequestParams.get("languageCode");
        Page<DlListing> result = dlListingRepository.findAllByTranslation_languageCode(pageable, languageCode);
        return result.map(dlListingMapper::dlListingToDlListingDTO);
    }

    @Transactional(readOnly = true)
    public DlListingDTO findOne(Long id) {
        log.debug("Request to get DlListingDTO: {}", id);
        DlListing result = dlListingRepository.findOne(id);
        return result != null ? dlListingMapper.dlListingToDlListingDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlCategoryDTO : {}", id);
        dlListingRepository.delete(id);
    }

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");

        List<ActiveLanguageDTO> result = new ArrayList<>();

        List<Language> allByActive = languageRepository.findAllByActive(true);
        for (Language language : allByActive) {
            Long count = dlListingRepository.countByTranslation_languageCode(language.getCode());
            result.add(activeLanguageMapper.toActiveLanguageDTO(language, count));
        }

        return result;
    }
}
