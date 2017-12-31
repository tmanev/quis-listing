package com.manev.quislisting.repository.taxonomy;

import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface TermTaxonomyRepository<T extends TermTaxonomy> extends JpaRepository<T, Long> {
    Page<T> findAllByTranslation_languageCode(Pageable pageable, String languageCode);
    List<T> findAllByTranslation_languageCode(String languageCode);
    List<T> findAllByTranslation_translationGroup(TranslationGroup translationGroup);

    Long countByTranslation_languageCode(String languageCode);

    T findByTranslation_id(Long id);
}
