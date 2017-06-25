package com.manev.quislisting.web.client;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.post.AbstractPostService;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.ClientDlListingDTO;
import com.manev.quislisting.web.rest.GenericResourceTest;
import com.manev.quislisting.web.rest.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_DL_LISTINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class ClientListingResourceTest extends GenericResourceTest {

    private static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    private static final Long DEFAULT_CATEGORY_ID = 1L;

    @Autowired
    private DlListingService dlListingService;
    @Autowired
    private QlConfigService qlConfigService;
    @Autowired
    private AbstractPostService abstractPostService;
    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private DlListingRepository dlListingRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientResourceMockMvc;

    private ClientDlListingDTO createClientListingDto() {
        ClientDlListingDTO clientDlListingDTO = new ClientDlListingDTO(null, DEFAULT_TITLE,
                DEFAULT_CATEGORY_ID);
        return clientDlListingDTO;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientListingResource clientListingResource = new ClientListingResource(dlListingService,
                qlConfigService, abstractPostService, localeResolver);
        this.restClientResourceMockMvc = MockMvcBuilders.standaloneSetup(clientListingResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
        setupSecurityContext();
    }

    @Test
    @Transactional
    public void createDlListing() throws Exception {
        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();

        ClientDlListingDTO clientListingDto = createClientListingDto();

        ResultActions resultActions = restClientResourceMockMvc.perform(post(RESOURCE_API_DL_LISTINGS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientListingDto)));
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/my-listings/edit/")));

        // Validate the DlListing in the database
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        DlListing dlListingSaved = dlListingList.get(dlListingList.size() - 1);
        assertThat(dlListingSaved.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(dlListingSaved.getStatus()).isEqualTo(DlListing.Status.UNFINISHED);
//        assertThat(dlListingSaved.getTranslation().getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
    }

}