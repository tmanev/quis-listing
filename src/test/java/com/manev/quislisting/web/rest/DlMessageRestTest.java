package com.manev.quislisting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.DlMessagesOverviewRepository;
import com.manev.quislisting.repository.DlMessagesRepository;
import com.manev.quislisting.security.AuthoritiesConstants;
import com.manev.quislisting.service.DlCategoryTestComponent;
import com.manev.quislisting.service.DlListingTestComponent;
import com.manev.quislisting.service.DlMessageTestComponent;
import com.manev.quislisting.service.MailService;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.dto.DlMessageDTO;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.DlMessagesService;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

/**
 * Test for {@link DlMessageRest}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlMessageRestTest extends GenericResourceTest {

    @Autowired
    private DlMessagesService dlMessagesService;

    @Autowired
    private UserService userService;

    @Autowired
    private DlListingService dlListingService;

    @Autowired
    private MailService mailService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private DlMessageTestComponent dlMessageTestComponent;

    @Autowired
    private DlListingTestComponent dlListingTestComponent;

    @Autowired
    private DlCategoryTestComponent dlCategoryTestComponent;

    @Autowired
    private DlMessagesRepository dlMessagesRepository;

    @Autowired
    private DlMessagesOverviewRepository dlMessagesOverviewRepository;

    private MockMvc restDlMessageMockMvc;

    private User sender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        sender = dlMessageTestComponent.createUser(DlMessageTestComponent.LOGIN, DlMessageTestComponent.FIRST_NAME,
                DlMessageTestComponent.LAST_NAME, DlMessageTestComponent.EMAIL, DlMessageTestComponent.IMAGE_URL,
                DlMessageTestComponent.PASSWORD, DlMessageTestComponent.ACTIVATED, AuthoritiesConstants.USER);

        final DlMessageRest dlMessageResource = new DlMessageRest(dlMessagesService, userService, dlListingService,
                mailService);
        this.restDlMessageMockMvc = MockMvcBuilders.standaloneSetup(dlMessageResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    @WithUserDetails
    public void createAndGetAllMessagesByReceiver() throws Exception {
        final DlMessageOverview dlMessage = dlMessageTestComponent.createDlMessageOverview();

        final ResultActions resultActions = restDlMessageMockMvc.perform(get(RestRouter.DlMessage.LIST
                + "?sort=id,desc&languageCode=en"));
        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dlMessage.getId().intValue())))
                .andExpect(jsonPath("$.[*].receiverId").value(hasItem(dlMessage.getReceiver().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DlMessageTestComponent.TEXT)))
                .andExpect(jsonPath("$.[*].listingId").value(hasItem(dlMessage.getListingId().intValue())));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void testCreateDlMessage() throws Exception {
        final int messageTableBeforeCreate = dlMessagesRepository.findAll().size();
        final int messageOverviewTableBeforeCreate = dlMessagesOverviewRepository.findAll().size();

        final DlMessageDTO dlMessageDTO = dlMessageTestComponent.createDlMessageDTO();

        final DlListing dlListing = dlListingTestComponent.createDlListing(dlCategoryTestComponent
                .initCategory(DlMessageTestComponent.LANG_KEY));
        dlMessageDTO.setListingId(dlListing.getId());

        restDlMessageMockMvc.perform(post(RestRouter.DlMessage.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlMessageDTO)))
                .andExpect(status().isCreated());

        final List<DlMessage> dlMessageList = dlMessagesRepository.findAll();
        final List<DlMessageOverview> dlMessageOverviewList = dlMessagesOverviewRepository.findAll();
        assertThat(dlMessageList).hasSize(messageTableBeforeCreate + 1);
        assertThat(dlMessageOverviewList).hasSize(messageOverviewTableBeforeCreate + 1);

        final DlMessage dlMessageSaved = dlMessageList.get(dlMessageList.size() - 1);

        assertThat(dlMessageSaved.getSender()).isEqualTo(sender);
        assertThat(dlMessageSaved.getReceiver()).isEqualTo(DlMessageTestComponent.RECEIVER);
        assertThat(dlMessageSaved.getText()).isEqualTo(DlMessageTestComponent.TEXT);
        assertThat(dlMessageSaved.getListingId()).isEqualTo(dlListing.getId());
    }
}
