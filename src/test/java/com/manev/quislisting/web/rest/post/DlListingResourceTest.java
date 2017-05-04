package com.manev.quislisting.web.rest.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.PostMeta;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.builder.DlListingBuilder;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingField;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.util.SlugUtil;
import com.manev.quislisting.web.rest.TestUtil;
import com.manev.quislisting.web.rest.taxonomy.DlCategoryResourceIntTest;
import com.manev.quislisting.web.rest.taxonomy.DlLocationResourceIntTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.*;

import static com.manev.quislisting.domain.post.PostMeta.*;
import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_DL_LISTINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlListingResourceTest {

    private static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    private static final String DEFAULT_CONTENT = "DEFAULT_CONTENT";
    private static final String DEFAULT_NAME = "default_title";
    private static final String DEFAULT_LANGUAGE_CODE = "en";
    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final ZonedDateTime DEFAULT_MODIFIED = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final DlListing.Status DEFAULT_STATUS = DlListing.Status.UNFINISHED;

    private static final String META_VALUE_EXPIRATION_DATE = "1486908703";

    private static final String META_VALUE_ATTACHED_IMAGE_00 = "232";
    private static final String META_VALUE_ATTACHED_IMAGE_01 = "233";
    private static final String META_VALUE_ATTACHED_IMAGE_02 = "234";
    private static final String META_VALUE_ATTACHED_IMAGE_03 = "235";


    private static final String META_VALUE_CONTENT_FIELD_20 = "24";
    private static final String META_VALUE_CONTENT_FIELD_21 = "175";
    private static final String META_VALUE_CONTENT_FIELD_22 = "55";
    private static final String META_VALUE_ATTACHED_IMAGE_AS_LOGO = "233";
    private static final String META_VALUE_THUMBNAIL_ID = "233";
    private static final String META_VALUE_LISTING_STATUS = "active";
    private static final String META_VALUE_POST_VIEWS_COUNT = "0";
    private static final String META_VALUE_LOCATION_ID = "233";
    private static final String META_VALUE_ADDRESS_LINE_1 = "Times Square, New York, New York";
    private static final String META_VALUE_ADDRESS_LINE_2 = "";
    private static final String META_VALUE_MAP_COORDS_1 = "42.65418440000001";
    private static final String META_VALUE_MAP_COORDS_2 = "23.371921499999985";
    private static final String META_KEY_MAP_ZOOM = "_map_zoom";
    private static final String META_VALUE_CLICKS_DATA = "{\"2017-01\", \"2\"}";
    private static final String META_VALUE_TOTAL_CLICKS = "2";

    @Autowired
    private DlListingService dlListingService;

    @Autowired
    private DlListingRepository dlListingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private DlCategoryRepository dlCategoryRepository;

    @Autowired
    private DlLocationRepository dlLocationRepository;

    @Autowired
    private DlContentFieldRepository dlContentFieldRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restDlListingMockMvc;

    private DlListing dlListing;
    private DlCategory dlCategory;
    private DlLocation dlLocation;
    private User user;

    public static DlListing createEntity(EntityManager em) {
        return DlListingBuilder.aDlListing()
                .withTitle(DEFAULT_TITLE)
                .withContent(DEFAULT_CONTENT)
                .withName(DEFAULT_NAME)
                .withCreated(DEFAULT_CREATED)
                .withModified(DEFAULT_MODIFIED)
                .withStatus(DEFAULT_STATUS)
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode("en")
                        .withTranslationGroup(new TranslationGroup())
                        .build())
                .build();
    }

    public static Set<PostMeta> createPostMeta(AbstractPost abstractPost) {
        Set<PostMeta> postMetas = new HashSet<>();
        postMetas.add(new PostMeta(abstractPost, META_KEY_EXPIRATION_DATE, META_VALUE_EXPIRATION_DATE));
        postMetas.add(new PostMeta(abstractPost, META_KEY_ATTACHED_IMAGE, META_VALUE_ATTACHED_IMAGE_00));
        postMetas.add(new PostMeta(abstractPost, META_KEY_ATTACHED_IMAGE, META_VALUE_ATTACHED_IMAGE_01));
        postMetas.add(new PostMeta(abstractPost, META_KEY_ATTACHED_IMAGE, META_VALUE_ATTACHED_IMAGE_02));
        postMetas.add(new PostMeta(abstractPost, META_KEY_ATTACHED_IMAGE, META_VALUE_ATTACHED_IMAGE_03));
        postMetas.add(new PostMeta(abstractPost, META_KEY_CONTENT_FIELD_20, META_VALUE_CONTENT_FIELD_20));
        postMetas.add(new PostMeta(abstractPost, META_KEY_CONTENT_FIELD_21, META_VALUE_CONTENT_FIELD_21));
        postMetas.add(new PostMeta(abstractPost, META_KEY_CONTENT_FIELD_22, META_VALUE_CONTENT_FIELD_22));
        postMetas.add(new PostMeta(abstractPost, META_KEY_ATTACHED_IMAGE_AS_LOGO, META_VALUE_ATTACHED_IMAGE_AS_LOGO));
        postMetas.add(new PostMeta(abstractPost, META_KEY_THUMBNAIL_ID, META_VALUE_THUMBNAIL_ID));
        postMetas.add(new PostMeta(abstractPost, META_KEY_LISTING_STATUS, META_VALUE_LISTING_STATUS));
        postMetas.add(new PostMeta(abstractPost, META_KEY_POST_VIEWS_COUNT, META_VALUE_POST_VIEWS_COUNT));
        postMetas.add(new PostMeta(abstractPost, META_KEY_LOCATION_ID, META_VALUE_LOCATION_ID));
        postMetas.add(new PostMeta(abstractPost, META_KEY_ADDRESS_LINE_1, META_VALUE_ADDRESS_LINE_1));
        postMetas.add(new PostMeta(abstractPost, META_KEY_ADDRESS_LINE_2, META_VALUE_ADDRESS_LINE_2));
        postMetas.add(new PostMeta(abstractPost, META_KEY_MAP_COORDS_1, META_VALUE_MAP_COORDS_1));
        postMetas.add(new PostMeta(abstractPost, META_KEY_MAP_COORDS_2, META_VALUE_MAP_COORDS_2));
        postMetas.add(new PostMeta(abstractPost, META_KEY_MAP_ZOOM, META_VALUE_MAP_ZOOM));
        postMetas.add(new PostMeta(abstractPost, META_KEY_CLICKS_DATA, META_VALUE_CLICKS_DATA));
        postMetas.add(new PostMeta(abstractPost, META_KEY_TOTAL_CLICKS, META_VALUE_TOTAL_CLICKS));

        return postMetas;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlListingResource dlListingResource = new DlListingResource(dlListingService);
        this.restDlListingMockMvc = MockMvcBuilders.standaloneSetup(dlListingResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
        setupSecurityContext();
    }

    @Before
    public void initTest() {
        dlListingRepository.deleteAll();

        // setup user in security
        user = userRepository.findOneByLogin("admin").get();

        // setup dlListing
        dlListing = createEntity(em);
        dlListing.setPostMeta(createPostMeta(dlListing));

        // setup dl category availability
        dlCategoryRepository.deleteAllByParent(null);
        dlCategory = DlCategoryResourceIntTest.createEntity();

        // setup dl location availability
        dlLocationRepository.deleteAllByParent(null);
        dlLocation = DlLocationResourceIntTest.createEntity();


    }

    private List<DlContentField> createContentFieldsForCategory(DlCategory dlCategory) {
        List<DlContentField> dlContentFields = new ArrayList<>();

        Set<DlCategory> dlCategories = new HashSet<>();
        dlCategories.add(dlCategory);

        dlContentFields.add(createNumberContentField("Height", 1, dlCategories));
        dlContentFields.add(createNumberContentField("Phone", 2, dlCategories));

        return dlContentFields;
    }

    private DlContentField createNumberContentField(String name, Integer orderNum, Set<DlCategory> dlCategories) {
        DlContentField numberDlContentField = new DlContentField();
        numberDlContentField.setCoreField(Boolean.FALSE);
        numberDlContentField.setOrderNum(orderNum);
        numberDlContentField.setName(name);
        numberDlContentField.setSlug(SlugUtil.getFileNameSlug(name));
        numberDlContentField.setDescription("Some description");
        numberDlContentField.setType("number");
        numberDlContentField.setRequired(Boolean.TRUE);
        numberDlContentField.hasConfiguration(Boolean.FALSE);
        numberDlContentField.hasSearchConfiguration(Boolean.FALSE);
        numberDlContentField.canBeOrdered(Boolean.FALSE);
        numberDlContentField.hideName(Boolean.FALSE);
        numberDlContentField.onExcerptPage(Boolean.FALSE);
        numberDlContentField.onListingPage(Boolean.FALSE);
        numberDlContentField.onSearchForm(Boolean.FALSE);
        numberDlContentField.onAdvancedSearchForm(Boolean.FALSE);
        numberDlContentField.onMap(Boolean.FALSE);
        numberDlContentField.options("");
        numberDlContentField.searchOptions("");
        numberDlContentField.setDlCategories(dlCategories);
        return numberDlContentField;
    }

    @Test
    @Transactional
    public void getAllDlListings() throws Exception {
        // Initialize the database
        dlCategoryRepository.saveAndFlush(dlCategory);
        dlListing.setUser(user);
        dlListing.setDlCategories(new HashSet<DlCategory>() {{
            add(dlCategory);
        }});
        dlListingRepository.saveAndFlush(dlListing);

        // Get all the navMenus
        ResultActions resultActions = restDlListingMockMvc.perform(get(RESOURCE_API_ADMIN_DL_LISTINGS + "?sort=id,desc&languageCode=en"));
        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dlListing.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].dlCategories.[*].term.name").value(hasItem(DlCategoryResourceIntTest.DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].dlCategories.[*].term.slug").value(hasItem(DlCategoryResourceIntTest.DEFAULT_SLUG)));
    }

    @Test
    @Transactional
    public void createDlListing() throws Exception {
        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();

        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setTitle(DEFAULT_TITLE);
        dlListingDTO.setLanguageCode(DEFAULT_LANGUAGE_CODE);

        restDlListingMockMvc.perform(post(RESOURCE_API_ADMIN_DL_LISTINGS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlListingDTO)))
                .andExpect(status().isCreated());

        // Validate the DlListing in the database
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        DlListing dlListingSaved = dlListingList.get(dlListingList.size() - 1);
        assertThat(dlListingSaved.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(dlListingSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlListingSaved.getStatus()).isEqualTo(DlListing.Status.UNFINISHED);
        assertThat(dlListingSaved.getTranslation().getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
    }

    @Test
    @Transactional
    public void updateDlListingIntoPublishStatus() throws Exception {
        // initialize categories and location
        dlCategoryRepository.saveAndFlush(dlCategory);
        dlLocationRepository.saveAndFlush(dlLocation);

        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();

        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setTitle(DEFAULT_TITLE);
        dlListingDTO.setLanguageCode(DEFAULT_LANGUAGE_CODE);

        MvcResult mvcResult = restDlListingMockMvc.perform(post(RESOURCE_API_ADMIN_DL_LISTINGS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlListingDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        DlListingDTO createdDlListingDTO = new ObjectMapper().readValue(contentAsString,
                DlListingDTO.class);

        // making updates to the dlListing so at the end object is updated and published
        createdDlListingDTO.setContent(DEFAULT_CONTENT);

        // set category
        DlCategoryDTO dlCategoryDTO = new DlCategoryDTO();
        dlCategoryDTO.setId(dlCategory.getId());
        createdDlListingDTO.setDlCategories(Collections.singletonList(dlCategoryDTO));

        // set location
        DlLocationDTO dlLocationDTO = new DlLocationDTO();
        dlLocationDTO.setId(dlLocation.getId());
        createdDlListingDTO.setDlLocations(Collections.singletonList(dlLocationDTO));

        // set content fields
        List<DlContentField> contentFieldsForCategory = createContentFieldsForCategory(dlCategory);
        List<DlContentField> savedDlContentFieldsForCategory = dlContentFieldRepository.save(contentFieldsForCategory);

        // create logic to save values in metadata of the dlListing
        DlListingField dlListingFieldHeight = new DlListingField(SlugUtil.metaContentFieldId(findDlContentFieldByName("Height",
                savedDlContentFieldsForCategory).getId()), "180");
        createdDlListingDTO.addDlListingField(dlListingFieldHeight);
        DlListingField dlListingFieldPhone = new DlListingField(SlugUtil.metaContentFieldId(findDlContentFieldByName("Phone",
                savedDlContentFieldsForCategory).getId()), "+123 456 555");
        createdDlListingDTO.addDlListingField(dlListingFieldPhone);

        // set attachment is not part of this test

        // make the put request for updating the listing
        restDlListingMockMvc.perform(put(RESOURCE_API_ADMIN_DL_LISTINGS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdDlListingDTO)))
                .andExpect(status().isOk());

        // make da asserts
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        DlListing dlListingSaved = dlListingList.get(dlListingList.size() - 1);
        assertThat(dlListingSaved.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(dlListingSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlListingSaved.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(dlListingSaved.getStatus()).isEqualTo(DlListing.Status.UNFINISHED);
        assertThat(dlListingSaved.getTranslation().getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
        assertThat(dlListingSaved.getDlCategories().iterator().next().getId()).isEqualTo(dlCategory.getId());
        assertThat(dlListingSaved.getDlLocations().iterator().next().getId()).isEqualTo(dlLocation.getId());

        assertThat(dlListingSaved.getPostMetaValue(dlListingFieldHeight.getFieldId())).isEqualTo("180");
        assertThat(dlListingSaved.getPostMetaValue(dlListingFieldPhone.getFieldId())).isEqualTo("+123 456 555");
    }

    private DlContentField findDlContentFieldByName(String name, List<DlContentField> dlContentFields) {
        for (DlContentField dlContentField : dlContentFields) {
            if (dlContentField.getName().equals(name)) {
                return dlContentField;
            }
        }
        return null;
    }

    @Test
    @Transactional
    public void getActiveLanguages() throws Exception {
        // Initialize the database
        dlListing.setUser(user);
        dlListingRepository.saveAndFlush(dlListing);

        Language lanEn = new Language().code("en").active(true).englishName("English");
        Language lanBg = new Language().code("bg").active(true).englishName("Bulgarian");
        Language lanRo = new Language().code("ro").active(true).englishName("Romanian");
        Language lanRu = new Language().code("ru").active(true).englishName("Russian");
        languageRepository.saveAndFlush(lanEn);
        languageRepository.saveAndFlush(lanBg);
        languageRepository.saveAndFlush(lanRo);
        languageRepository.saveAndFlush(lanRu);

        // Get active languages
        restDlListingMockMvc.perform(get(RESOURCE_API_ADMIN_DL_LISTINGS + "/active-languages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].code").value(hasItem("en")))
                .andExpect(jsonPath("$.[*].englishName").value(hasItem("English")))
                .andExpect(jsonPath("$.[*].count").value(hasItem(1)));
    }

    private void setupSecurityContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("admin",
                "admin",
                grantedAuthorities);
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
    }
}
