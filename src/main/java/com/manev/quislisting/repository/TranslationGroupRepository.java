package com.manev.quislisting.repository;

import com.manev.quislisting.domain.TranslationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TranslationGroupRepository extends JpaRepository<TranslationGroup, Long> {
}
