package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.service.post.dto.StaticPageDTO;
import com.manev.quislisting.service.post.dto.TranslationDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class QlStaticPageMapper {

    private TranslationMapper translationMapper;

    public QlStaticPageMapper(TranslationMapper translationMapper) {
        this.translationMapper = translationMapper;
    }

    public StaticPage pageDTOtoPage(StaticPageDTO staticPageDTO) {
        return pageDTOtoPage(new StaticPage(), staticPageDTO);
    }

    public StaticPage pageDTOtoPage(StaticPage staticPage, StaticPageDTO staticPageDTO) {
        staticPage.setId(staticPageDTO.getId());
        staticPage.setContent(staticPageDTO.getContent());
        staticPage.setName(staticPageDTO.getName());
        staticPage.setStatus(staticPageDTO.getStatus());
        staticPage.setTitle(staticPageDTO.getTitle());

        return staticPage;
    }

    public StaticPageDTO staticPageToStaticPageDTO(StaticPage qlPage, List<Language> activeLanguages) {
        StaticPageDTO staticPageDTO = new StaticPageDTO();
        staticPageDTO.setId(qlPage.getId());
        staticPageDTO.setTitle(qlPage.getTitle());
        staticPageDTO.setName(qlPage.getName());
        staticPageDTO.setContent(qlPage.getContent());
        staticPageDTO.setStatus(qlPage.getStatus());
        staticPageDTO.setLanguageCode(qlPage.getTranslation().getLanguageCode());
        staticPageDTO.setTranslationGroupId(qlPage.getTranslation().getTranslationGroup().getId());
        staticPageDTO.setSourceLanguageCode(qlPage.getTranslation().getSourceLanguageCode());

        setTranslationsDTO(qlPage, staticPageDTO, activeLanguages);

        return staticPageDTO;
    }

    private void setTranslationsDTO(StaticPage qlPage, StaticPageDTO staticPageDTO, List<Language> activeLanguages) {
        Set<Translation> translations = qlPage.getTranslation().getTranslationGroup().getTranslations();
        if (translations != null) {
            Map<String, Translation> stringTranslationMap = mapTranslationsByLanguageCode(translations);
            for (Language activeLanguage : activeLanguages) {
                // I don't need the language that the page is displayed
                if (!activeLanguage.getCode().equals(qlPage.getTranslation().getLanguageCode())) {
                    Translation translation = stringTranslationMap.get(activeLanguage.getCode());
                    if (translation != null) {
                        staticPageDTO.addTranslationDTO(translationMapper.translationToTranslationDTO(translation));
                    } else {
                        TranslationDTO translationDTO = new TranslationDTO();
                        translationDTO.setLanguageCode(activeLanguage.getCode());
                        translationDTO.setSourceLanguageCode(qlPage.getTranslation().getLanguageCode());
                        staticPageDTO.addTranslationDTO(translationDTO);
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

}
