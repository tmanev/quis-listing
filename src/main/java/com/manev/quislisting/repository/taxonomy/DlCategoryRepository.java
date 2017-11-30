package com.manev.quislisting.repository.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.model.CategoryCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DlCategoryRepository extends TermTaxonomyRepository<DlCategory> {
    void deleteAllByParent(DlCategory parent);

    @Query(value = "select cat.name, count(*) as count from ql_dl_listing_dl_category_relationship dldlcr LEFT JOIN ql_term_taxonomy cat ON dldlcr.term_taxonomy_id = cat.id GROUP BY cat.id;", nativeQuery = true)
    List<CategoryCount> findCategoriesWithCount();
}
