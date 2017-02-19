package com.manev.quislisting.repository.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NavMenuRepository extends TermTaxonomyRepository<NavMenu> {
}
