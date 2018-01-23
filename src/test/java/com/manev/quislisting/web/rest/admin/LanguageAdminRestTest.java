package com.manev.quislisting.web.rest.admin;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.service.qlml.LanguageService;
import com.manev.quislisting.web.rest.AdminRestRouter;
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
public class LanguageAdminRestTest {

    private static final String DEFAULT_CODE = "AAAAAA";
    private static final String UPDATED_CODE = "BBBBBB";

    private static final String DEFAULT_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_LOCALE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_LOCALE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restLanguageMockMvc;

    private Language language;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createEntity(EntityManager em) {
        Language language = new Language()
                .code(DEFAULT_CODE)
                .englishName(DEFAULT_ENGLISH_NAME)
                .defaultLocale(DEFAULT_DEFAULT_LOCALE)
                .active(DEFAULT_ACTIVE);
        return language;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LanguageAdminRest languageResource = new LanguageAdminRest(languageService);
        this.restLanguageMockMvc = MockMvcBuilders.standaloneSetup(languageResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        languageRepository.deleteAll();
        language = createEntity(em);
    }

    @Test
    @Transactional
    public void createLanguage() throws Exception {
        int databaseSizeBeforeCreate = languageRepository.findAll().size();

        // Create the Language

        restLanguageMockMvc.perform(post(AdminRestRouter.Language.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(language)))
                .andExpect(status().isCreated());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeCreate + 1);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLanguage.getEnglishName()).isEqualTo(DEFAULT_ENGLISH_NAME);
        assertThat(testLanguage.getDefaultLocale()).isEqualTo(DEFAULT_DEFAULT_LOCALE);
        assertThat(testLanguage.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createLanguageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = languageRepository.findAll().size();

        // Create the Language with an existing ID
        Language existingLanguage = new Language();
        existingLanguage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageMockMvc.perform(post(AdminRestRouter.Language.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingLanguage)))
                .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLanguages() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList
        restLanguageMockMvc.perform(get(AdminRestRouter.Language.LIST + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
                .andExpect(jsonPath("$.[*].englishName").value(hasItem(DEFAULT_ENGLISH_NAME)))
                .andExpect(jsonPath("$.[*].defaultLocale").value(hasItem(DEFAULT_DEFAULT_LOCALE)))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    public void getLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc.perform(get(AdminRestRouter.Language.DETAIL, language.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(language.getId().intValue()))
                .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
                .andExpect(jsonPath("$.englishName").value(DEFAULT_ENGLISH_NAME))
                .andExpect(jsonPath("$.defaultLocale").value(DEFAULT_DEFAULT_LOCALE))
                .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    public void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get(AdminRestRouter.Language.DETAIL, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguage() throws Exception {
        // Initialize the database
        languageService.save(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language
        Language updatedLanguage = languageRepository.findOne(language.getId());
        updatedLanguage
                .code(UPDATED_CODE)
                .englishName(UPDATED_ENGLISH_NAME)
                .defaultLocale(UPDATED_DEFAULT_LOCALE)
                .active(UPDATED_ACTIVE);

        restLanguageMockMvc.perform(put(AdminRestRouter.Language.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLanguage)))
                .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLanguage.getEnglishName()).isEqualTo(UPDATED_ENGLISH_NAME);
        assertThat(testLanguage.getDefaultLocale()).isEqualTo(UPDATED_DEFAULT_LOCALE);
        assertThat(testLanguage.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Create the Language

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLanguageMockMvc.perform(put(AdminRestRouter.Language.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(language)))
                .andExpect(status().isCreated());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLanguage() throws Exception {
        // Initialize the database
        languageService.save(language);

        int databaseSizeBeforeDelete = languageRepository.findAll().size();

        // Get the language
        restLanguageMockMvc.perform(delete(AdminRestRouter.Language.DETAIL, language.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeDelete - 1);
    }

}