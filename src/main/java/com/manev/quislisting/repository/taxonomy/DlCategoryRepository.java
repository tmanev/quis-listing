package com.manev.quislisting.repository.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DlCategoryRepository extends TermTaxonomyRepository<DlCategory> {
    void deleteAllByParent(DlCategory parent);

}
