package com.manev.quislisting.repository;

import com.manev.quislisting.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TranslationRepository extends JpaRepository<Translation, Long> {
}
