package com.manev.quislisting.repository.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
@Transactional
public class DlCategoryRepositoryTest {

    @Inject
    DlCategoryRepository dlCategoryRepository;

    @Test
    public void findAllTest() {
        List<DlCategory> all = dlCategoryRepository.findAll();
        Assert.assertEquals(6, all.size());
    }

}