package com.manev.quislisting.repository;

import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermTaxonomyRepository<T extends TermTaxonomy> extends JpaRepository<T, Long> {
}
