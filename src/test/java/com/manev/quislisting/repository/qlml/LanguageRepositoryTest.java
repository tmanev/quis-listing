package com.manev.quislisting.repository.qlml;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.qlml.Language;
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
public class LanguageRepositoryTest {

    @Inject
    private LanguageRepository languageRepository;

    @Test
    public void findAllByActive() throws Exception {
        List<Language> allByActive = languageRepository.findAllByActive(true);
        Assert.assertEquals(3, allByActive.size());
    }

}