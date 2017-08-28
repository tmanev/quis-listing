package com.manev.quislisting.repository.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.web.rest.taxonomy.DlCategoryResourceIntTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
@Transactional
public class DlCategoryRepositoryTest {

    @Inject
    private DlCategoryRepository dlCategoryRepository;

    @Test
    public void findAllTest() {
        List<DlCategory> all = dlCategoryRepository.findAll();
        Assert.assertEquals(0, all.size());
    }

    @Test
    public void saveDlCategoryTest() {
        int size = dlCategoryRepository.findAll().size();
        DlCategory dlCategory = DlCategoryResourceIntTest.createEntity();
        DlCategory dlCategorySaved = dlCategoryRepository.saveAndFlush(dlCategory);
        assertEquals(size + 1, dlCategoryRepository.findAll().size());

        // save translation
        DlCategory entity2 = DlCategoryResourceIntTest.createEntity2();
        entity2.getTranslation().setTranslationGroup(dlCategorySaved.getTranslation().getTranslationGroup());
        dlCategoryRepository.saveAndFlush(entity2);
        assertEquals(size + 2, dlCategoryRepository.findAll().size());
    }

    @Test
    public void findAllByLanguageCode() {
        Page<DlCategory> translationsEn = dlCategoryRepository.findAllByTranslation_languageCode(new PageRequest(0, 20), "en");
        Assert.assertEquals(0, translationsEn.getContent().size());
        Page<DlCategory> translationsBg = dlCategoryRepository.findAllByTranslation_languageCode(new PageRequest(0, 20), "bg");
        Assert.assertEquals(0, translationsBg.getContent().size());
        Page<DlCategory> translationsNull = dlCategoryRepository.findAllByTranslation_languageCode(new PageRequest(0, 20), null);
        Assert.assertEquals(0, translationsNull.getContent().size());
    }

    @Test
    public void countByLanguageCode() {
        Long count = dlCategoryRepository.countByTranslation_languageCode("en");
        assertEquals(0L, count.longValue());
    }

}