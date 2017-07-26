package com.manev.quislisting.repository.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DlLocationRepository extends TermTaxonomyRepository<DlLocation> {
    void deleteAllByParent(DlLocation parent);

    List<DlLocation> findAllByParentAndTranslation_languageCode(DlLocation parent, String language);
}
