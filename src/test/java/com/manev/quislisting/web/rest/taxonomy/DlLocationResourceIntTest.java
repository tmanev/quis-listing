package com.manev.quislisting.web.rest.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlLocationBuilder;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
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

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_DL_LOCATIONS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlLocationResourceIntTest {

    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_SLUG = "DEFAULT_SLUG";
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
    private DlLocationService dlLocationService;

    @Autowired
    private DlLocationRepository dlLocationRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private DlLocationMapper dlLocationMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restDlLocationMockMvc;

    private DlLocation dlLocation;

    public static DlLocation createEntity() {
        return DlLocationBuilder.aDlLocation()
                .withTerm(TermBuilder.aTerm()
                        .withName(DEFAULT_NAME)
                        .withSlug(DEFAULT_SLUG)
                        .build())
                .withDescription(DEFAULT_DESCRIPTION)
                .withCount(DEFAULT_COUNT)
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode("en")
                        .withTranslationGroup(new TranslationGroup())
                        .build())
                .build();
    }

    public static DlLocation createEntity2() {
        return DlLocationBuilder.aDlLocation()
                .withTerm(TermBuilder.aTerm()
                        .withName(DEFAULT_NAME_2)
                        .withSlug(DEFAULT_SLUG_2)
                        .build())
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
        DlLocationResource dlLocationResource = new DlLocationResource(dlLocationService);
        this.restDlLocationMockMvc = MockMvcBuilders.standaloneSetup(dlLocationResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dlLocationRepository.deleteAllByParent(null);
        languageRepository.deleteAll();
        dlLocation = createEntity();
    }

    @Test
    @Transactional
    public void createDlLocation() throws Exception {
        int databaseSizeBeforeCreate = dlLocationRepository.findAll().size();

        // Create the DlLocation
        DlLocationDTO dlLocationDTO = dlLocationMapper.dlLocationToDlLocationDTO(dlLocation);

        restDlLocationMockMvc.perform(post(RESOURCE_API_ADMIN_DL_LOCATIONS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlLocationDTO)))
                .andExpect(status().isCreated());

        // Validate the DlLocation in the database
        List<DlLocation> dlLocationList = dlLocationRepository.findAll();
        assertThat(dlLocationList).hasSize(databaseSizeBeforeCreate + 1);
        DlLocation dlLocationSaved = dlLocationList.get(dlLocationList.size() - 1);
        assertThat(dlLocationSaved.getTerm().getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlLocationSaved.getTerm().getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(dlLocationSaved.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(dlLocationSaved.getParent()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(dlLocationSaved.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(dlLocationSaved.getTranslation().getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
    }

    @Test
    @Transactional
    public void createDlLocationExistingId() throws Exception {
        int databaseSizeBeforeCreate = dlLocationRepository.findAll().size();

        // Create the DlLocation with an existing ID
        DlLocation existingDlLocation = new DlLocation();
        existingDlLocation.setId(1L);
        DlLocationDTO existingDlLocationDTO = dlLocationMapper.dlLocationToDlLocationDTO(existingDlLocation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDlLocationMockMvc.perform(post(RESOURCE_API_ADMIN_DL_LOCATIONS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingDlLocationDTO)))
                .andExpect(status().isBadRequest());

        // Validate the DlLocation in the database
        List<DlLocation> dlLocationList = dlLocationRepository.findAll();
        assertThat(dlLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDlLocations() throws Exception {
        // Initialize the database
        dlLocationRepository.saveAndFlush(dlLocation);

        // Get all the dlLocations
        restDlLocationMockMvc.perform(get(RESOURCE_API_ADMIN_DL_LOCATIONS + "?sort=id,desc&languageCode=en"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dlLocation.getId().intValue())))
                .andExpect(jsonPath("$.[*].term.name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
                .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));
    }

    @Test
    @Transactional
    public void getDlLocation() throws Exception {
        // Initialize the database
        dlLocationRepository.saveAndFlush(dlLocation);

        // Get the DlLocation
        restDlLocationMockMvc.perform(get(RESOURCE_API_ADMIN_DL_LOCATIONS + "/{id}", dlLocation.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(dlLocation.getId().intValue()))
                .andExpect(jsonPath("$.term.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
                .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDlLocation() throws Exception {
        // Get the dlLocation
        restDlLocationMockMvc.perform(get(RESOURCE_API_ADMIN_DL_LOCATIONS + "/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDlLocation() throws Exception {
        // Initialize the database
        dlLocationRepository.saveAndFlush(dlLocation);
        DlLocation parent = dlLocationRepository.saveAndFlush(createEntity2());
        int databaseSizeBeforeUpdate = dlLocationRepository.findAll().size();

        // Update the DlLocation
        DlLocation updatedDlLocation = dlLocationRepository.findOne(dlLocation.getId());
        updatedDlLocation.getTerm().name(UPDATED_NAME).slug(UPDATED_SLUG);

        updatedDlLocation.setDescription(UPDATED_DESCRIPTION);
        updatedDlLocation.setParent(parent);
        updatedDlLocation.setCount(UPDATED_COUNT);
        DlLocationDTO dlLocationDTO = dlLocationMapper.dlLocationToDlLocationDTO(updatedDlLocation);

        restDlLocationMockMvc.perform(put(RESOURCE_API_ADMIN_DL_LOCATIONS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlLocationDTO)))
                .andExpect(status().isOk());

        // Validate the DlLocation in the database
        List<DlLocation> dlLocationList = dlLocationRepository.findAll();
        assertThat(dlLocationList).hasSize(databaseSizeBeforeUpdate);
        DlLocation testDlLocation = dlLocationRepository.findOne(updatedDlLocation.getId());
        assertThat(testDlLocation.getTerm().getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDlLocation.getTerm().getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testDlLocation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDlLocation.getParent().getId()).isEqualTo(parent.getId());
        assertThat(testDlLocation.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingDlLocation() throws Exception {
        int databaseSizeBeforeUpdate = dlLocationRepository.findAll().size();

        // Create the DlLocation
        DlLocationDTO dlLocationDTO = dlLocationMapper.dlLocationToDlLocationDTO(dlLocation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDlLocationMockMvc.perform(put(RESOURCE_API_ADMIN_DL_LOCATIONS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlLocationDTO)))
                .andExpect(status().isCreated());

        // Validate the DlLocation in the database
        List<DlLocation> dlLocationList = dlLocationRepository.findAll();
        assertThat(dlLocationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDlLocation() throws Exception {
        // Initialize the database
        dlLocationRepository.saveAndFlush(dlLocation);
        int databaseSizeBeforeDelete = dlLocationRepository.findAll().size();

        // Get the dlLocation
        restDlLocationMockMvc.perform(delete(RESOURCE_API_ADMIN_DL_LOCATIONS + "/{id}", dlLocation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DlLocation> dlLocationList = dlLocationRepository.findAll();
        assertThat(dlLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void getActiveLanguages() throws Exception {
        // Initialize the database
        dlLocationRepository.saveAndFlush(dlLocation);

        Language lanEn = new Language().code("en").active(true).englishName("English");
        Language lanBg = new Language().code("bg").active(true).englishName("Bulgarian");
        Language lanRo = new Language().code("ro").active(true).englishName("Romanian");
        Language lanRu = new Language().code("ru").active(true).englishName("Russian");
        languageRepository.saveAndFlush(lanEn);
        languageRepository.saveAndFlush(lanBg);
        languageRepository.saveAndFlush(lanRo);
        languageRepository.saveAndFlush(lanRu);

        // Get active languages
        restDlLocationMockMvc.perform(get(RESOURCE_API_ADMIN_DL_LOCATIONS + "/active-languages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].code").value(hasItem("en")))
                .andExpect(jsonPath("$.[*].englishName").value(hasItem("English")))
                .andExpect(jsonPath("$.[*].count").value(hasItem(1)));
    }
}