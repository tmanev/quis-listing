package com.manev.quislisting.repository.taxonomy;

import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PostCategoryRepository extends TermTaxonomyRepository<PostCategory> {
}
