package com.manev.quislisting.service.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.mapper.NavMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class NavMenuService {

    private final Logger log = LoggerFactory.getLogger(NavMenuService.class);

    private NavMenuRepository navMenuRepository;

    private NavMenuMapper navMenuMapper;

    public NavMenuService(NavMenuRepository navMenuRepository, NavMenuMapper navMenuMapper) {
        this.navMenuRepository = navMenuRepository;
        this.navMenuMapper = navMenuMapper;
    }

    public NavMenuDTO save(NavMenuDTO navMenuDTO) {
        log.debug("Request to save NavMenuDTO : {}", navMenuDTO);

        NavMenu navMenu = navMenuMapper.navMenuDTOToNavMenu(navMenuDTO);
        if (navMenuDTO.getParentId() != null) {
            navMenu.setParent(navMenuRepository.findOne(navMenuDTO.getParentId()));
        }
        navMenu = navMenuRepository.save(navMenu);
        return navMenuMapper.navMenuToNavMenuDTO(navMenu);
    }

    public Page<NavMenuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NavMenuDTO");
        Page<NavMenu> result = navMenuRepository.findAll(pageable);
        List<NavMenuDTO> navMenuDTOS = navMenuMapper.navMenuToNavMenuDtoFlat(result);
        return new PageImpl<>(navMenuDTOS, pageable, result.getTotalElements());
    }

    @Transactional(readOnly = true)
    public NavMenuDTO findOne(Long id) {
        log.debug("Request to get NavMenuDTO : {}", id);
        NavMenu result = navMenuRepository.findOne(id);
        return result != null ? navMenuMapper.navMenuToNavMenuDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete NavMenuDTO : {}", id);
        navMenuRepository.delete(id);
    }

}
