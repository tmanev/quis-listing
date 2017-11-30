package com.manev.quislisting.web.rest;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldGroup;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.DlContentFieldGroupRepository;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.service.DlContentFieldService;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.service.mapper.DlContentFieldMapper;
import com.manev.quislisting.service.util.SlugUtil;
import com.manev.quislisting.web.rest.taxonomy.DlCategoryResourceIntTest;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_DL_CONTENT_FIELDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlContentFieldResourceTest {

    private static final boolean DEFAULT_CORE_FIELD = true;
    private static final int DEFAULT_ORDER_NUM = 0;
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_SLUG = "DEFAULT_SLUG";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";
    private static final DlContentField.Type DEFAULT_TYPE = DlContentField.Type.STRING;
    private static final String DEFAULT_ICON_IMAGE = "DEFAULT_ICON_IMAGE";
    private static final boolean DEFAULT_REQUIRED = true;
    private static final boolean DEFAULT_HAS_CONFIGURATION = true;
    private static final boolean DEFAULT_HAS_SEARCH_CONFIGURATION = true;
    private static final boolean DEFAULT_CAN_BE_ORDERED = true;
    private static final boolean DEFAULT_HIDE_NAME = false;
    private static final boolean DEFAULT_ON_EXCERPT_PAGE = true;
    private static final boolean DEFAULT_ON_LISTING_PAGE = true;
    private static final boolean DEFAULT_ON_SEARCH_FORM = true;
    private static final boolean DEFAULT_ON_MAP = false;
    private static final boolean DEFAULT_ON_ADVANCED_SEARCH_FORM = true;
    private static final String DEFAULT_OPTIONS = "DEFAULT_OPTIONS";
    private static final String DEFAULT_SEARCH_OPTIONS = "DEFAULT_SEARCH_OPTIONS";

    private static final boolean UPDATED_CORE_FIELD = false;
    private static final int UPDATED_ORDER_NUM = 1;
    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String UPDATED_SLUG = "UPDATED_SLUG";
    private static final String UPDATED_DESCRIPTION = "UPDATED_DESCRIPTION";
    private static final DlContentField.Type UPDATED_TYPE = DlContentField.Type.NUMBER;
    private static final String UPDATED_ICON_IMAGE = "UPDATED_ICON_IMAGE";
    private static final boolean UPDATED_REQUIRED = false;
    private static final boolean UPDATED_HAS_CONFIGURATION = false;
    private static final boolean UPDATED_HAS_SEARCH_CONFIGURATION = false;
    private static final boolean UPDATED_CAN_BE_ORDERED = false;
    private static final boolean UPDATED_HIDE_NAME = true;
    private static final boolean UPDATED_ON_EXCERPT_PAGE = false;
    private static final boolean UPDATED_ON_LISTING_PAGE = false;
    private static final boolean UPDATED_ON_SEARCH_FORM = false;
    private static final boolean UPDATED_ON_MAP = true;
    private static final boolean UPDATED_ON_ADVANCED_SEARCH_FORM = false;
    private static final String UPDATED_OPTIONS = "UPDATED_OPTIONS";
    private static final String UPDATED_SEARCH_OPTIONS = "UPDATED_SEARCH_OPTIONS";

    @Autowired
    private DlContentFieldService dlContentFieldService;

    @Autowired
    private DlContentFieldRepository dlContentFieldRepository;

    @Autowired
    private DlContentFieldGroupRepository dlContentFieldGroupRepository;

    @Autowired
    private DlCategoryRepository dlCategoryRepository;

    @Autowired
    private DlContentFieldMapper dlContentFieldMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDlContentFieldMockMvc;

    private DlContentField dlContentField;
    private DlCategory dlCategory;
    private DlCategory dlCategory2;
    private DlContentFieldGroup dlContentFieldGroup;

    public static DlContentField createEntity() {
        return new DlContentField()
                .coreField(DEFAULT_CORE_FIELD)
                .orderNum(DEFAULT_ORDER_NUM)
                .name(DEFAULT_NAME)
                .slug(DEFAULT_SLUG)
                .description(DEFAULT_DESCRIPTION)
                .type(DEFAULT_TYPE)
                .iconImage(DEFAULT_ICON_IMAGE)
                .required(DEFAULT_REQUIRED)
                .hasConfiguration(DEFAULT_HAS_CONFIGURATION)
                .hasSearchConfiguration(DEFAULT_HAS_SEARCH_CONFIGURATION)
                .canBeOrdered(DEFAULT_CAN_BE_ORDERED)
                .hideName(DEFAULT_HIDE_NAME)
                .onExcerptPage(DEFAULT_ON_EXCERPT_PAGE)
                .onListingPage(DEFAULT_ON_LISTING_PAGE)
                .onSearchForm(DEFAULT_ON_SEARCH_FORM)
                .onMap(DEFAULT_ON_MAP)
                .onAdvancedSearchForm(DEFAULT_ON_ADVANCED_SEARCH_FORM)
                .options(DEFAULT_OPTIONS)
                .searchOptions(DEFAULT_SEARCH_OPTIONS)
                .qlString(new QlString().languageCode("en").context("dl-content-field").name("dl-content-field-#" + DEFAULT_NAME).value(DEFAULT_NAME).status(0));
    }

    public static DlContentField createField(DlContentField.Type type, String name, Integer orderNum,
                                             Set<DlCategory> dlCategories) {
        return new DlContentField()
                .coreField(Boolean.FALSE)
                .orderNum(orderNum)
                .name(name)
                .slug(SlugUtil.getFileNameSlug(name))
                .description("Some description")
                .type(type)
                .required(Boolean.TRUE)
                .hasConfiguration(Boolean.FALSE)
                .hasSearchConfiguration(Boolean.FALSE)
                .canBeOrdered(Boolean.FALSE)
                .hideName(Boolean.FALSE)
                .onExcerptPage(Boolean.FALSE)
                .onListingPage(Boolean.FALSE)
                .onSearchForm(Boolean.FALSE)
                .onAdvancedSearchForm(Boolean.FALSE)
                .onMap(Boolean.FALSE)
                .options("")
                .searchOptions("")
                .dlCategories(dlCategories);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlContentFieldResource dlContentFieldResource = new DlContentFieldResource(dlContentFieldService);
        this.restDlContentFieldMockMvc = MockMvcBuilders.standaloneSetup(dlContentFieldResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Before
    public void initTest() {
        dlContentFieldRepository.deleteAll();
        dlContentField = createEntity();

        dlCategoryRepository.deleteAllByParent(null);
        dlCategory = DlCategoryResourceIntTest.createEntity();
        dlCategory2 = DlCategoryResourceIntTest.createEntity2();
        dlContentFieldGroup = DlContentFieldGroupResourceTest.createEntity();
    }

    @Test
    @Transactional
    public void createDlContentField() throws Exception {
        int databaseSizeBeforeCreate = dlContentFieldRepository.findAll().size();

        dlContentFieldGroupRepository.saveAndFlush(dlContentFieldGroup);
        DlCategory dlCategorySaved = dlCategoryRepository.saveAndFlush(dlCategory);
        dlContentField.setDlCategories(new HashSet<DlCategory>() {
            {
                add(dlCategorySaved);
            }
        });
        dlContentField.setDlContentFieldGroup(dlContentFieldGroup);

        // Create the DlContentField
        DlContentFieldDTO dlContentFieldDTO = dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(dlContentField, null);

        restDlContentFieldMockMvc.perform(post(RESOURCE_API_ADMIN_DL_CONTENT_FIELDS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlContentFieldDTO)))
                .andExpect(status().isCreated());

        // Validate the DlContentField in the database
        List<DlContentField> dlContentFields = dlContentFieldRepository.findAll();
        assertThat(dlContentFields).hasSize(databaseSizeBeforeCreate + 1);

        DlContentField dlContentFieldSaved = dlContentFields.get(dlContentFields.size() - 1);
        assertThat(dlContentFieldSaved.getCoreField()).isEqualTo(DEFAULT_CORE_FIELD);
        assertThat(dlContentFieldSaved.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(dlContentFieldSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlContentFieldSaved.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(dlContentFieldSaved.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(dlContentFieldSaved.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(dlContentFieldSaved.getIconImage()).isEqualTo(DEFAULT_ICON_IMAGE);
        assertThat(dlContentFieldSaved.getRequired()).isEqualTo(DEFAULT_REQUIRED);
        assertThat(dlContentFieldSaved.getHasConfiguration()).isEqualTo(DEFAULT_HAS_CONFIGURATION);
        assertThat(dlContentFieldSaved.getHasSearchConfiguration()).isEqualTo(DEFAULT_HAS_SEARCH_CONFIGURATION);
        assertThat(dlContentFieldSaved.getCanBeOrdered()).isEqualTo(DEFAULT_CAN_BE_ORDERED);
        assertThat(dlContentFieldSaved.getHideName()).isEqualTo(DEFAULT_HIDE_NAME);
        assertThat(dlContentFieldSaved.getOnExcerptPage()).isEqualTo(DEFAULT_ON_EXCERPT_PAGE);
        assertThat(dlContentFieldSaved.getOnListingPage()).isEqualTo(DEFAULT_ON_LISTING_PAGE);
        assertThat(dlContentFieldSaved.getOnSearchForm()).isEqualTo(DEFAULT_ON_SEARCH_FORM);
        assertThat(dlContentFieldSaved.getOnMap()).isEqualTo(DEFAULT_ON_MAP);
        assertThat(dlContentFieldSaved.getOnAdvancedSearchForm()).isEqualTo(DEFAULT_ON_ADVANCED_SEARCH_FORM);
        assertThat(dlContentFieldSaved.getDlCategories().stream().findFirst().orElse(null).getId()).isEqualTo(this.dlCategory.getId());
        assertThat(dlContentFieldSaved.getOptions()).isEqualTo(DEFAULT_OPTIONS);
        assertThat(dlContentFieldSaved.getSearchOptions()).isEqualTo(DEFAULT_SEARCH_OPTIONS);

        // verify also that qlString is created
        assertThat(dlContentFieldSaved.getQlString().getValue()).isEqualTo(DEFAULT_NAME);

        // verify that also group is saved
        assertThat(dlContentFieldSaved.getDlContentFieldGroup().getId()).isEqualTo(dlContentFieldGroup.getId());
    }

    @Test
    @Transactional
    public void createDlContentFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dlContentFieldRepository.findAll().size();

        // Create the DlContentField with an existing ID
        DlContentField existingDlContentField = new DlContentField();
        existingDlContentField.setId(1L);
        DlContentFieldDTO existingDlContentFieldDTO = dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(existingDlContentField, null);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDlContentFieldMockMvc.perform(post(RESOURCE_API_ADMIN_DL_CONTENT_FIELDS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingDlContentFieldDTO)))
                .andExpect(status().isBadRequest());

        // Validate the DlContentField in the database
        List<DlContentField> dlContentFields = dlContentFieldRepository.findAll();
        assertThat(dlContentFields).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getDlContentField() throws Exception {
        // Initialize the database
        DlCategory dlCategorySaved = dlCategoryRepository.saveAndFlush(dlCategory);
        dlContentField.setDlCategories(new HashSet<DlCategory>() {
            {
                add(dlCategorySaved);
            }
        });
        dlContentFieldGroupRepository.saveAndFlush(dlContentFieldGroup);
        dlContentField.setDlContentFieldGroup(dlContentFieldGroup);
        dlContentFieldRepository.saveAndFlush(dlContentField);

        // Get the DlContentField
        restDlContentFieldMockMvc.perform(get(RESOURCE_API_ADMIN_DL_CONTENT_FIELDS + "/{id}",
                dlContentField.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(dlContentField.getId().intValue()))
                .andExpect(jsonPath("$.coreField").value(dlContentField.getCoreField()))
                .andExpect(jsonPath("$.orderNum").value(dlContentField.getOrderNum()))
                .andExpect(jsonPath("$.name").value(dlContentField.getName()))
                .andExpect(jsonPath("$.slug").value(dlContentField.getSlug()))
                .andExpect(jsonPath("$.description").value(dlContentField.getDescription()))
                .andExpect(jsonPath("$.type").value(dlContentField.getType().toString()))
                .andExpect(jsonPath("$.iconImage").value(dlContentField.getIconImage()))
                .andExpect(jsonPath("$.required").value(dlContentField.getRequired()))
                .andExpect(jsonPath("$.hasConfiguration").value(dlContentField.getHasConfiguration()))
                .andExpect(jsonPath("$.hasSearchConfiguration").value(dlContentField.getHasSearchConfiguration()))
                .andExpect(jsonPath("$.canBeOrdered").value(dlContentField.getCanBeOrdered()))
                .andExpect(jsonPath("$.hideName").value(dlContentField.getHideName()))
                .andExpect(jsonPath("$.onExcerptPage").value(dlContentField.getOnExcerptPage()))
                .andExpect(jsonPath("$.onListingPage").value(dlContentField.getOnListingPage()))
                .andExpect(jsonPath("$.onSearchForm").value(dlContentField.getOnSearchForm()))
                .andExpect(jsonPath("$.onMap").value(dlContentField.getOnMap()))
                .andExpect(jsonPath("$.onAdvancedSearchForm").value(dlContentField.getOnAdvancedSearchForm()))
                .andExpect(jsonPath("$.dlCategories.[0].id").value(dlContentField.getDlCategories().stream().findFirst().orElse(null).getId()))
                .andExpect(jsonPath("$.options").value(dlContentField.getOptions()))
                .andExpect(jsonPath("$.searchOptions").value(dlContentField.getSearchOptions()))
                .andExpect(jsonPath("$.dlContentFieldGroup.id").value(dlContentFieldGroup.getId()))
                .andExpect(jsonPath("$.dlContentFieldGroup.name").value(dlContentFieldGroup.getName()))
                .andExpect(jsonPath("$.dlContentFieldGroup.slug").value(dlContentFieldGroup.getSlug()))
        ;
    }

    @Test
    @Transactional
    public void getNonExistingDlContentField() throws Exception {
        // Get non existing DlContentField
        restDlContentFieldMockMvc.perform(get(RESOURCE_API_ADMIN_DL_CONTENT_FIELDS + "/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDlContentField() throws Exception {
        // Initialize the database
        DlCategory dlCategorySaved = dlCategoryRepository.saveAndFlush(dlCategory);
        DlCategory dlCategory2Saved = dlCategoryRepository.saveAndFlush(dlCategory2);
        dlContentField.setDlCategories(new HashSet<DlCategory>() {
            {
                add(dlCategorySaved);
            }
        });
        dlContentFieldRepository.saveAndFlush(dlContentField);
        int databaseSizeBeforeUpdate = dlContentFieldRepository.findAll().size();

        // Update the DlContentField
        DlContentField updatedDlContentField = dlContentFieldRepository.findOne(this.dlContentField.getId());
        updatedDlContentField
                .coreField(UPDATED_CORE_FIELD)
                .orderNum(UPDATED_ORDER_NUM)
                .name(UPDATED_NAME)
                .slug(UPDATED_SLUG)
                .description(UPDATED_DESCRIPTION)
                .type(UPDATED_TYPE)
                .iconImage(UPDATED_ICON_IMAGE)
                .required(UPDATED_REQUIRED)
                .hasConfiguration(UPDATED_HAS_CONFIGURATION)
                .hasSearchConfiguration(UPDATED_HAS_SEARCH_CONFIGURATION)
                .canBeOrdered(UPDATED_CAN_BE_ORDERED)
                .hideName(UPDATED_HIDE_NAME)
                .onExcerptPage(UPDATED_ON_EXCERPT_PAGE)
                .onListingPage(UPDATED_ON_LISTING_PAGE)
                .onSearchForm(UPDATED_ON_SEARCH_FORM)
                .onMap(UPDATED_ON_MAP)
                .onAdvancedSearchForm(UPDATED_ON_ADVANCED_SEARCH_FORM)
                .dlCategories(new HashSet<DlCategory>() {{
                    add(dlCategorySaved);
                    add(dlCategory2Saved);
                }})
                .options(UPDATED_OPTIONS)
                .searchOptions(UPDATED_SEARCH_OPTIONS);

        dlContentFieldGroupRepository.saveAndFlush(dlContentFieldGroup);
        updatedDlContentField.setDlContentFieldGroup(dlContentFieldGroup);

        DlContentFieldDTO updateContentFieldDTO = dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(updatedDlContentField, null);

        restDlContentFieldMockMvc.perform(put(RESOURCE_API_ADMIN_DL_CONTENT_FIELDS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updateContentFieldDTO)))
                .andExpect(status().isOk());

        // Validate the DlContentField in the database
        List<DlContentField> all = dlContentFieldRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate);

        DlContentField dlContentFieldSaved = dlContentFieldRepository.findOne(updatedDlContentField.getId());
        assertThat(dlContentFieldSaved.getCoreField()).isEqualTo(UPDATED_CORE_FIELD);
        assertThat(dlContentFieldSaved.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(dlContentFieldSaved.getName()).isEqualTo(UPDATED_NAME);
        assertThat(dlContentFieldSaved.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(dlContentFieldSaved.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(dlContentFieldSaved.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(dlContentFieldSaved.getIconImage()).isEqualTo(UPDATED_ICON_IMAGE);
        assertThat(dlContentFieldSaved.getRequired()).isEqualTo(UPDATED_REQUIRED);
        assertThat(dlContentFieldSaved.getHasConfiguration()).isEqualTo(UPDATED_HAS_CONFIGURATION);
        assertThat(dlContentFieldSaved.getHasSearchConfiguration()).isEqualTo(UPDATED_HAS_SEARCH_CONFIGURATION);
        assertThat(dlContentFieldSaved.getCanBeOrdered()).isEqualTo(UPDATED_CAN_BE_ORDERED);
        assertThat(dlContentFieldSaved.getHideName()).isEqualTo(UPDATED_HIDE_NAME);
        assertThat(dlContentFieldSaved.getOnExcerptPage()).isEqualTo(UPDATED_ON_EXCERPT_PAGE);
        assertThat(dlContentFieldSaved.getOnListingPage()).isEqualTo(UPDATED_ON_LISTING_PAGE);
        assertThat(dlContentFieldSaved.getOnSearchForm()).isEqualTo(UPDATED_ON_SEARCH_FORM);
        assertThat(dlContentFieldSaved.getOnMap()).isEqualTo(UPDATED_ON_MAP);
        assertThat(dlContentFieldSaved.getOnAdvancedSearchForm()).isEqualTo(UPDATED_ON_ADVANCED_SEARCH_FORM);
        assertThat(dlContentFieldSaved.getDlCategories().size()).isEqualTo(2);
        assertThat(dlContentFieldSaved.getOptions()).isEqualTo(UPDATED_OPTIONS);
        assertThat(dlContentFieldSaved.getSearchOptions()).isEqualTo(UPDATED_SEARCH_OPTIONS);

        assertThat(dlContentFieldSaved.getDlContentFieldGroup().getId()).isEqualTo(dlContentFieldGroup.getId());

        // verify also that qlString is created
        assertThat(dlContentFieldSaved.getQlString().getValue()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDlContentField() throws Exception {
        int databaseSizeBeforeUpdate = dlContentFieldRepository.findAll().size();

        // Create the DTO
        DlContentFieldDTO dlContentFieldDTO = dlContentFieldMapper.dlContentFieldToDlContentFieldDTO(dlContentField, null);

        // If the entity does not have an ID, it will be created instead of just being updated
        restDlContentFieldMockMvc.perform(put(RESOURCE_API_ADMIN_DL_CONTENT_FIELDS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlContentFieldDTO)))
                .andExpect(status().isCreated());

        // Validate the DlContentField in the database
        List<DlContentField> all = dlContentFieldRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDlContentField() throws Exception {
        // Initialize the database
        dlContentFieldRepository.saveAndFlush(dlContentField);
        int databaseSizeBeforeDelete = dlContentFieldRepository.findAll().size();

        // Delete the DlContentField
        restDlContentFieldMockMvc.perform(delete(RESOURCE_API_ADMIN_DL_CONTENT_FIELDS + "/{id}",
                dlContentField.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DlContentField> all = dlContentFieldRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeDelete - 1);
    }

}