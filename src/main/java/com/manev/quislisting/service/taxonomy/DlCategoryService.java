package com.manev.quislisting.service.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.TermTaxonomyRepository;
import com.manev.quislisting.service.dto.taxonomy.DlCategoryDTO;
import com.manev.quislisting.service.mapper.DlCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DlCategoryService {

    private final Logger log = LoggerFactory.getLogger(DlCategoryService.class);

    private TermTaxonomyRepository<DlCategory> termTaxonomyRepository;

    private DlCategoryMapper postCategoryMapper;

    public DlCategoryService(TermTaxonomyRepository<DlCategory> termTaxonomyRepository, DlCategoryMapper postCategoryMapper) {
        this.termTaxonomyRepository = termTaxonomyRepository;
        this.postCategoryMapper = postCategoryMapper;
    }

    public DlCategoryDTO save(DlCategoryDTO dlCategoryDTO) {
        log.debug("Request to save DlCategoryDTO : {}", dlCategoryDTO);

        DlCategory dlCategory = postCategoryMapper.dlCategoryDTOTodlCategory(dlCategoryDTO);
        dlCategory = termTaxonomyRepository.save(dlCategory);
        return postCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);
    }

    public Page<DlCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DlCategoryDTO");
        Page<DlCategory> result = termTaxonomyRepository.findAll(pageable);
        return result.map(postCategoryMapper::dlCategoryToDlCategoryDTO);
    }

    @Transactional(readOnly = true)
    public DlCategoryDTO findOne(Long id) {
        log.debug("Request to get DlCategoryDTO : {}", id);
        DlCategory result = termTaxonomyRepository.findOne(id);
        return result != null ? postCategoryMapper.dlCategoryToDlCategoryDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlCategoryDTO : {}", id);
        termTaxonomyRepository.delete(id);
    }

}
