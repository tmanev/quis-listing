package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.service.post.dto.TranslationDTO;
import org.springframework.stereotype.Component;

@Component
public class TranslationMapper {

    public TranslationDTO translationToTranslationDTO(Translation translation) {
        TranslationDTO translationDTO = new TranslationDTO();
        translationDTO.setId(translation.getId());
        translationDTO.setLanguageCode(translation.getLanguageCode());
        translationDTO.setSourceLanguageCode(translation.getSourceLanguageCode());

        return translationDTO;
    }



}
