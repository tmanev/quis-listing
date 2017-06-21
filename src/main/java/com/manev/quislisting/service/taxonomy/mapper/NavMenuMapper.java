package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.post.discriminator.NavMenuItem;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.post.NavMenuItemRepository;
import com.manev.quislisting.service.post.dto.TranslationDTO;
import com.manev.quislisting.service.post.mapper.TranslationMapper;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.NavMenuItemDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.NavMenuDTOBuilder;
import com.manev.quislisting.service.taxonomy.dto.builder.TermDTOBuilder;
import com.manev.quislisting.service.util.SlugUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NavMenuMapper {

    private NavMenuItemMapper navMenuItemMapper;
    private NavMenuItemRepository navMenuItemRepository;
    private TranslationMapper translationMapper;

    public NavMenuMapper(NavMenuItemMapper navMenuItemMapper, NavMenuItemRepository navMenuItemRepository, TranslationMapper translationMapper) {
        this.navMenuItemMapper = navMenuItemMapper;
        this.navMenuItemRepository = navMenuItemRepository;
        this.translationMapper = translationMapper;
    }

    public List<NavMenuDTO> navMenuToNavMenuDtoFlat(Page<NavMenu> page, List<Language> activeLanguages) {
        List<NavMenuDTO> result = new ArrayList<>();
        for (NavMenu navMenu : page) {
            result.add(navMenuToNavMenuDTO(navMenu, activeLanguages));
        }

        return result;
    }

    public NavMenuDTO navMenuToNavMenuDTO(NavMenu navMenu, List<Language> activeLanguages) {
        NavMenuDTO navMenuDTO = NavMenuDTOBuilder.aNavMenuDTO()
                .withId(navMenu.getId())
                .withTerm(TermDTOBuilder.aTerm()
                        .withId(navMenu.getTerm().getId())
                        .withName(navMenu.getTerm().getName())
                        .withSlug(navMenu.getTerm().getSlug())
                        .build())
                .withDescription(navMenu.getDescription())
                .withCount(navMenu.getCount())
                .withLanguageCode(navMenu.getTranslation().getLanguageCode())
                .withSourceLanguageCode(navMenu.getTranslation().getSourceLanguageCode())
                .withTranslationGroupId(navMenu.getTranslation().getTranslationGroup().getId())
                .withNavMenuItemDTOList(navMenuItemMapper.navMenuItemToNavMenuItemDto(navMenu.getNavMenuItems()))
                .build();

        setTranslationsDTO(navMenu, navMenuDTO, activeLanguages);

        return navMenuDTO;
    }

    private void setTranslationsDTO(NavMenu navMenu, NavMenuDTO navMenuDTO, List<Language> activeLanguages) {
        Set<Translation> translations = navMenu.getTranslation().getTranslationGroup().getTranslations();
        if (translations != null) {
            Map<String, Translation> stringTranslationMap = mapTranslationsByLanguageCode(translations);
            for (Language activeLanguage : activeLanguages) {
                // I don't need the language that the page is displayed
                if (!activeLanguage.getCode().equals(navMenu.getTranslation().getLanguageCode())) {
                    Translation translation = stringTranslationMap.get(activeLanguage.getCode());
                    if (translation != null) {
                        navMenuDTO.addTranslationDTO(translationMapper.translationToTranslationDTO(translation));
                    } else {
                        TranslationDTO translationDTO = new TranslationDTO();
                        translationDTO.setLanguageCode(activeLanguage.getCode());
                        translationDTO.setSourceLanguageCode(navMenu.getTranslation().getLanguageCode());
                        navMenuDTO.addTranslationDTO(translationDTO);
                    }
                }
            }
        }
    }

    private Map<String, Translation> mapTranslationsByLanguageCode(Set<Translation> translations) {
        HashMap<String, Translation> result = new HashMap<>();
        for (Translation translation : translations) {
            result.put(translation.getLanguageCode(), translation);
        }
        return result;
    }

    public NavMenu navMenuDTOToNavMenu(NavMenuDTO navMenuDTO) {
        return navMenuDTOToNavMenu(new NavMenu(), navMenuDTO);
    }

    public NavMenu navMenuDTOToNavMenu(NavMenu existingNavMenu, NavMenuDTO navMenuDTO) {
        existingNavMenu.setId(navMenuDTO.getId());

        Term term = existingNavMenu.getTerm();
        if (term == null) {
            term = new Term();
        }
        term.setId(navMenuDTO.getTerm().getId());
        term.setName(navMenuDTO.getTerm().getName());
        term.setSlug(SlugUtil.getFileNameSlug(navMenuDTO.getTerm().getName()));

        existingNavMenu.setDescription(navMenuDTO.getDescription());

        Set<NavMenuItem> existingNavMenuItems = existingNavMenu.getNavMenuItems();
        List<NavMenuItem> toBeDeletedNavMenuItems = new ArrayList<>();
        if (existingNavMenuItems == null) {
            existingNavMenu.setNavMenuItems(navMenuItemMapper.navMenuItemDtoToNavMenuItem(navMenuDTO, navMenuDTO.getNavMenuItemDTOs()));
        } else {
            // search existing one
            Iterator<NavMenuItem> iterator = existingNavMenuItems.iterator();
            while (iterator.hasNext()) {
                NavMenuItem navMenuItem = iterator.next();
                NavMenuItemDTO existingNavMenuItemDto = findAndRemoveNavMenuItem(navMenuItem, navMenuDTO.getNavMenuItemDTOs());
                if (existingNavMenuItemDto != null) {
                    navMenuItem.setTitle(existingNavMenuItemDto.getTitle());
                } else {
                    // remove the item if not present in the list
                    toBeDeletedNavMenuItems.add(navMenuItem);
                    iterator.remove();
                }
            }
            // add the rest of the nav menu items
            existingNavMenuItems.addAll(navMenuItemMapper.navMenuItemDtoToNavMenuItem(navMenuDTO, navMenuDTO.getNavMenuItemDTOs()));
            navMenuItemRepository.delete(toBeDeletedNavMenuItems);
        }

        return existingNavMenu;
    }

    private NavMenuItemDTO findAndRemoveNavMenuItem(NavMenuItem navMenuItem, List<NavMenuItemDTO> navMenuItemDTOs) {
        Iterator<NavMenuItemDTO> iterator = navMenuItemDTOs.iterator();
        while (iterator.hasNext()) {
            NavMenuItemDTO navMenuItemDTO = iterator.next();
            if (navMenuItemDTO.getId() != null && navMenuItemDTO.getId().equals(navMenuItem.getId())) {
                iterator.remove();
                return navMenuItemDTO;
            }
        }
        return null;
    }
}
