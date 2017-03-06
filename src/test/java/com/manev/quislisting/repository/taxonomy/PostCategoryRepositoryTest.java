package com.manev.quislisting.repository.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.PostCategoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
@Transactional
public class PostCategoryRepositoryTest {

    @Inject
    private PostCategoryRepository postCategoryRepository;

    @Test
    public void findAllTest() {
        List<PostCategory> all = postCategoryRepository.findAll();
        System.out.println(all.size());
    }

    @Test
    public void saveTest() {
        PostCategory postCategory = PostCategoryBuilder.aPostCategory()
                .withTerm(TermBuilder.aTerm()
                        .withName("Category 1")
                        .withSlug("category-1")
                        .build()
                ).withDescription("Some description")
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode("en")
                        .withTranslationGroup(new TranslationGroup())
                        .build())
                .build();

        PostCategory savedPostCategory = postCategoryRepository.save(postCategory);

        Term term2 = new Term();
        term2.setName("Sub Cat Name 2");
        term2.setSlug("sub-cat-name-2");

        PostCategory postCategorySub = PostCategoryBuilder.aPostCategory()
                .withTerm(TermBuilder.aTerm().withName("Sub Category 1").withSlug("sub-category-1").build())
                .withDescription("Sub category description")
                .withParent(savedPostCategory)
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode("en")
                        .withTranslationGroup(new TranslationGroup())
                        .build())
                .build();

        PostCategory savedPostSubCategory = postCategoryRepository.save(postCategorySub);

        PostCategory child = postCategoryRepository.findOne(savedPostSubCategory.getId());
        assertEquals(savedPostCategory.getId(), child.getParent() != null ? child.getParent().getId() : null);
    }

}