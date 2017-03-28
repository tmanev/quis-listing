package com.manev.quislisting.web.rest.post;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.post.PostMeta;
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
import java.util.HashSet;
import java.util.Set;

import static com.manev.quislisting.domain.post.PostMeta.*;
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
    }

    @Before
    public void initTest() {
        postRepository.deleteAll();
        user = userRepository.findOneByLogin("admin").get();
        dlListing = createEntity(em);
        dlListing.setPostMeta(createPostMeta(dlListing));
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
                .andExpect(jsonPath("$.[*].status").value(hasItem(META_VALUE_LISTING_STATUS)));
    }
}
