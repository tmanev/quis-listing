package com.manev.quislisting.service.taxonomy;

import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.TranslationGroupRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.mapper.ActiveLanguageMapper;
import com.manev.quislisting.service.taxonomy.mapper.NavMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class NavMenuService {

    private final Logger log = LoggerFactory.getLogger(NavMenuService.class);

    private NavMenuRepository navMenuRepository;

    private TranslationGroupRepository translationGroupRepository;

    private NavMenuMapper navMenuMapper;
    private ActiveLanguageMapper activeLanguageMapper;

    private LanguageRepository languageRepository;

    public NavMenuService(NavMenuRepository navMenuRepository, NavMenuMapper navMenuMapper,
                          TranslationGroupRepository translationGroupRepository, ActiveLanguageMapper activeLanguageMapper, LanguageRepository languageRepository) {
        this.navMenuRepository = navMenuRepository;
        this.navMenuMapper = navMenuMapper;
        this.translationGroupRepository = translationGroupRepository;
        this.activeLanguageMapper = activeLanguageMapper;
        this.languageRepository = languageRepository;
    }

    public NavMenuDTO save(NavMenuDTO navMenuDTO) {
        log.debug("Request to save NavMenuDTO : {}", navMenuDTO);

        NavMenu navMenu;
        if (navMenuDTO.getId()!=null) {
            NavMenu existingNavMenu = navMenuRepository.findOne(navMenuDTO.getId());
            navMenu = navMenuMapper.navMenuDTOToNavMenu(existingNavMenu, navMenuDTO);
        } else {
            navMenu = navMenuMapper.navMenuDTOToNavMenu(navMenuDTO);
        }

        if (navMenuDTO.getTrGroupId() != null) {
            navMenu.getTranslation().setTranslationGroup(translationGroupRepository.findOne(navMenuDTO.getTrGroupId()));
        } else {
            navMenu.getTranslation().setTranslationGroup(new TranslationGroup());
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

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");
        List<ActiveLanguageDTO> result = new ArrayList<>();

        List<Language> allByActive = languageRepository.findAllByActive(true);
        for (Language language : allByActive) {
            Long count = navMenuRepository.countByTranslation_languageCode(language.getCode());
            result.add(activeLanguageMapper.toActiveLanguageDTO(language, count));
        }

        return result;
    }
}
