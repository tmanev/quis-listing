package com.manev.quislisting.web.rest.post;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.builder.DlListingBuilder;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.service.post.DlListingService;
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
import java.time.ZonedDateTime;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_DL_LISTINGS;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlListingResourceTest {

    private static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    private static final String DEFAULT_CONTENT = "DEFAULT_CONTENT";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final ZonedDateTime DEFAULT_MODIFIED = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
    private static final String DEFAULT_STATUS = "DEFAULT_STATUS";

    @Autowired
    private DlListingService dlListingService;

    @Autowired
    private PostRepository<DlListing> postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restDlListingMockMvc;

    private DlListing dlListing;

    private User user;

    public static DlListing createEntity(EntityManager em) {
        return DlListingBuilder.aDlListing()
                .withTitle(DEFAULT_TITLE)
                .withContent(DEFAULT_CONTENT)
                .withName(DEFAULT_NAME)
                .withCreated(DEFAULT_CREATED)
                .withModified(DEFAULT_MODIFIED)
                .withStatus(DEFAULT_STATUS)
                .build();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlListingResource navMenuResource = new DlListingResource(dlListingService);
        this.restDlListingMockMvc = MockMvcBuilders.standaloneSetup(navMenuResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        postRepository.deleteAll();
        user = userRepository.findOneByLogin("admin").get();
        dlListing = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllDlListings() throws Exception {
        // Initialize the database
        dlListing.setUser(user);
        postRepository.saveAndFlush(dlListing);

        // Get all the navMenus
        restDlListingMockMvc.perform(get(RESOURCE_API_ADMIN_DL_LISTINGS + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dlListing.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
}
