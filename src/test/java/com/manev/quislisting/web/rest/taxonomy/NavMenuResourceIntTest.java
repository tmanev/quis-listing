package com.manev.quislisting.web.rest.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.NavMenuBuilder;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.taxonomy.NavMenuService;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
import com.manev.quislisting.service.taxonomy.mapper.NavMenuMapper;
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

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_NAV_MENUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class NavMenuResourceIntTest {

    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_SLUG = "DEFAULT_SLUG";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";
    private static final Long DEFAULT_PARENT_ID = null;
    private static final Long DEFAULT_COUNT = 0L;

    private static final String DEFAULT_NAME_2 = "DEFAULT_NAME_2";
    private static final String DEFAULT_SLUG_2 = "DEFAULT_SLUG_2";
    private static final String DEFAULT_DESCRIPTION_2 = "DEFAULT_DESCRIPTION_2";
    private static final Long DEFAULT_COUNT_2 = 0L;

    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String UPDATED_SLUG = "UPDATED_SLUG";
    private static final String UPDATED_DESCRIPTION = "UPDATED_DESCRIPTION";
    private static final Long UPDATED_COUNT = 1L;

    @Autowired
    private NavMenuService navMenuService;

    @Autowired
    private NavMenuRepository navMenuRepository;

    @Autowired
    private NavMenuMapper navMenuMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restNavMenuMockMvc;

    private NavMenu navMenu;

    public static NavMenu createEntity() {
        return NavMenuBuilder.aNavMenu().withTerm(
                TermBuilder.aTerm().withName(DEFAULT_NAME).withSlug(DEFAULT_SLUG).build()
        ).withDescription(DEFAULT_DESCRIPTION)
                .withCount(DEFAULT_COUNT).build();
    }

    public static NavMenu createEntity2() {
        return NavMenuBuilder.aNavMenu().withTerm(
                TermBuilder.aTerm().withName(DEFAULT_NAME_2).withSlug(DEFAULT_SLUG_2).build()
        ).withDescription(DEFAULT_DESCRIPTION_2)
                .withCount(DEFAULT_COUNT_2).build();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NavMenuResource navMenuResource = new NavMenuResource(navMenuService);
        this.restNavMenuMockMvc = MockMvcBuilders.standaloneSetup(navMenuResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        navMenuRepository.deleteAll();
        navMenu = createEntity();
    }

    @Test
    @Transactional
    public void createNavMenu() throws Exception {
        int databaseSizeBeforeCreate = navMenuRepository.findAll().size();

        // Create the NavMenu
        NavMenuDTO navMenuDTO = navMenuMapper.navMenuToNavMenuDTO(navMenu);

        restNavMenuMockMvc.perform(post(RESOURCE_API_NAV_MENUS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(navMenuDTO)))
                .andExpect(status().isCreated());

        // Validate the NavMenu in the database
        List<NavMenu> navMenuList = navMenuRepository.findAll();
        assertThat(navMenuList).hasSize(databaseSizeBeforeCreate + 1);
        NavMenu navMenu = navMenuList.get(navMenuList.size() - 1);
        assertThat(navMenu.getTerm().getName()).isEqualTo(DEFAULT_NAME);
        assertThat(navMenu.getTerm().getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(navMenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
//        assertThat(navMenu.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(navMenu.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    @Transactional
    public void createNavMenuExistingId() throws Exception {
        int databaseSizeBeforeCreate = navMenuRepository.findAll().size();

        // Create the NavMenu with an existing ID
        NavMenu existingNavMenu = new NavMenu();
        existingNavMenu.setId(1L);
        NavMenuDTO existingNavMenuDTO = navMenuMapper.navMenuToNavMenuDTO(existingNavMenu);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNavMenuMockMvc.perform(post(RESOURCE_API_NAV_MENUS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingNavMenuDTO)))
                .andExpect(status().isBadRequest());

        // Validate the NavMenu in the database
        List<NavMenu> navMenuList = navMenuRepository.findAll();
        assertThat(navMenuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNavMenus() throws Exception {
        // Initialize the database
        navMenuRepository.saveAndFlush(navMenu);

        // Get all the navMenus
        restNavMenuMockMvc.perform(get(RESOURCE_API_NAV_MENUS + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(navMenu.getId().intValue())))
                .andExpect(jsonPath("$.[*].term.name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
                .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));
    }

    @Test
    @Transactional
    public void getNavMenu() throws Exception {
        // Initialize the database
        navMenuRepository.saveAndFlush(navMenu);

        // Get the NavMenu
        restNavMenuMockMvc.perform(get(RESOURCE_API_NAV_MENUS + "/{id}", navMenu.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(navMenu.getId().intValue()))
                .andExpect(jsonPath("$.term.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
                .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNavMenu() throws Exception {
        // Get the navMenu
        restNavMenuMockMvc.perform(get(RESOURCE_API_NAV_MENUS + "/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNavMenu() throws Exception {
        // Initialize the database
        navMenuRepository.saveAndFlush(navMenu);
        NavMenu parent = navMenuRepository.saveAndFlush(createEntity2());
        int databaseSizeBeforeUpdate = navMenuRepository.findAll().size();

        // Update the NavMenu
        NavMenu updatedNavMenu = navMenuRepository.findOne(navMenu.getId());
        updatedNavMenu.getTerm().name(UPDATED_NAME).slug(UPDATED_SLUG);

        updatedNavMenu.setDescription(UPDATED_DESCRIPTION);
        updatedNavMenu.setParent(parent);
        updatedNavMenu.setCount(UPDATED_COUNT);
        NavMenuDTO navMenuDTO = navMenuMapper.navMenuToNavMenuDTO(updatedNavMenu);

        restNavMenuMockMvc.perform(put(RESOURCE_API_NAV_MENUS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(navMenuDTO)))
                .andExpect(status().isOk());

        // Validate the NavMenu in the database
        List<NavMenu> navMenuList = navMenuRepository.findAll();
        assertThat(navMenuList).hasSize(databaseSizeBeforeUpdate);
        NavMenu testNavMenu = navMenuRepository.findOne(updatedNavMenu.getId());
        assertThat(testNavMenu.getTerm().getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNavMenu.getTerm().getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testNavMenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNavMenu.getParent().getId()).isEqualTo(parent.getId());
        assertThat(testNavMenu.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingNavMenu() throws Exception {
        int databaseSizeBeforeUpdate = navMenuRepository.findAll().size();

        // Create the NavMenu
        NavMenuDTO navMenuDTO = navMenuMapper.navMenuToNavMenuDTO(navMenu);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNavMenuMockMvc.perform(put(RESOURCE_API_NAV_MENUS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(navMenuDTO)))
                .andExpect(status().isCreated());

        // Validate the NavMenu in the database
        List<NavMenu> navMenuList = navMenuRepository.findAll();
        assertThat(navMenuList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNavMenu() throws Exception {
        // Initialize the database
        navMenuRepository.saveAndFlush(navMenu);
        int databaseSizeBeforeDelete = navMenuRepository.findAll().size();

        // Get the navMenu
        restNavMenuMockMvc.perform(delete(RESOURCE_API_NAV_MENUS + "/{id}", navMenu.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NavMenu> navMenuList = navMenuRepository.findAll();
        assertThat(navMenuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}