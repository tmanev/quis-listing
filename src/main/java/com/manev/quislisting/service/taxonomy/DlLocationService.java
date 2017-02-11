package com.manev.quislisting.service.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.taxonomy.TermTaxonomyRepository;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DlLocationService {

    private final Logger log = LoggerFactory.getLogger(DlLocationService.class);

    private TermTaxonomyRepository<DlLocation> termTaxonomyRepository;

    private DlLocationMapper dlLocationMapper;

    public DlLocationService(TermTaxonomyRepository<DlLocation> termTaxonomyRepository, DlLocationMapper dlLocationMapper) {
        this.termTaxonomyRepository = termTaxonomyRepository;
        this.dlLocationMapper = dlLocationMapper;
    }

    public DlLocationDTO save(DlLocationDTO dlLocationDTO) {
        log.debug("Request to save DlLocationDTO : {}", dlLocationDTO);

        DlLocation dlLocation = dlLocationMapper.dlLocationDTOTodlLocation(dlLocationDTO);
        dlLocation = termTaxonomyRepository.save(dlLocation);
        return dlLocationMapper.dlLocationToDlLocationDTO(dlLocation);
    }

    public Page<DlLocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DlLocationDTO");
        Page<DlLocation> result = termTaxonomyRepository.findAll(pageable);
        return result.map(dlLocationMapper::dlLocationToDlLocationDTO);
    }

    @Transactional(readOnly = true)
    public DlLocationDTO findOne(Long id) {
        log.debug("Request to get DlLocationDTO : {}", id);
        DlLocation result = termTaxonomyRepository.findOne(id);
        return result != null ? dlLocationMapper.dlLocationToDlLocationDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlLocationDTO : {}", id);
        termTaxonomyRepository.delete(id);
    }

}
