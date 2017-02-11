package com.manev.quislisting.service.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.taxonomy.TermTaxonomyRepository;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.mapper.NavMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class NavMenuService {

    private final Logger log = LoggerFactory.getLogger(NavMenuService.class);

    private TermTaxonomyRepository<NavMenu> termTaxonomyRepository;

    private NavMenuMapper postCategoryMapper;

    public NavMenuService(TermTaxonomyRepository<NavMenu> termTaxonomyRepository, NavMenuMapper postCategoryMapper) {
        this.termTaxonomyRepository = termTaxonomyRepository;
        this.postCategoryMapper = postCategoryMapper;
    }

    public NavMenuDTO save(NavMenuDTO navMenuDTO) {
        log.debug("Request to save NavMenuDTO : {}", navMenuDTO);

        NavMenu navMenu = postCategoryMapper.navMenuDTOToNavMenu(navMenuDTO);
        navMenu = termTaxonomyRepository.save(navMenu);
        return postCategoryMapper.navMenuToNavMenuDTO(navMenu);
    }

    public Page<NavMenuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NavMenuDTO");
        Page<NavMenu> result = termTaxonomyRepository.findAll(pageable);
        return result.map(postCategoryMapper::navMenuToNavMenuDTO);
    }

    @Transactional(readOnly = true)
    public NavMenuDTO findOne(Long id) {
        log.debug("Request to get NavMenuDTO : {}", id);
        NavMenu result = termTaxonomyRepository.findOne(id);
        return result != null ? postCategoryMapper.navMenuToNavMenuDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete NavMenuDTO : {}", id);
        termTaxonomyRepository.delete(id);
    }

}
