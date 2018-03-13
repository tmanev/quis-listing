package com.manev.quislisting.web.rest;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.DlMessageOverviewRepository;
import com.manev.quislisting.repository.DlMessagesRepository;
import com.manev.quislisting.security.AuthoritiesConstants;
import com.manev.quislisting.service.DlCategoryTestComponent;
import com.manev.quislisting.service.DlListingTestComponent;
import com.manev.quislisting.service.DlMessageTestComponent;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.form.DlListingMessageForm;
import com.manev.quislisting.service.form.DlWriteMessageForm;
import com.manev.quislisting.service.post.DlMessagesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private DlMessageOverviewRepository dlMessagesOverviewRepository;

    private MockMvc restDlMessageMockMvc;

    private User sender;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        sender = dlMessageTestComponent.createUser(DlMessageTestComponent.LOGIN, DlMessageTestComponent.FIRST_NAME,
                DlMessageTestComponent.LAST_NAME, DlMessageTestComponent.EMAIL, DlMessageTestComponent.IMAGE_URL,
                DlMessageTestComponent.PASSWORD, DlMessageTestComponent.ACTIVATED, AuthoritiesConstants.USER);

        final DlMessageRest dlMessageResource = new DlMessageRest(dlMessagesService, userService);
        this.restDlMessageMockMvc = MockMvcBuilders.standaloneSetup(dlMessageResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldCreateAndGetAllConversationsForLoggedUser() throws Exception {
        DlListing dlListing = dlListingTestComponent.createDlListing(dlCategoryTestComponent
                .initCategory(DlMessageTestComponent.LANG_KEY));
        sendMessageToListingAuthorized(dlListing, sender);

        ResultActions resultActions = restDlMessageMockMvc.perform(get(RestRouter.DlMessageCenter.CONVERSATIONS + "?sort=id,desc"));
        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].listingId").value(hasItem(dlListing.getId().intValue())))
                .andExpect(jsonPath("$.[*].sender.id").value(hasItem(sender.getId().intValue())))
                .andExpect(jsonPath("$.[*].receiver.id").value(hasItem(dlListing.getUser().getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DlMessageTestComponent.TEXT)));
    }

    private void sendMessageToListingAuthorized(DlListing dlListing, User sender) {
        DlListingMessageForm dlListingMessageForm = new DlListingMessageForm();
        dlListingMessageForm.setText(DlMessageTestComponent.TEXT);
        dlMessagesService.sendMessageForListing(dlListing.getId(), dlListingMessageForm, sender.getLogin());
    }

    @Test
    @Transactional
    @WithUserDetails
    public void testReplayToListingMessage() throws Exception {
        DlListing dlListing = dlListingTestComponent.createDlListing(dlCategoryTestComponent
                .initCategory(DlMessageTestComponent.LANG_KEY));
        sendMessageToListingAuthorized(dlListing, sender);

        final int messageTableBeforeCreate = dlMessagesRepository.findAll().size();
        DlMessageOverview messageOverviewForReceiver = dlMessagesOverviewRepository.findOneByDlListingAndSenderAndReceiver(dlListing.getId(), sender, dlListing.getUser());
        assertThat(messageOverviewForReceiver).isNotNull();

        DlWriteMessageForm dlWriteMessageForm = new DlWriteMessageForm();
        dlWriteMessageForm.setText("Some replay text");

        restDlMessageMockMvc.perform(post(RestRouter.DlMessageCenter.CONVERSATION_THREAD, messageOverviewForReceiver.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlWriteMessageForm)))
                .andExpect(status().isCreated());

        final List<DlMessage> dlMessageList = dlMessagesRepository.findAll();
        assertThat(dlMessageList).hasSize(messageTableBeforeCreate + 1);

        DlMessageOverview recrivedMessage = dlMessagesOverviewRepository.findOneByDlListingAndSenderAndReceiver(dlListing.getId(), dlListing.getUser(), sender);
        assertThat(recrivedMessage).isNotNull();

        DlMessage lastReplyMsg = dlMessagesRepository.findAllMessages(new PageRequest(0, 10), dlListing.getId(), dlListing.getUser(), sender).getContent().get(0);
        assertThat(lastReplyMsg.getSender().getId()).isEqualTo(dlListing.getUser().getId());
        assertThat(lastReplyMsg.getReceiver().getId()).isEqualTo(sender.getId());
        assertThat(lastReplyMsg.getText()).isEqualTo("Some replay text");
        assertThat(lastReplyMsg.getListingId()).isEqualTo(dlListing.getId());
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldGetConversationThread() throws Exception {
        DlListing dlListing = dlListingTestComponent.createDlListing(dlCategoryTestComponent
                .initCategory(DlMessageTestComponent.LANG_KEY));
        sendMessageToListingAuthorized(dlListing, sender);

        DlMessageOverview messageOverviewFromSender = dlMessagesOverviewRepository.findOneByDlListingAndSenderAndReceiver(dlListing.getId(), sender, dlListing.getUser());

        restDlMessageMockMvc.perform(get(RestRouter.DlMessageCenter.CONVERSATION_THREAD, messageOverviewFromSender.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].listingId").value(hasItem(dlListing.getId().intValue())))
                .andExpect(jsonPath("$.[*].sender.id").value(hasItem(sender.getId().intValue())))
                .andExpect(jsonPath("$.[*].receiver.id").value(hasItem(dlListing.getUser().getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DlMessageTestComponent.TEXT)));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldDeleteConversation() throws Exception {
        DlListing dlListing = dlListingTestComponent.createDlListing(dlCategoryTestComponent
                .initCategory(DlMessageTestComponent.LANG_KEY));
        sendMessageToListingAuthorized(dlListing, sender);

        DlMessageOverview messageOverviewFromSender = dlMessagesOverviewRepository.findOneByDlListingAndSenderAndReceiver(dlListing.getId(), sender, dlListing.getUser());

        restDlMessageMockMvc.perform(delete(RestRouter.DlMessageCenter.CONVERSATION_THREAD, messageOverviewFromSender.getId()))
                .andExpect(status().isOk());


        DlMessageOverview conversation = dlMessagesOverviewRepository.findOneByDlListingAndSenderAndReceiver(dlListing.getId(), dlListing.getUser(), sender);
        assertThat(conversation).isNull();
    }
}
