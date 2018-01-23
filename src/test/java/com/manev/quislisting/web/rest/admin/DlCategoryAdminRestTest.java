package com.manev.quislisting.web.rest.admin;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlCategoryBuilder;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlCategoryAdminRestTest {

    public static final String DEFAULT_NAME = "DEFAULT_NAME";
    public static final String DEFAULT_SLUG = "DEFAULT_SLUG";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";
    private static final Long DEFAULT_PARENT_ID = null;
    private static final Long DEFAULT_COUNT = 0L;
    private static final String DEFAULT_LANGUAGE_CODE = "en";

    private static final String DEFAULT_NAME_2 = "DEFAULT_NAME_2";
    private static final String DEFAULT_SLUG_2 = "DEFAULT_SLUG_2";
    private static final String DEFAULT_DESCRIPTION_2 = "DEFAULT_DESCRIPTION_2";
    private static final Long DEFAULT_COUNT_2 = 0L;

    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String UPDATED_SLUG = "UPDATED_SLUG";
    private static final String UPDATED_DESCRIPTION = "UPDATED_DESCRIPTION";
    private static final Long UPDATED_COUNT = 0L;

    @Autowired
    private DlCategoryService dlCategoryService;

    @Autowired
    private DlCategoryRepository dlCategoryRepository;

    @Autowired
    private LanguageRepository languageRepository;

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

    public static DlCategory createEntity(String langKey) {
        return DlCategoryBuilder.aDlCategory()
                .withName(DEFAULT_NAME)
                .withSlug(DEFAULT_SLUG)
                .withDescription(DEFAULT_DESCRIPTION)
                .withCount(DEFAULT_COUNT)
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode(langKey)
                        .withTranslationGroup(new TranslationGroup())
                        .build())
                .build();
    }

    public static DlCategory createEntity2() {
        return DlCategoryBuilder.aDlCategory()
                .withName(DEFAULT_NAME_2)
                .withSlug(DEFAULT_SLUG_2)
                .withDescription(DEFAULT_DESCRIPTION_2)
                .withCount(DEFAULT_COUNT_2)
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode("bg")
                        .withTranslationGroup(new TranslationGroup())
                        .build())
                .build();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlCategoryAdminRest dlCategoryResource = new DlCategoryAdminRest(dlCategoryService);
        this.restDlCategoryMockMvc = MockMvcBuilders.standaloneSetup(dlCategoryResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dlCategoryRepository.deleteAllByParent(null);
        languageRepository.deleteAll();
        dlCategory = createEntity("en");
    }

    @Test
    @Transactional
    public void createDlCategory() throws Exception {
        int databaseSizeBeforeCreate = dlCategoryRepository.findAll().size();

        // Create the DlCategory
        DlCategoryDTO dlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);

        restDlCategoryMockMvc.perform(post(AdminRestRouter.DlCategory.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlCategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the DlCategory in the database
        List<DlCategory> dlCategoryTrList = dlCategoryRepository.findAll();
        assertThat(dlCategoryTrList).hasSize(databaseSizeBeforeCreate + 1);
        DlCategory dlCategorySaved = dlCategoryTrList.get(dlCategoryTrList.size() - 1);
        assertThat(dlCategorySaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlCategorySaved.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(dlCategorySaved.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(dlCategorySaved.getParent()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(dlCategorySaved.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(dlCategorySaved.getTranslation().getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
    }

    @Test
    @Transactional
    public void createDlCategoryExistingId() throws Exception {
        int databaseSizeBeforeCreate = dlCategoryRepository.findAll().size();

        // Create the DlCategory with an existing ID
        dlCategory.setId(1L);
        DlCategoryDTO existingDlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDlCategoryMockMvc.perform(post(AdminRestRouter.DlCategory.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingDlCategoryDTO)))
                .andExpect(status().isBadRequest());

        // Validate the DlCategory in the database
        List<DlCategory> dlCategoryList = dlCategoryRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDlCategories() throws Exception {
        // Initialize the database
        dlCategoryRepository.saveAndFlush(dlCategory);

        // Get all the dlCategories
        restDlCategoryMockMvc.perform(get(AdminRestRouter.DlCategory.LIST + "?sort=id,desc&languageCode=en"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dlCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
                .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));
    }

    @Test
    @Transactional
    public void getDlCategory() throws Exception {
        // Initialize the database
        dlCategoryRepository.saveAndFlush(dlCategory);

        // Get the DlCategory
        restDlCategoryMockMvc.perform(get(AdminRestRouter.DlCategory.DETAIL, dlCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(dlCategory.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
                .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getDlCategoryByTranslationId() throws Exception {
        // Initialize the database
        dlCategoryRepository.saveAndFlush(dlCategory);

        // Get the DlCategory
        restDlCategoryMockMvc.perform(get(AdminRestRouter.DlCategory.DETAIL_BY_TRANSLATION, dlCategory.getTranslation().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(dlCategory.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
                .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDlCategory() throws Exception {
        // Get the dlCategory
        restDlCategoryMockMvc.perform(get(AdminRestRouter.DlCategory.DETAIL, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDlCategory() throws Exception {
        // Initialize the database
        dlCategoryRepository.saveAndFlush(dlCategory);
        DlCategory parent = dlCategoryRepository.saveAndFlush(createEntity2());
        int databaseSizeBeforeUpdate = dlCategoryRepository.findAll().size();

        // Update the DlCategory
        DlCategory updatedDlCategory = dlCategoryRepository.findOne(this.dlCategory.getId());
        updatedDlCategory.setName(UPDATED_NAME);
        updatedDlCategory.setSlug(UPDATED_SLUG);

        updatedDlCategory.setDescription(UPDATED_DESCRIPTION);
        updatedDlCategory.setParent(parent);
        updatedDlCategory.setCount(UPDATED_COUNT);
        DlCategoryDTO dlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(updatedDlCategory);

        restDlCategoryMockMvc.perform(put(AdminRestRouter.DlCategory.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlCategoryDTO)))
                .andExpect(status().isOk());

        // Validate the DlCategory in the database
        List<DlCategory> dlCategoryList = dlCategoryRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeUpdate);
        DlCategory testDlCategory = dlCategoryRepository.findOne(updatedDlCategory.getId());
        assertThat(testDlCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDlCategory.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testDlCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDlCategory.getParent().getId()).isEqualTo(parent.getId());
        assertThat(testDlCategory.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingDlCategory() throws Exception {
        int databaseSizeBeforeUpdate = dlCategoryRepository.findAll().size();

        // Create the DlCategory
        DlCategoryDTO dlCategoryDTO = dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDlCategoryMockMvc.perform(put(AdminRestRouter.DlCategory.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlCategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the DlCategory in the database
        List<DlCategory> dlCategoryList = dlCategoryRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDlCategory() throws Exception {
        // Initialize the database
        dlCategoryRepository.saveAndFlush(dlCategory);
        int databaseSizeBeforeDelete = dlCategoryRepository.findAll().size();

        // Delete the dlCategory
        restDlCategoryMockMvc.perform(delete(AdminRestRouter.DlCategory.DETAIL, dlCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DlCategory> dlCategoryList = dlCategoryRepository.findAll();
        assertThat(dlCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void getActiveLanguages() throws Exception {
        // Initialize the database
        dlCategoryRepository.saveAndFlush(dlCategory);

        Language lanEn = new Language().code("en").active(true).englishName("English");
        Language lanBg = new Language().code("bg").active(true).englishName("Bulgarian");
        Language lanRo = new Language().code("ro").active(true).englishName("Romanian");
        Language lanRu = new Language().code("ru").active(true).englishName("Russian");
        languageRepository.saveAndFlush(lanEn);
        languageRepository.saveAndFlush(lanBg);
        languageRepository.saveAndFlush(lanRo);
        languageRepository.saveAndFlush(lanRu);

        // Get active languages
        restDlCategoryMockMvc.perform(get(AdminRestRouter.DlCategory.ACTIVE_LANGUAGES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].code").value(hasItem("en")))
                .andExpect(jsonPath("$.[*].englishName").value(hasItem("English")))
                .andExpect(jsonPath("$.[*].count").value(hasItem(1)));
    }
}