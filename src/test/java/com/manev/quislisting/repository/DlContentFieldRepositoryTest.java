package com.manev.quislisting.repository;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlContentField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
@Transactional
public class DlContentFieldRepositoryTest {

    @Inject
    private DlContentFieldRepository dlContentFieldRepository;

    @Test
    public void saveTest() {
    }

}