package com.manev.quislisting.repository.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
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
public class NavMenuRepositoryTest {

    @Inject
    private NavMenuRepository navMenuRepository;

    @Test
    public void testFindAll() {
        List<NavMenu> all = navMenuRepository.findAll();
    }

    @Test
    public void testFindOne() {
        NavMenu one = navMenuRepository.findOne(8L);
    }

}