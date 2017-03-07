package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import org.springframework.stereotype.Component;

@Component
public class ActiveLanguageMapper {

    public ActiveLanguageDTO toActiveLanguageDTO(Language language, Long count) {
        ActiveLanguageDTO activeLanguageDTO = new ActiveLanguageDTO();
        activeLanguageDTO.setCode(language.getCode());
        activeLanguageDTO.setEnglishName(language.getEnglishName());
        activeLanguageDTO.setCount(count);

        return activeLanguageDTO;
    }
}
