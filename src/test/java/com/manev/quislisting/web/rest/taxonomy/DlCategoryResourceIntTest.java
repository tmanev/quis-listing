package com.manev.quislisting.web.rest.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlCategoryBuilder;
import com.manev.quislisting.repository.taxonomy.TermTaxonomyRepository;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.web.rest.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_DL_CATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_SLUG = "DEFAULT_SLUG";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";
    private static final Long DEFAULT_PARENT_ID = 0L;
    private static final Long DEFAULT_COUNT = 0L;

    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String UPDATED_SLUG = "UPDATED_SLUG";
    private static final String UPDATED_DESCRIPTION = "UPDATED_DESCRIPTION";
    private static final Long UPDATED_PARENT_ID = 1L;
    private static final Long UPDATED_COUNT = 1L;

    @Autowired
    private DlCategoryService dlCategoryService;

    @Autowired
    private TermTaxonomyRepository<DlCategory> termTaxonomyRepository;

    @Autowired
    private DlCategoryMapper dlCategoryMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restDlCategoryMockMvc;

    private DlCategory dlCategory;

    public static DlCategory createEntity(EntityManager em) {
        return DlCategoryBuilder.aDlCategory().withTerm(
                TermBuilder.aTerm().withName(DEFAULT_NAME).withSlug(DEFAULT_SLUG).build()
        ).withDescription(DEFAULT_DESCRIPTION).withParentId(DEFAULT_PARENT_ID)
                .withCount(DEFAULT_COUNT).build();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlCategoryResource dlCategoryResource = new DlCategoryResource(dlCategoryService);
        this.restDlCategoryMockMvc = MockMvcBuilders.standaloneSetup(dlCategoryResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        termTaxonomyRepository.deleteAll();
        dlCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDlCategory() throws Exception {
        int databaseSizeBeforeCreate = termTaxonomyRepository.findAll().size();

        // Create the DlCategory
        DlCategoryDTO dlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);

        restDlCategoryMockMvc.perform(post(RESOURCE_API_DL_CATEGORIES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlCategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the DlCategory in the database
        List<DlCategory> dlCategoryList = termTaxonomyRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        DlCategory dlCategory = dlCategoryList.get(dlCategoryList.size() - 1);
        assertThat(dlCategory.getTerm().getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlCategory.getTerm().getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(dlCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(dlCategory.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(dlCategory.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    @Transactional
    public void createDlCategoryExistingId() throws Exception {
        int databaseSizeBeforeCreate = termTaxonomyRepository.findAll().size();

        // Create the DlCategory with an existing ID
        DlCategory existingDlCategory = new DlCategory();
        existingDlCategory.setId(1L);
        DlCategoryDTO existingDlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(existingDlCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDlCategoryMockMvc.perform(post(RESOURCE_API_DL_CATEGORIES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingDlCategoryDTO)))
                .andExpect(status().isBadRequest());

        // Validate the DlCategory in the database
        List<DlCategory> dlCategoryList = termTaxonomyRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDlCategories() throws Exception {
        // Initialize the database
        termTaxonomyRepository.saveAndFlush(dlCategory);

        // Get all the dlCategories
        restDlCategoryMockMvc.perform(get(RESOURCE_API_DL_CATEGORIES + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dlCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].term.name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
                .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));
    }

    @Test
    @Transactional
    public void getDlCategory() throws Exception {
        // Initialize the database
        termTaxonomyRepository.saveAndFlush(dlCategory);

        // Get the DlCategory
        restDlCategoryMockMvc.perform(get(RESOURCE_API_DL_CATEGORIES + "/{id}", dlCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(dlCategory.getId().intValue()))
                .andExpect(jsonPath("$.term.name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
                .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDlCategory() throws Exception {
        // Get the dlCategory
        restDlCategoryMockMvc.perform(get(RESOURCE_API_DL_CATEGORIES + "/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDlCategory() throws Exception {
        // Initialize the database
        termTaxonomyRepository.saveAndFlush(dlCategory);
        int databaseSizeBeforeUpdate = termTaxonomyRepository.findAll().size();

        // Update the DlCategory
        DlCategory updatedDlCategory = termTaxonomyRepository.findOne(dlCategory.getId());
        updatedDlCategory.getTerm().name(UPDATED_NAME).slug(UPDATED_SLUG);

        updatedDlCategory.setDescription(UPDATED_DESCRIPTION);
        updatedDlCategory.setParentId(UPDATED_PARENT_ID);
        updatedDlCategory.setCount(UPDATED_COUNT);
        DlCategoryDTO dlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(updatedDlCategory);

        restDlCategoryMockMvc.perform(put(RESOURCE_API_DL_CATEGORIES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlCategoryDTO)))
                .andExpect(status().isOk());

        // Validate the DlCategory in the database
        List<DlCategory> dlCategoryList = termTaxonomyRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeUpdate);
        DlCategory testDlCategory = dlCategoryList.get(dlCategoryList.size() - 1);
        assertThat(testDlCategory.getTerm().getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDlCategory.getTerm().getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testDlCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDlCategory.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testDlCategory.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingDlCategory() throws Exception {
        int databaseSizeBeforeUpdate = termTaxonomyRepository.findAll().size();

        // Create the DlCategory
        DlCategoryDTO dlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDlCategoryMockMvc.perform(put(RESOURCE_API_DL_CATEGORIES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlCategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the DlCategory in the database
        List<DlCategory> dlCategoryList = termTaxonomyRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDlCategory() throws Exception {
        // Initialize the database
        termTaxonomyRepository.saveAndFlush(dlCategory);
        int databaseSizeBeforeDelete = termTaxonomyRepository.findAll().size();

        // Get the dlCategory
        restDlCategoryMockMvc.perform(delete(RESOURCE_API_DL_CATEGORIES + "/{id}", dlCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DlCategory> dlCategoryList = termTaxonomyRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}