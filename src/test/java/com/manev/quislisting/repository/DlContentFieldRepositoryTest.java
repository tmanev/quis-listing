package com.manev.quislisting.repository;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
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
public class DlContentFieldRepositoryTest {

    @Inject
    private DlContentFieldRepository dlContentFieldRepository;

    @Inject
    private DlCategoryRepository dlCategoryRepository;

    @Test
    public void findAllByCategoryAndNoCategory() {
        System.out.println(dlContentFieldRepository.findAll().size());

        DlCategory dlCategory = dlCategoryRepository.findOne(2L);
        List<DlContentField> allByDlCategoriesOrDlCategoriesIsNull = dlContentFieldRepository.findAllByDlCategoriesOrDlCategoriesIsNullAndEnabledOrderByOrderNum(dlCategory, Boolean.TRUE);
        System.out.println(allByDlCategoriesOrDlCategoriesIsNull.size());
    }

}