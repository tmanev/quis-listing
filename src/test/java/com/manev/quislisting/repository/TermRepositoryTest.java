package com.manev.quislisting.repository;

import com.manev.QuisListingApplication;
import com.manev.quislisting.domain.Term;
import com.manev.quislisting.domain.TermTaxonomy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApplication.class)
//@Transactional
public class TermRepositoryTest {

    @Inject
    TermTaxonomyRepository termTaxonomyRepository;
    @Inject
    private TermRepository termRepository;

    @Test
    public void saveTest() {
        Term term = new Term();
        term.setName("Cat Name");
        term.setSlug("cat-name");

        TermTaxonomy termTaxonomy = new TermTaxonomy();
        termTaxonomy.setDescription("Some description");
        termTaxonomy.setTaxonomy("nav_menu_item");
        termTaxonomy.setTerm(term);

        TermTaxonomy savedTermTaxonomy = termTaxonomyRepository.save(termTaxonomy);

        Term term2 = new Term();
        term2.setName("Sub Cat Name 2");
        term2.setSlug("sub-cat-name-2");

        TermTaxonomy termTaxonomy2 = new TermTaxonomy();
        termTaxonomy2.setDescription("Sub category description");
        termTaxonomy2.setTaxonomy("nav_menu_item");
        termTaxonomy2.setTerm(term2);
        termTaxonomy2.setParent(termTaxonomy);

        TermTaxonomy savedTermTaxonomy2 = termTaxonomyRepository.save(termTaxonomy2);

        TermTaxonomy one = termTaxonomyRepository.findOne(savedTermTaxonomy2.getId());
        TermTaxonomy parent = termTaxonomyRepository.findOne(savedTermTaxonomy.getId());
        assertEquals(termTaxonomy.getDescription(), one.getParent().getDescription());
    }

}