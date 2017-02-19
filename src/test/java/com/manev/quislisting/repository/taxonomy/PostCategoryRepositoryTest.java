package com.manev.quislisting.repository.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;
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
public class PostCategoryRepositoryTest {

    @Inject
    PostCategoryRepository postCategoryRepository;

    @Test
    public void findAllTest() {
        List<PostCategory> all = postCategoryRepository.findAll();
        System.out.println(all.size());
    }

}