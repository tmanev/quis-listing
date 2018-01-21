package com.manev.quislisting.web.rest.post;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.repository.StaticPageRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.service.post.StaticPageService;
import com.manev.quislisting.service.post.dto.StaticPageDTO;
import com.manev.quislisting.service.post.mapper.QlStaticPageMapper;
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

import java.util.ArrayList;
import java.util.List;

import static com.manev.quislisting.web.rest.RestRouter.RESOURCE_API_ADMIN_QL_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class StaticPageResourceTest {

    private static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    private static final String DEFAULT_CONTENT = "DEFAULT_CONTENT";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final StaticPage.Status DEFAULT_STATUS = StaticPage.Status.PUBLISH;

    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String UPDATED_TITLE = "UPDATED_TITLE";
    private static final StaticPage.Status UPDATED_STATUS = StaticPage.Status.DRAFT;
    private static final String UPDATED_CONTENT = "UPDATED_CONTENT";

    @Autowired
    private StaticPageService staticPageService;

    @Autowired
    private QlStaticPageMapper qlStaticPageMapper;

    @Autowired
    private StaticPageRepository staticPageRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restQlPageMockMvc;

    private StaticPage staticPage;

    public static StaticPage createEntity() {

        StaticPage qlPage = new StaticPage();
        qlPage.setContent(DEFAULT_CONTENT);
        qlPage.setName(DEFAULT_NAME);
        qlPage.setStatus(DEFAULT_STATUS);
        qlPage.setTitle(DEFAULT_TITLE);
        qlPage.setTranslation(TranslationBuilder.aTranslation()
                .withLanguageCode("en")
                .withTranslationGroup(new TranslationGroup())
                .build());

        return qlPage;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StaticPageResource staticPageResource = new StaticPageResource(staticPageService);
        this.restQlPageMockMvc = MockMvcBuilders.standaloneSetup(staticPageResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
        setupSecurityContext();
    }

    @Before
    public void initTest() {
//        staticPageRepository.deleteAll();
        staticPage = createEntity();
    }

    @Test
    @Transactional
    public void getAllQlPages() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

        // Get all the navMenus
        ResultActions resultActions = restQlPageMockMvc.perform(get(RESOURCE_API_ADMIN_QL_PAGES + "?sort=id,desc&languageCode=en"));
        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(staticPage.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getActiveLanguages() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);

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
        int databaseSizeBeforeCreate = staticPageRepository.findAll().size();

        // Create the DlCategory
        StaticPageDTO staticPageDTO = qlStaticPageMapper.staticPageToStaticPageDTO(staticPage, createActiveLanguages());

        restQlPageMockMvc.perform(post(RESOURCE_API_ADMIN_QL_PAGES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staticPageDTO)))
                .andExpect(status().isCreated());

        // Validate the DlCategory in the database
        List<StaticPage> qlPageTrList = staticPageRepository.findAll();
        assertThat(qlPageTrList).hasSize(databaseSizeBeforeCreate + 1);
        StaticPage qlPageSaved = qlPageTrList.get(qlPageTrList.size() - 1);
        assertThat(qlPageSaved.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(qlPageSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(qlPageSaved.getContent()).isEqualTo(DEFAULT_CONTENT);
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
        staticPageRepository.saveAndFlush(staticPage);

        // Get the DlCategory
        restQlPageMockMvc.perform(get(RESOURCE_API_ADMIN_QL_PAGES + "/{id}", staticPage.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(staticPage.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
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
        staticPageRepository.saveAndFlush(staticPage);
        int databaseSizeBeforeUpdate = staticPageRepository.findAll().size();

        // Update the DlCategory
        StaticPage updatedQlPage = staticPageRepository.findOne(this.staticPage.getId());
        updatedQlPage.setName(UPDATED_NAME);
        updatedQlPage.setTitle(UPDATED_TITLE);
        updatedQlPage.setContent(UPDATED_CONTENT);
        updatedQlPage.setStatus(UPDATED_STATUS);

        StaticPageDTO staticPageDTO = qlStaticPageMapper.staticPageToStaticPageDTO(updatedQlPage, createActiveLanguages());

        restQlPageMockMvc.perform(put(RESOURCE_API_ADMIN_QL_PAGES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staticPageDTO)))
                .andExpect(status().isOk());

        // Validate the DlCategory in the database
        List<StaticPage> qlPagesList = staticPageRepository.findAll();
        assertThat(qlPagesList).hasSize(databaseSizeBeforeUpdate);
        StaticPage testQlPage = staticPageRepository.findOne(updatedQlPage.getId());
        assertThat(testQlPage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQlPage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQlPage.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingQlPage() throws Exception {
        int databaseSizeBeforeUpdate = staticPageRepository.findAll().size();

        // Create the QlPage
        StaticPageDTO staticPageDTO = qlStaticPageMapper.staticPageToStaticPageDTO(staticPage, createActiveLanguages());

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQlPageMockMvc.perform(put(RESOURCE_API_ADMIN_QL_PAGES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staticPageDTO)))
                .andExpect(status().isCreated());

        // Validate the QlPage in the database
        List<StaticPage> qlPageList = staticPageRepository.findAll();
        assertThat(qlPageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQlPage() throws Exception {
        // Initialize the database
        staticPageRepository.saveAndFlush(staticPage);
        int databaseSizeBeforeDelete = staticPageRepository.findAll().size();

        // Delete the dlCategory
        restQlPageMockMvc.perform(delete(RESOURCE_API_ADMIN_QL_PAGES + "/{id}", staticPage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StaticPage> qlPageList = staticPageRepository.findAll();
        assertThat(qlPageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
