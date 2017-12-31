package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import com.manev.quislisting.service.taxonomy.dto.TranslatedTermDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TranslateTermConverter implements Converter<TermTaxonomy, TranslatedTermDTO> {

    @Override
    public TranslatedTermDTO convert(TermTaxonomy termTaxonomy) {
        TranslatedTermDTO translatedTermDTO = new TranslatedTermDTO();

        translatedTermDTO.setId(termTaxonomy.getId());
        translatedTermDTO.setName(termTaxonomy.getName());
        translatedTermDTO.setTranslationGroupId(termTaxonomy.getTranslation().getTranslationGroup().getId());

        translatedTermDTO.setLangKey(termTaxonomy.getTranslation().getLanguageCode());

        return translatedTermDTO;
    }

}
