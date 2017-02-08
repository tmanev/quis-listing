package com.manev.quislisting.repository;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.PostCategoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
@Transactional
public class TermRepositoryTest {

    @Inject
    TermTaxonomyRepository<PostCategory> termTaxonomyRepository;

    @Test
    public void saveTest() {
        PostCategory postCategory = PostCategoryBuilder.aPostCategory()
                .withTerm(
                        TermBuilder.aTerm().withName("Category 1").withSlug("category-1").build()
                ).withDescription("Some description").build();

        PostCategory savedPostCategory = termTaxonomyRepository.save(postCategory);

        Term term2 = new Term();
        term2.setName("Sub Cat Name 2");
        term2.setSlug("sub-cat-name-2");

        PostCategory postCategorySub = PostCategoryBuilder.aPostCategory()
                .withTerm(TermBuilder.aTerm().withName("Sub Category 1").withSlug("sub-category-1").build())
                .withDescription("Sub category description")
                .withParentId(savedPostCategory.getId()).build();

        PostCategory savedPostSubCategory = termTaxonomyRepository.save(postCategorySub);

        TermTaxonomy child = termTaxonomyRepository.findOne(savedPostSubCategory.getId());
        assertEquals(savedPostCategory.getId(), child.getParentId());
    }

}