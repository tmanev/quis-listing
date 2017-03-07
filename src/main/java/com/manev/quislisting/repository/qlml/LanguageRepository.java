package com.manev.quislisting.repository.qlml;

import com.manev.quislisting.domain.qlml.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    List<Language> findAllByActive(Boolean active);

}
