package com.manev.quislisting.repository.qlml;

import com.manev.quislisting.domain.qlml.LanguageTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageTranslationRepository extends JpaRepository<LanguageTranslation, Long> {

    List<LanguageTranslation> findAllByLanguageCodeInAndDisplayLanguageCode(List<String> activeLanguages, String language);
}
