package com.manev.quislisting.repository;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.domain.Translation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StaticPageRepository extends JpaRepository<StaticPage, Long> {

    Page<StaticPage> findAllByTranslation_languageCode(Pageable pageable, String languageCode);

    Long countByTranslation_languageCode(String languageCode);

    StaticPage findOneByTranslation(Translation translationForLanguage);

    StaticPage findOneByName(String name);
}
