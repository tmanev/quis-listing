package com.manev.quislisting.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.DlListingContentFieldRel;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.DlCategoryTestComponent;
import com.manev.quislisting.service.DlContentFieldTestComponent;
import com.manev.quislisting.service.DlListingTestComponent;
import com.manev.quislisting.service.DlLocationTestComponent;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.form.DlListingForm;
import com.manev.quislisting.service.mapper.DlListingDtoToDlListingBaseMapper;
import com.manev.quislisting.service.mapper.DlListingDtoToDlListingModelMapper;
import com.manev.quislisting.service.model.DlContentFieldInput;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingFieldDTO;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
import com.manev.quislisting.web.rest.admin.DlCategoryAdminRestTest;
import com.manev.quislisting.web.rest.filter.DlContentFieldFilter;
import com.manev.quislisting.web.rest.filter.DlListingSearchFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlListingRestTest extends GenericResourceTest {

    private static final String UPDATE_CONTENT = "UPDATE_CONTENT";

    @Autowired
    private DlListingService dlListingService;

    @Autowired
    private DlListingRepository dlListingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private DlCategoryMapper dlCategoryMapper;

    @Autowired
    private DlLocationMapper dlLocationMapper;

    @Autowired
    private DlListingMapper dlListingMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private DlListingTestComponent dlListingTestComponent;

    @Autowired
    private DlCategoryTestComponent dlCategoryTestComponent;

    @Autowired
    private DlLocationTestComponent dlLocationTestComponent;

    @Autowired
    private DlContentFieldTestComponent dlContentFieldTestComponent;

    @Autowired
    private UserService userService;

    @Autowired
    private DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper;
    @Autowired
    private DlListingDtoToDlListingModelMapper dlListingDtoToDlListingModelMapper;

    private MockMvc restDlListingMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlListingRest dlListingResource = new DlListingRest(dlListingService, userService, dlListingDtoToDlListingBaseMapper, dlListingDtoToDlListingModelMapper);
        this.restDlListingMockMvc = MockMvcBuilders.standaloneSetup(dlListingResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    @WithUserDetails
    public void getAllDlListings() throws Exception {
        // Initialize the database
        DlListing dlListing = dlListingTestComponent.createDlListing(dlCategoryTestComponent.initCategory("en"));

        // Get all the navMenus
        ResultActions resultActions = restDlListingMockMvc.perform(get(RestRouter.DlListing.LIST + "?sort=id,desc&languageCode=en"));
        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dlListing.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DlListingTestComponent.DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DlListingTestComponent.DEFAULT_CONTENT)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DlListingTestComponent.DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DlListingTestComponent.DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].dlCategories.[*].name").value(hasItem(DlCategoryAdminRestTest.DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].dlCategories.[*].slug").value(hasItem(DlCategoryAdminRestTest.DEFAULT_SLUG)));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void createDlListing() throws Exception {
        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();
        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlLocation dlLocation = dlLocationTestComponent.initDlLocation();
        DlContentField priceCF = dlContentFieldTestComponent.createNumberField(dlCategory, "Number-price");
        DlContentField dlPhoneCF = dlContentFieldTestComponent.createStringField(dlCategory, "String-Phone");

        DlListingForm dlListingForm = dlListingTestComponent.createDlListingForm(dlCategory, dlLocation,
                new ArrayList<DlContentFieldInput>() {{
                    add(new DlContentFieldInput(priceCF, "180"));
                    add(new DlContentFieldInput(dlPhoneCF, "+123 456 555"));
                }});

        restDlListingMockMvc.perform(post(RestRouter.DlListing.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlListingForm)))
                .andExpect(status().isCreated());

        // Validate the DlListing in the database
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        Optional<User> optionalUser = userRepository.findOneByLogin("user");
        User user = optionalUser.orElse(null);

        DlListing dlListingSaved = dlListingList.get(dlListingList.size() - 1);
        assertThat(dlListingSaved.getTitle()).isEqualTo(DlListingTestComponent.DEFAULT_TITLE);
        assertThat(dlListingSaved.getName()).isEqualTo(DlListingTestComponent.DEFAULT_NAME);
        assertThat(dlListingSaved.getContent()).isEqualTo(DlListingTestComponent.DEFAULT_CONTENT);
        assertThat(dlListingSaved.getStatus()).isEqualTo(DlListing.Status.DRAFT);
        assertThat(dlListingSaved.getTranslation().getLanguageCode()).isEqualTo(user.getLangKey());
        assertThat(dlListingSaved.getTranslation().getSourceLanguageCode()).isNull();
        assertThat(dlListingSaved.getTranslation().getTranslationGroup().getId()).isNotEqualTo(DlListingTestComponent.TRANSLATION_GROUP_ID_SHOULD_NOT_BE_THIS);
        assertThat(dlListingSaved.getDlCategories().iterator().next().getId()).isEqualTo(dlCategory.getId());
        assertThat(dlListingSaved.getDlListingLocationRels().iterator().next().getDlLocation().getId()).isEqualTo(dlLocation.getId());
        assertThat(dlListingSaved.getUser().getId()).isEqualTo(user.getId());
        assertThat(dlListingSaved.getCreated()).isNotEqualTo(DlListingTestComponent.ZONED_DATE_TIME_SHOULD_NOT_BE_THIS);
        assertThat(dlListingSaved.getModified()).isNotEqualTo(DlListingTestComponent.ZONED_DATE_TIME_SHOULD_NOT_BE_THIS);
        assertThat(dlListingSaved.getDlAttachments()).isNull();

        Set<DlListingContentFieldRel> dlListingContentFieldRels = dlListingSaved.getDlListingContentFieldRels();
        dlListingContentFieldRels.stream().filter(dlContentFieldRelationship ->
                dlContentFieldRelationship.getDlContentField().getId().equals(priceCF.getId()))
                .findFirst()
                .ifPresent(dlContentFieldRelationship -> {
                    assertThat(dlContentFieldRelationship.getValue()).isEqualTo("180");
                });
        dlListingContentFieldRels.stream().filter(dlContentFieldRelationship ->
                dlContentFieldRelationship.getDlContentField().getId().equals(dlPhoneCF.getId()))
                .findFirst()
                .ifPresent(dlContentFieldRelationship -> {
                    assertThat(dlContentFieldRelationship.getValue()).isEqualTo("+123 456 555");
                });

        assertThat(dlListingSaved.getDlListingLocationRels().iterator().next().getDlLocation().getId()).isEqualTo(dlLocation.getId());
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updateDlListing() throws Exception {
        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();
        // initialize categories and location

        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlLocation dlLocation = dlLocationTestComponent.initDlLocation();
        DlContentField priceCF = dlContentFieldTestComponent.createNumberField(dlCategory, "Number-price");
        DlContentField dlPhoneCF = dlContentFieldTestComponent.createStringField(dlCategory, "String-Phone");

        DlListing dlListing = dlListingTestComponent.createDlListing(dlCategory);

        DlListingDTO createdDlListingDTO = dlListingMapper.dlListingToDlListingDTO(dlListing);

        // making updates to the dlListing so at the end object is updated and published
        createdDlListingDTO.setContent(UPDATE_CONTENT);
        createdDlListingDTO.setDlCategories(Collections.singletonList(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory)));
        createdDlListingDTO.setDlLocations(Collections.singletonList(dlLocationMapper.dlLocationToDlLocationDTO(dlLocation)));
        createdDlListingDTO.addDlListingField(new DlListingFieldDTO().id(priceCF.getId()).value("190"));
        createdDlListingDTO.addDlListingField(new DlListingFieldDTO().id(dlPhoneCF.getId()).value("+123 456 666"));

        // make the put request for updating the listing
        restDlListingMockMvc.perform(put(RestRouter.DlListing.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdDlListingDTO)))
                .andExpect(status().isOk());

        // make da asserts
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        DlListing dlListingSaved = dlListingList.get(dlListingList.size() - 1);
        assertThat(dlListingSaved.getTitle()).isEqualTo(DlListingTestComponent.DEFAULT_TITLE);
        assertThat(dlListingSaved.getName()).isEqualTo(DlListingTestComponent.DEFAULT_NAME);
        assertThat(dlListingSaved.getContent()).isEqualTo(UPDATE_CONTENT);
        assertThat(dlListingSaved.getStatus()).isEqualTo(DlListing.Status.DRAFT);
        assertThat(dlListingSaved.getTranslation().getLanguageCode()).isEqualTo(DlListingTestComponent.DEFAULT_LANGUAGE_CODE);
        assertThat(dlListingSaved.getDlCategories().iterator().next().getId()).isEqualTo(dlCategory.getId());
        assertThat(dlListingSaved.getDlListingLocationRels().iterator().next().getDlLocation().getId()).isEqualTo(dlLocation.getId());

        Set<DlListingContentFieldRel> dlListingContentFieldRels = dlListingSaved.getDlListingContentFieldRels();
        dlListingContentFieldRels.stream().filter(dlContentFieldRelationship ->
                dlContentFieldRelationship.getDlContentField().getId().equals(priceCF.getId()))
                .findFirst()
                .ifPresent(dlContentFieldRelationship -> {
                    assertThat(dlContentFieldRelationship.getValue()).isEqualTo("190");
                });
        dlListingContentFieldRels.stream().filter(dlContentFieldRelationship ->
                dlContentFieldRelationship.getDlContentField().getId().equals(dlPhoneCF.getId()))
                .findFirst()
                .ifPresent(dlContentFieldRelationship -> {
                    assertThat(dlContentFieldRelationship.getValue()).isEqualTo("+123 456 666");
                });
    }

    @Test
    @Transactional
    @WithUserDetails
    public void publishDlListing() throws Exception {
        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();

        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setTitle(DlListingTestComponent.DEFAULT_TITLE);
        dlListingDTO.setLanguageCode(DlListingTestComponent.DEFAULT_LANGUAGE_CODE);

        MvcResult mvcResult = restDlListingMockMvc.perform(post(RestRouter.DlListing.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlListingDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        // make da asserts
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        String contentAsString = mvcResult.getResponse().getContentAsString();

        DlListingDTO createdDlListingDTO = new ObjectMapper().readValue(contentAsString,
                DlListingDTO.class);

        // make the put request for updating the listing
        restDlListingMockMvc.perform(put(RestRouter.DlListing.PUBLISH)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdDlListingDTO)))
                .andExpect(status().isOk());

        DlListing publishedDlListing = dlListingRepository.findOne(createdDlListingDTO.getId());
        assertThat(publishedDlListing.getStatus()).isEqualTo(DlListing.Status.PUBLISHED);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void uploadAttachmentToDlListing() throws Exception {
        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();

        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setTitle(DlListingTestComponent.DEFAULT_TITLE);
        dlListingDTO.setLanguageCode(DlListingTestComponent.DEFAULT_LANGUAGE_CODE);

        MvcResult mvcResult = restDlListingMockMvc.perform(post(RestRouter.DlListing.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlListingDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        // make the asserts
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        String contentAsString = mvcResult.getResponse().getContentAsString();

        DlListingDTO createdDlListingDTO = new ObjectMapper().readValue(contentAsString,
                DlListingDTO.class);

        // make the upload
        AttachmentDTO[] uploadedAttachmentDTOs = doFileUpload(createdDlListingDTO.getId());
        assertThat(uploadedAttachmentDTOs).hasSize(1);
        AttachmentDTO attachmentDTO = uploadedAttachmentDTOs[0];

        // test removal of attachments
        MvcResult mvcResultDelete = restDlListingMockMvc.perform(delete(RestRouter.DlListing.ATTACHMENT_DETAIL, createdDlListingDTO.getId(), attachmentDTO.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsStringFromDelete = mvcResultDelete.getResponse().getContentAsString();
        DlListingDTO deletedAttachmentDlListingDTO = new ObjectMapper().readValue(contentAsStringFromDelete,
                DlListingDTO.class);
        assertThat(deletedAttachmentDlListingDTO.getAttachments()).isEmpty();

        // verify it also in database
        DlListing updatedDlListing = dlListingRepository.findOne(deletedAttachmentDlListingDTO.getId());
        assertThat(updatedDlListing.getDlAttachments()).isEmpty();
    }

    private AttachmentDTO[] doFileUpload(Long id) throws Exception {
        String contentType = Files.probeContentType(imageFile.toPath());

        MockMultipartFile multipartFile =
                new MockMultipartFile("files[]", imageFile.getName(), contentType, new FileInputStream(imageFile));

        ResultActions resultActions = this.restDlListingMockMvc.perform(fileUpload(RestRouter.DlListing.UPLOAD, id)
                .file(multipartFile))
                .andExpect(status().isOk());
        // test get call to verify the resource
        DlListing updatedDlListing = dlListingRepository.findOne(id);
        Set<DlAttachment> attachments = updatedDlListing.getDlAttachments();
        assertThat(attachments.size()).isEqualTo(1);
        DlAttachment attachment = attachments.iterator().next();
        attachmentsToBeDeletedFromJcrInAfter.add(attachment);

        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        return new ObjectMapper().readValue(contentAsString,
                AttachmentDTO[].class);
    }

    @Test
    @Transactional
    public void getActiveLanguages() throws Exception {
        // Initialize the database
        dlListingTestComponent.createDlListing(dlCategoryTestComponent.initCategory("en"));

        Language lanEn = new Language().code("en").active(true).englishName("English");
        Language lanBg = new Language().code("bg").active(true).englishName("Bulgarian");
        Language lanRo = new Language().code("ro").active(true).englishName("Romanian");
        Language lanRu = new Language().code("ru").active(true).englishName("Russian");
        languageRepository.saveAndFlush(lanEn);
        languageRepository.saveAndFlush(lanBg);
        languageRepository.saveAndFlush(lanRo);
        languageRepository.saveAndFlush(lanRu);

        // Get active languages
        restDlListingMockMvc.perform(get(RestRouter.DlListing.ACTIVE_LANGUAGES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].code").value(hasItem("en")))
                .andExpect(jsonPath("$.[*].englishName").value(hasItem("English")))
                .andExpect(jsonPath("$.[*].count").value(hasItem(1)));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldSearchByTitleOrDescription() throws Exception {
        DlListingDTO savedDlListingDTO = createDlListingDTO();
        dlListingService.publishListing(savedDlListingDTO.getId());

        DlListingSearchFilter dlListingSearchFilter = new DlListingSearchFilter();
        dlListingSearchFilter.setText(DlListingTestComponent.DEFAULT_TITLE);
        dlListingSearchFilter.setLanguageCode("en");

        String query = TestUtil.convertObjectToJsonString(dlListingSearchFilter);
        restDlListingMockMvc.perform(get(RestRouter.DlListing.SEARCH + "?query=" + URLEncoder.encode(query, "UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savedDlListingDTO.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DlListingTestComponent.DEFAULT_TITLE)));
    }

    private DlListingDTO createDlListingDTO() {
        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlLocation dlLocation = dlLocationTestComponent.initDlLocation();
        DlContentField priceCF = dlContentFieldTestComponent.createNumberField(dlCategory, "Number-price");
        DlContentField dlPhoneCF = dlContentFieldTestComponent.createStringField(dlCategory, "String-Phone");

        DlListingForm dlListingForm = dlListingTestComponent.createDlListingForm(dlCategory, dlLocation, new ArrayList<DlContentFieldInput>() {{
            add(new DlContentFieldInput(priceCF, "180"));
            add(new DlContentFieldInput(dlPhoneCF, "+123 456 555"));
        }});

        User loggedUser = getAuthenticatedUser(SecurityUtils.getCurrentUserLogin());
        return dlListingService.create(dlListingForm, loggedUser, "en");
    }

    private User getAuthenticatedUser(String login) {
        Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(login);
        if (userWithAuthoritiesByLogin.isPresent()) {
            return userWithAuthoritiesByLogin.get();
        }
        throw new UsernameNotFoundException("User " + login + " was not found in the " +
                "database");
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldSearchByCategory() throws Exception {
        DlCategory dlCategory1EN = dlCategoryTestComponent.initCategory("Category 1 EN", "en");
        DlCategory dlCategory11EN = dlCategoryTestComponent.initCategory(dlCategory1EN, "Category 11 EN", "en");
        DlCategory dlCategory2EN = dlCategoryTestComponent.initCategory("Category 2 EN", "en");
        DlCategory dlCategory21EN = dlCategoryTestComponent.initCategory(dlCategory2EN, "Category 21 EN", "en");
        DlCategory dlCategory22EN = dlCategoryTestComponent.initCategory(dlCategory2EN, "Category 22 EN", "en");

        DlCategory dlCategory1BG = dlCategoryTestComponent.initCategory(dlCategory1EN.getTranslation().getTranslationGroup().getId(), "Category 1 BG", "bg");
        DlCategory dlCategory11BG = dlCategoryTestComponent.initCategory(dlCategory11EN.getTranslation().getTranslationGroup().getId(), dlCategory1BG, "Category 11 BG", "bg");
        DlCategory dlCategory2BG = dlCategoryTestComponent.initCategory(dlCategory2EN.getTranslation().getTranslationGroup().getId(), "Category 2 BG", "bg");
        DlCategory dlCategory21BG = dlCategoryTestComponent.initCategory(dlCategory21EN.getTranslation().getTranslationGroup().getId(), dlCategory2BG, "Category 21 BG", "bg");
        DlCategory dlCategory22BG = dlCategoryTestComponent.initCategory(dlCategory22EN.getTranslation().getTranslationGroup().getId(), dlCategory2BG, "Category 22 BG", "bg");

        User loggedUser = getAuthenticatedUser(SecurityUtils.getCurrentUserLogin());

        DlListingDTO savedDlListing1 = dlListingService.create(dlListingTestComponent.createDlListingForm("Listing One",
                dlCategory11EN, null, null), loggedUser, "en");
        dlListingService.publishListing(savedDlListing1.getId());
        DlListingDTO savedDlListing2 = dlListingService.create(dlListingTestComponent.createDlListingForm("Listing Two",
                dlCategory21EN, null, null), loggedUser, "en");
        dlListingService.publishListing(savedDlListing2.getId());
        DlListingDTO savedDlListing3 = dlListingService.create(dlListingTestComponent.createDlListingForm("Listing Three",
                dlCategory21BG, null, null), loggedUser, "bg");
        dlListingService.publishListing(savedDlListing3.getId());
        DlListingDTO savedDlListingDTO4 = dlListingService.create(dlListingTestComponent.createDlListingForm("Listing Four",
                dlCategory22BG, null, null), loggedUser, "bg");
        dlListingService.publishListing(savedDlListingDTO4.getId());
        DlListingDTO savedDlListingDTO5 = dlListingService.create(dlListingTestComponent.createDlListingForm("Listing Five",
                dlCategory22BG, null, null), loggedUser, "bg");
        dlListingService.publishListing(savedDlListingDTO5.getId());

        // search by category
        DlListingSearchFilter dlListingSearchFilterEN = new DlListingSearchFilter();
        dlListingSearchFilterEN.setCategoryId(String.valueOf(dlCategory21EN.getId()));
        dlListingSearchFilterEN.setLanguageCode("en");

        String query = TestUtil.convertObjectToJsonString(dlListingSearchFilterEN);
        ResultActions perform = restDlListingMockMvc.perform(get(RestRouter.DlListing.SEARCH + "?query=" + URLEncoder.encode(query, "UTF-8")));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[0].id").value(savedDlListing2.getId().intValue()))
                .andExpect(jsonPath("$.[1].id").value(savedDlListing3.getId().intValue()))
                .andExpect(header().string("X-Total-Count", "2"));

        // search by category on different language
        DlListingSearchFilter dlListingSearchFilterBG = new DlListingSearchFilter();
        dlListingSearchFilterBG.setCategoryId(String.valueOf(dlCategory21BG.getId()));
        dlListingSearchFilterBG.setLanguageCode("bg");

        String queryBG = TestUtil.convertObjectToJsonString(dlListingSearchFilterBG);
        restDlListingMockMvc.perform(get(RestRouter.DlListing.SEARCH + "?query=" + URLEncoder.encode(queryBG, "UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[0].id").value(savedDlListing3.getId().intValue()))
                .andExpect(jsonPath("$.[1].id").value(savedDlListing2.getId().intValue()))
                .andExpect(header().string("X-Total-Count", "2"));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldSearchByLocation() throws Exception {
        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlLocation dlLocation = dlLocationTestComponent.initDlLocation();
        DlLocation dlLocation2 = dlLocationTestComponent.initDlLocation("Second location");

        DlListingForm dlListingForm1 = dlListingTestComponent.createDlListingForm(dlCategory, dlLocation, null);
        DlListingForm dlListingForm2 = dlListingTestComponent.createDlListingForm(dlCategory, dlLocation2, null);

        User loggedUser = getAuthenticatedUser(SecurityUtils.getCurrentUserLogin());

        DlListingDTO savedDlListingDTO1 = dlListingService.create(dlListingForm1, loggedUser, "en");
        dlListingService.publishListing(savedDlListingDTO1.getId());
        DlListingDTO savedDlListingDTO2 = dlListingService.create(dlListingForm2, loggedUser, "en");
        dlListingService.publishListing(savedDlListingDTO2.getId());

        DlListingSearchFilter dlListingSearchFilter = new DlListingSearchFilter();
        dlListingSearchFilter.setCountryId(String.valueOf(dlLocation2.getId()));
        dlListingSearchFilter.setLanguageCode("en");

        String query = TestUtil.convertObjectToJsonString(dlListingSearchFilter);
        ResultActions perform = restDlListingMockMvc.perform(get(RestRouter.DlListing.SEARCH + "?query=" + URLEncoder.encode(query, "UTF-8")));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savedDlListingDTO2.getId().intValue())))
                .andExpect(header().string("X-Total-Count", "1"));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldSearchByContentFields() throws Exception {
        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlContentField priceCF = dlContentFieldTestComponent.createNumberField(dlCategory, "Number-price");
        DlContentField dlPhoneCF = dlContentFieldTestComponent.createStringField(dlCategory, "String-Phone");
        DlContentField dlFuelCF = dlContentFieldTestComponent.createSelectField(dlCategory, "Select-Fuel", Arrays.asList("Diesel", "Gasoline"));

        User loggedUser = getAuthenticatedUser(SecurityUtils.getCurrentUserLogin());

        DlListingForm dlListingForm1 = dlListingTestComponent.createDlListingForm(dlCategory, null, new ArrayList<DlContentFieldInput>() {{
            add(new DlContentFieldInput(priceCF, "180"));
            add(new DlContentFieldInput(dlPhoneCF, "+123 456 555"));
            add(new DlContentFieldInput(dlFuelCF, null, String.valueOf(findByName("Diesel", dlFuelCF.getDlContentFieldItems()).getId())));
        }});

        DlListingForm dlListingForm2 = dlListingTestComponent.createDlListingForm(dlCategory, null, new ArrayList<DlContentFieldInput>() {{
            add(new DlContentFieldInput(priceCF, "150"));
            add(new DlContentFieldInput(dlPhoneCF, "+123 456 555"));
            add(new DlContentFieldInput(dlFuelCF, null, String.valueOf(findByName("Gasoline", dlFuelCF.getDlContentFieldItems()).getId())));
        }});

        DlListingDTO savedDlListingDTO1 = dlListingService.create(dlListingForm1, loggedUser, "en");
        dlListingService.publishListing(savedDlListingDTO1.getId());
        DlListingDTO savedDlListingDTO2 = dlListingService.create(dlListingForm2, loggedUser, "en");
        dlListingService.publishListing(savedDlListingDTO2.getId());

        DlListingSearchFilter dlListingSearchFilter = new DlListingSearchFilter();
        dlListingSearchFilter.setContentFields(new ArrayList<>(Collections.singletonList(new DlContentFieldFilter(dlFuelCF.getId(), null, String.valueOf(findByName("Gasoline", dlFuelCF.getDlContentFieldItems()).getId())))));
        dlListingSearchFilter.setLanguageCode("en");

        String query = TestUtil.convertObjectToJsonString(dlListingSearchFilter);
        ResultActions perform = restDlListingMockMvc.perform(get(RestRouter.DlListing.SEARCH + "?query=" + URLEncoder.encode(query, "UTF-8")));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savedDlListingDTO2.getId().intValue())))
                .andExpect(header().string("X-Total-Count", "1"));
    }

    private DlContentFieldItem findByName(String item, Set<DlContentFieldItem> items) {
        for (DlContentFieldItem dlContentFieldItem : items) {
            if (item.equals(dlContentFieldItem.getValue())) {
                return dlContentFieldItem;
            }
        }
        return null;
    }

}
