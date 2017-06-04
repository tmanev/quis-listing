package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.post.PostMeta;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.service.post.dto.QlPageDTO;
import com.manev.quislisting.service.post.dto.TranslationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class QlPageMapper {

    private final Logger log = LoggerFactory.getLogger(QlPageMapper.class);

    private TranslationMapper translationMapper;

    public QlPageMapper(TranslationMapper translationMapper) {
        this.translationMapper = translationMapper;
    }

    public QlPage pageDTOtoPage(QlPageDTO qlPageDTO) {
        return pageDTOtoPage(new QlPage(), qlPageDTO);
    }

    public QlPage pageDTOtoPage(QlPage qlPage, QlPageDTO qlPageDTO) {
        qlPage.setId(qlPageDTO.getId());
        qlPage.setContent(qlPageDTO.getContent());
        qlPage.setName(qlPageDTO.getName());
        qlPage.setStatus(qlPageDTO.getStatus());
        qlPage.setTitle(qlPageDTO.getTitle());

        return qlPage;
    }

    public QlPageDTO pageToPageDTO(QlPage qlPage, List<Language> activeLanguages) {
        QlPageDTO qlPageDTO = new QlPageDTO();
        qlPageDTO.setId(qlPage.getId());
        qlPageDTO.setTitle(qlPage.getTitle());
        qlPageDTO.setName(qlPage.getName());
        qlPageDTO.setContent(qlPage.getContent());
        qlPageDTO.setCreated(qlPage.getCreated());
        qlPageDTO.setModified(qlPage.getModified());
        qlPageDTO.setStatus(qlPage.getStatus());
        qlPageDTO.setViews(qlPage.getPostMetaValue(PostMeta.META_KEY_POST_VIEWS_COUNT));
        qlPageDTO.setLanguageCode(qlPage.getTranslation().getLanguageCode());
        qlPageDTO.setTranslationGroupId(qlPage.getTranslation().getTranslationGroup().getId());
        qlPageDTO.setSourceLanguageCode(qlPage.getTranslation().getSourceLanguageCode());

        setTranslationsDTO(qlPage, qlPageDTO, activeLanguages);

        return qlPageDTO;
    }

    private void setTranslationsDTO(QlPage qlPage, QlPageDTO qlPageDTO, List<Language> activeLanguages) {
        Set<Translation> translations = qlPage.getTranslation().getTranslationGroup().getTranslations();
        if (translations!=null) {
            Map<String, Translation> stringTranslationMap = mapTranslationsByLanguageCode(translations);
            for (Language activeLanguage : activeLanguages) {
                // I don't need the language that the page is displayed
                if (!activeLanguage.getCode().equals(qlPage.getTranslation().getLanguageCode())) {
                    Translation translation = stringTranslationMap.get(activeLanguage.getCode());
                    if (translation != null) {
                        qlPageDTO.addTranslationDTO(translationMapper.translationToTranslationDTO(translation));
                    } else {
                        TranslationDTO translationDTO = new TranslationDTO();
                        translationDTO.setLanguageCode(activeLanguage.getCode());
                        translationDTO.setSourceLanguageCode(qlPage.getTranslation().getLanguageCode());
                        qlPageDTO.addTranslationDTO(translationDTO);
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
