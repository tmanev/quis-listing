package com.manev.quislisting.web.rest.post;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.PageRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.service.post.QlPageService;
import com.manev.quislisting.service.post.dto.QlPageDTO;
import com.manev.quislisting.service.post.mapper.QlPageMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_QL_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class QlPageResourceTest {

    private static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    private static final String DEFAULT_CONTENT = "DEFAULT_CONTENT";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final ZonedDateTime DEFAULT_MODIFIED = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final QlPage.Status DEFAULT_STATUS = QlPage.Status.PUBLISH;

    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String UPDATED_TITLE = "UPDATED_TITLE";
    private static final QlPage.Status UPDATED_STATUS = QlPage.Status.DRAFT;
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final ZonedDateTime UPDATED_MODIFIED = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final String UPDATED_CONTENT = "UPDATED_CONTENT";


    private static final String DEFAULT_TITLE2 = "DEFAULT_TITLE";
    private static final String DEFAULT_CONTENT2 = "DEFAULT_CONTENT";
    private static final String DEFAULT_NAME2 = "DEFAULT_NAME";
    private static final ZonedDateTime DEFAULT_CREATED2 = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final ZonedDateTime DEFAULT_MODIFIED2 = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final String DEFAULT_STATUS2 = "DEFAULT_STATUS";

    private static final String META_VALUE_EXPIRATION_DATE = "1486908703";


    private static final String META_VALUE_CONTENT_FIELD_20 = "24";
    private static final String META_VALUE_CONTENT_FIELD_21 = "175";
    private static final String META_VALUE_CONTENT_FIELD_22 = "55";
    private static final String META_VALUE_THUMBNAIL_ID = "233";
    private static final String META_VALUE_LISTING_STATUS = "active";
    private static final String META_VALUE_LOCATION_ID = "233";
    private static final String META_VALUE_ADDRESS_LINE_1 = "Times Square, New York, New York";
    private static final String META_VALUE_ADDRESS_LINE_2 = "";
    private static final String META_VALUE_MAP_COORDS_1 = "42.65418440000001";
    private static final String META_VALUE_MAP_COORDS_2 = "23.371921499999985";
    private static final String META_KEY_MAP_ZOOM = "_map_zoom";
    private static final String META_VALUE_CLICKS_DATA = "{\"2017-01\", \"2\"}";
    private static final String META_VALUE_TOTAL_CLICKS = "2";

    @Autowired
    private QlPageService qlPageService;

    @Autowired
    private QlPageMapper qlPageMapper;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restQlPageMockMvc;

    private QlPage qlPage;
    private User user;

    public static QlPage createEntity(EntityManager em) {

        QlPage qlPage = new QlPage();
        qlPage.setContent(DEFAULT_CONTENT);
        qlPage.setCreated(DEFAULT_CREATED);
        qlPage.setModified(DEFAULT_MODIFIED);
        qlPage.setName(DEFAULT_NAME);
        qlPage.setStatus(DEFAULT_STATUS);
        qlPage.setTitle(DEFAULT_TITLE);
        qlPage.setTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode("en")
                        .withTranslationGroup(new TranslationGroup())
                        .build());

        return qlPage;
    }

    public static QlPage createEntity2() {
        QlPage qlPage = new QlPage();
        qlPage.setContent(DEFAULT_CONTENT);
        qlPage.setCreated(DEFAULT_CREATED);
        qlPage.setModified(DEFAULT_MODIFIED);
        qlPage.setName(DEFAULT_NAME);
        qlPage.setStatus(DEFAULT_STATUS);
        qlPage.setTitle(DEFAULT_TITLE);
        qlPage.setTranslation(TranslationBuilder.aTranslation()
                .withLanguageCode("bg")
                .withTranslationGroup(new TranslationGroup())
                .build());
        return qlPage;
    }


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QlPageResource qlPageResource = new QlPageResource(qlPageService);
        this.restQlPageMockMvc = MockMvcBuilders.standaloneSetup(qlPageResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
        setupSecurityContext();
    }

    @Before
    public void initTest() {
        pageRepository.deleteAll();
        user = userRepository.findOneByLogin("admin").get();
        qlPage = createEntity(em);

    }

    @Test
    @Transactional
    public void getAllQlPages() throws Exception {
        // Initialize the database
       // dlCategoryRepository.saveAndFlush(dlCategory);
        qlPage.setUser(user);
      //  dlListing.setDlCategories(new HashSet<DlCategory>() {{
      //      add(dlCategory);
      //  }});
        pageRepository.saveAndFlush(qlPage);

        // Get all the navMenus
        ResultActions resultActions = restQlPageMockMvc.perform(get(RESOURCE_API_ADMIN_QL_PAGES + "?sort=id,desc&languageCode=en"));
        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(qlPage.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
            //    .andExpect(jsonPath("$.[*].dlCategories.[*].term.name").value(hasItem(DlCategoryResourceIntTest.DEFAULT_NAME)))
            //    .andExpect(jsonPath("$.[*].dlCategories.[*].term.slug").value(hasItem(DlCategoryResourceIntTest.DEFAULT_SLUG)));
    }

    @Test
    @Transactional
    public void getActiveLanguages() throws Exception {
        // Initialize the database
        qlPage.setUser(user);
        pageRepository.saveAndFlush(qlPage);

        List<Language> activeLanguages = createActiveLanguages();
        for (Language activeLanguage : activeLanguages) {
            languageRepository.saveAndFlush(activeLanguage);
        }

        // Get active languages
        restQlPageMockMvc.perform(get(RESOURCE_API_ADMIN_QL_PAGES + "/active-languages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].code").value(hasItem("en")))
                .andExpect(jsonPath("$.[*].englishName").value(hasItem("English")))
                .andExpect(jsonPath("$.[*].count").value(hasItem(1)));
    }

    private List<Language> createActiveLanguages() {
        List<Language> result = new ArrayList<>();

        result.add(new Language().code("en").active(true).englishName("English"));
        result.add(new Language().code("bg").active(true).englishName("Bulgarian"));
        result.add(new Language().code("ro").active(true).englishName("Romanian"));
        result.add(new Language().code("ru").active(true).englishName("Russian"));

        return result;
    }

    @Test
    @Transactional
    public void createQlPage() throws Exception {
        int databaseSizeBeforeCreate = pageRepository.findAll().size();

        // Create the DlCategory
        QlPageDTO qlPageDTO = qlPageMapper.pageToPageDTO(qlPage, createActiveLanguages());

        restQlPageMockMvc.perform(post(RESOURCE_API_ADMIN_QL_PAGES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qlPageDTO)))
                .andExpect(status().isCreated());

        // Validate the DlCategory in the database
        List<QlPage> qlPageTrList = pageRepository.findAll();
        assertThat(qlPageTrList).hasSize(databaseSizeBeforeCreate + 1);
        QlPage qlPageSaved = qlPageTrList.get(qlPageTrList.size() - 1);
        assertThat(qlPageSaved.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(qlPageSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(qlPageSaved.getContent()).isEqualTo(DEFAULT_CONTENT);
//        assertThat(qlPageSaved.getModified()).isEqualTo(DEFAULT_MODIFIED);
//        assertThat(qlPageSaved.getCreated()).isEqualTo(DEFAULT_CREATED);
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
    @Test
    @Transactional
    public void getQlPage() throws Exception {
        // Initialize the database
        qlPage.setUser(user);
        pageRepository.saveAndFlush(qlPage);

        // Get the DlCategory
        restQlPageMockMvc.perform(get(RESOURCE_API_ADMIN_QL_PAGES + "/{id}", qlPage.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(qlPage.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
//                .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
//                .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()))
                ;
    }

    @Test
    @Transactional
    public void getNonExistingQlPage() throws Exception {
        // Get the dlCategory
        restQlPageMockMvc.perform(get(RESOURCE_API_ADMIN_QL_PAGES + "/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQlPage() throws Exception {
        // Initialize the database
        qlPage.setUser(user);
        pageRepository.saveAndFlush(qlPage);
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Update the DlCategory
        QlPage updatedQlPage = pageRepository.findOne(this.qlPage.getId());
        updatedQlPage.setName(UPDATED_NAME);
        updatedQlPage.setTitle(UPDATED_TITLE);
        updatedQlPage.setContent(UPDATED_CONTENT);
        updatedQlPage.setStatus(UPDATED_STATUS);
        updatedQlPage.setCreated(UPDATED_CREATED);
        updatedQlPage.setModified(UPDATED_MODIFIED);

        QlPageDTO qlPageDTO = qlPageMapper.pageToPageDTO(updatedQlPage, createActiveLanguages());

        restQlPageMockMvc.perform(put(RESOURCE_API_ADMIN_QL_PAGES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qlPageDTO)))
                .andExpect(status().isOk());

        // Validate the DlCategory in the database
        List<QlPage> qlPagesList = pageRepository.findAll();
        assertThat(qlPagesList).hasSize(databaseSizeBeforeUpdate);
        QlPage testQlPage = pageRepository.findOne(updatedQlPage.getId());
        assertThat(testQlPage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQlPage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQlPage.getTitle()).isEqualTo(UPDATED_TITLE);
//        assertThat(testQlPage.getContent()).isEqualTo(UPDATED_CONTENT);
//        assertThat(testQlPage.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingQlPage() throws Exception {
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Create the QlPage
        QlPageDTO qlPageDTO = qlPageMapper.pageToPageDTO(qlPage, createActiveLanguages());

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQlPageMockMvc.perform(put(RESOURCE_API_ADMIN_QL_PAGES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qlPageDTO)))
                .andExpect(status().isCreated());

        // Validate the QlPage in the database
        List<QlPage> qlPageList = pageRepository.findAll();
        assertThat(qlPageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQlPage() throws Exception {
        qlPage.setUser(user);
        // Initialize the database
        pageRepository.saveAndFlush(qlPage);
        int databaseSizeBeforeDelete = pageRepository.findAll().size();

        // Delete the dlCategory
        restQlPageMockMvc.perform(delete(RESOURCE_API_ADMIN_QL_PAGES + "/{id}", qlPage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<QlPage> qlPageList = pageRepository.findAll();
        assertThat(qlPageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
