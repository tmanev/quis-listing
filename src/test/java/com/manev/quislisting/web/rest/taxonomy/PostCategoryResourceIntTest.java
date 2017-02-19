package com.manev.quislisting.web.rest.taxonomy;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.PostCategoryBuilder;
import com.manev.quislisting.repository.taxonomy.PostCategoryRepository;
import com.manev.quislisting.service.taxonomy.PostCategoryService;
import com.manev.quislisting.service.taxonomy.dto.PostCategoryDTO;
import com.manev.quislisting.service.taxonomy.mapper.PostCategoryMapper;
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

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_POST_CATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class PostCategoryResourceIntTest {

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
    private PostCategoryService postCategoryService;

    @Autowired
    private PostCategoryRepository postCategoryRepository;

    @Autowired
    private PostCategoryMapper postCategoryMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restBookMockMvc;

    private PostCategory postCategory;

    public static PostCategory createEntity() {
        return PostCategoryBuilder.aPostCategory().withTerm(
                TermBuilder.aTerm().withName(DEFAULT_NAME).withSlug(DEFAULT_SLUG).build()
        ).withDescription(DEFAULT_DESCRIPTION)
                .withCount(DEFAULT_COUNT).build();
    }

    public static PostCategory createEntity2() {
        return PostCategoryBuilder.aPostCategory().withTerm(
                TermBuilder.aTerm().withName(DEFAULT_NAME_2).withSlug(DEFAULT_SLUG_2).build()
        ).withDescription(DEFAULT_DESCRIPTION_2)
                .withCount(DEFAULT_COUNT_2).build();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostCategoriesResource postCategoriesResource = new PostCategoriesResource(postCategoryService);
        this.restBookMockMvc = MockMvcBuilders.standaloneSetup(postCategoriesResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        postCategoryRepository.deleteAll();
        postCategory = createEntity();
    }

    @Test
    @Transactional
    public void createPostCategory() throws Exception {
        int databaseSizeBeforeCreate = postCategoryRepository.findAll().size();

        // Create the PostCategory
        PostCategoryDTO postCategoryDTO = postCategoryMapper.postCategoryToPostCategoryDTO(postCategory);

        restBookMockMvc.perform(post(RESOURCE_API_POST_CATEGORIES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postCategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the PostCategory in the database
        List<PostCategory> postCategoryList = postCategoryRepository.findAll();
        assertThat(postCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        PostCategory postCategory = postCategoryList.get(postCategoryList.size() - 1);
        assertThat(postCategory.getTerm().getName()).isEqualTo(DEFAULT_NAME);
        assertThat(postCategory.getTerm().getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(postCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
//        assertThat(postCategory.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(postCategory.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    @Transactional
    public void createPostCategoryExistingId() throws Exception {
        int databaseSizeBeforeCreate = postCategoryRepository.findAll().size();

        // Create the PostCategory with an existing ID
        PostCategory existingPostCategory = new PostCategory();
        existingPostCategory.setId(1L);
        PostCategoryDTO existingPostCategoryDTO = postCategoryMapper.postCategoryToPostCategoryDTO(existingPostCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookMockMvc.perform(post(RESOURCE_API_POST_CATEGORIES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingPostCategoryDTO)))
                .andExpect(status().isBadRequest());

        // Validate the PostCategory in the database
        List<PostCategory> bookList = postCategoryRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPostCategories() throws Exception {
        // Initialize the database
        postCategoryRepository.saveAndFlush(postCategory);

        // Get all the postCategories
        restBookMockMvc.perform(get(RESOURCE_API_POST_CATEGORIES + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(postCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].term.name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
                .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));
    }

    @Test
    @Transactional
    public void getPostCategory() throws Exception {
        // Initialize the database
        postCategoryRepository.saveAndFlush(postCategory);

        // Get the PostCategory
        restBookMockMvc.perform(get(RESOURCE_API_POST_CATEGORIES + "/{id}", postCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(postCategory.getId().intValue()))
                .andExpect(jsonPath("$.term.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
                .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPostCategory() throws Exception {
        // Get the postCategory
        restBookMockMvc.perform(get(RESOURCE_API_POST_CATEGORIES + "/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostCategory() throws Exception {
        // Initialize the database
        postCategoryRepository.saveAndFlush(postCategory);
        PostCategory parent = postCategoryRepository.saveAndFlush(createEntity2());
        int databaseSizeBeforeUpdate = postCategoryRepository.findAll().size();

        // Update the PostCategory
        PostCategory updatedPostCategory = postCategoryRepository.findOne(postCategory.getId());
        updatedPostCategory.getTerm().name(UPDATED_NAME).slug(UPDATED_SLUG);

        updatedPostCategory.setDescription(UPDATED_DESCRIPTION);
        updatedPostCategory.setParent(parent);
        updatedPostCategory.setCount(UPDATED_COUNT);
        PostCategoryDTO postCategoryDTO = postCategoryMapper.postCategoryToPostCategoryDTO(updatedPostCategory);

        restBookMockMvc.perform(put(RESOURCE_API_POST_CATEGORIES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postCategoryDTO)))
                .andExpect(status().isOk());

        // Validate the PostCategory in the database
        List<PostCategory> postCategoryList = postCategoryRepository.findAll();
        assertThat(postCategoryList).hasSize(databaseSizeBeforeUpdate);
        PostCategory testDlLocation = postCategoryRepository.findOne(updatedPostCategory.getId());
        assertThat(testDlLocation.getTerm().getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDlLocation.getTerm().getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testDlLocation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDlLocation.getParent().getId()).isEqualTo(parent.getId());
        assertThat(testDlLocation.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingPostCategory() throws Exception {
        int databaseSizeBeforeUpdate = postCategoryRepository.findAll().size();

        // Create the PostCategory
        PostCategoryDTO postCategoryDTO = postCategoryMapper.postCategoryToPostCategoryDTO(postCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBookMockMvc.perform(put(RESOURCE_API_POST_CATEGORIES)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postCategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the PostCategory in the database
        List<PostCategory> postCategoryList = postCategoryRepository.findAll();
        assertThat(postCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePostCategory() throws Exception {
        // Initialize the database
        postCategoryRepository.saveAndFlush(postCategory);
        int databaseSizeBeforeDelete = postCategoryRepository.findAll().size();

        // Get the postCategory
        restBookMockMvc.perform(delete(RESOURCE_API_POST_CATEGORIES + "/{id}", postCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PostCategory> postCategoryList = postCategoryRepository.findAll();
        assertThat(postCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}