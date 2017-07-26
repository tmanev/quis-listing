package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.StaticPageNavMenuRel;
import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.StaticPageNavMenuRelRepository;
import com.manev.quislisting.service.post.dto.TranslationDTO;
import com.manev.quislisting.service.post.mapper.TranslationMapper;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.StaticPageNavMenuDTO;
import com.manev.quislisting.service.taxonomy.dto.builder.NavMenuDTOBuilder;
import com.manev.quislisting.service.util.SlugUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NavMenuMapper {

    private NavMenuItemMapper navMenuItemMapper;
    private StaticPageNavMenuRelRepository navMenuItemRepository;
    private TranslationMapper translationMapper;

    public NavMenuMapper(NavMenuItemMapper navMenuItemMapper, StaticPageNavMenuRelRepository staticPageNavMenuRelRepository, TranslationMapper translationMapper) {
        this.navMenuItemMapper = navMenuItemMapper;
        this.navMenuItemRepository = staticPageNavMenuRelRepository;
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
                .withName(navMenu.getName())
                .withSlug(navMenu.getSlug())
                .withDescription(navMenu.getDescription())
                .withCount(navMenu.getCount())
                .withLanguageCode(navMenu.getTranslation().getLanguageCode())
                .withSourceLanguageCode(navMenu.getTranslation().getSourceLanguageCode())
                .withTranslationGroupId(navMenu.getTranslation().getTranslationGroup().getId())
                .withNavMenuItemDTOList(navMenuItemMapper.navMenuItemToNavMenuItemDto(navMenu.getStaticPageNavMenuRels()))
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

        existingNavMenu.setName(navMenuDTO.getName());
        existingNavMenu.setSlug(SlugUtil.getFileNameSlug(navMenuDTO.getName()));

        existingNavMenu.setDescription(navMenuDTO.getDescription());

        Set<StaticPageNavMenuRel> existingNavMenuItems = existingNavMenu.getStaticPageNavMenuRels();
        List<StaticPageNavMenuRel> toBeDeletedNavMenuItems = new ArrayList<>();
        if (existingNavMenuItems == null) {
            existingNavMenu.setStaticPageNavMenuRels(navMenuItemMapper.navMenuItemDtoToNavMenuItem(existingNavMenu, navMenuDTO.getStaticPageNavMenuDTOS()));
        } else {
            // search existing one
            Iterator<StaticPageNavMenuRel> iterator = existingNavMenuItems.iterator();
            while (iterator.hasNext()) {
                StaticPageNavMenuRel navMenuItem = iterator.next();
                StaticPageNavMenuDTO existingStaticPageNavMenuDto = findAndRemoveNavMenuItem(navMenuItem, navMenuDTO.getStaticPageNavMenuDTOS());
                if (existingStaticPageNavMenuDto == null) {
                    // remove the item if not present in the list
                    toBeDeletedNavMenuItems.add(navMenuItem);
                    iterator.remove();
                }
            }
            // add the rest of the nav menu items
            existingNavMenuItems.addAll(navMenuItemMapper.navMenuItemDtoToNavMenuItem(existingNavMenu, navMenuDTO.getStaticPageNavMenuDTOS()));
            navMenuItemRepository.delete(toBeDeletedNavMenuItems);
        }

        return existingNavMenu;
    }

    private StaticPageNavMenuDTO findAndRemoveNavMenuItem(StaticPageNavMenuRel navMenuItem, List<StaticPageNavMenuDTO> staticPageNavMenuDTOS) {
        Iterator<StaticPageNavMenuDTO> iterator = staticPageNavMenuDTOS.iterator();
        while (iterator.hasNext()) {
            StaticPageNavMenuDTO staticPageNavMenuDTO = iterator.next();
            if (staticPageNavMenuDTO.getId() != null && staticPageNavMenuDTO.getId().equals(navMenuItem.getId())) {
                iterator.remove();
                return staticPageNavMenuDTO;
            }
        }
        return null;
    }
}
