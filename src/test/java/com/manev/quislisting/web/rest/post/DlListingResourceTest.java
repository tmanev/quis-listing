package com.manev.quislisting.web.rest.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.QuisListingApp;
import com.manev.quislisting.domain.*;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.builder.DlListingBuilder;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.*;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
import com.manev.quislisting.web.rest.DlContentFieldResourceTest;
import com.manev.quislisting.web.rest.GenericResourceTest;
import com.manev.quislisting.web.rest.TestUtil;
import com.manev.quislisting.web.rest.taxonomy.DlCategoryResourceIntTest;
import com.manev.quislisting.web.rest.taxonomy.DlLocationResourceIntTest;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_DL_LISTINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlListingResourceTest extends GenericResourceTest {

    private static final String SHOULD_NOT_BE_THIS_NAME = "SHOULD_NOT_BE_THIS_NAME";
    private static final long TRANSLATION_GROUP_ID_SHOULD_NOT_BE_THIS = 10000L;
    private static final String LANGUAGE_CODE_SHOULD_NOT_BE_THIS = "LANGUAGE_CODE_SHOULD_NOT_BE_THIS";
    private static final String SOURCE_LANGUAGE_CODE_SHOULD_NOT_BE_THIS = "SOURCE_LANGUAGE_CODE_SHOULD_NOT_BE_THIS";
    private static final String UPDATE_CONTENT = "UPDATE_CONTENT";
    private static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    private static final String DEFAULT_CONTENT = "DEFAULT_CONTENT";
    private static final String DEFAULT_NAME = "default_title";
    private static final String DEFAULT_LANGUAGE_CODE = "en";
    private static final Timestamp DEFAULT_CREATED = Timestamp.valueOf(LocalDateTime.parse("2007-12-03T10:15:30"));
    private static final Timestamp DEFAULT_MODIFIED = Timestamp.valueOf(LocalDateTime.parse("2007-12-03T10:15:30"));
    private static final DlListing.Status DEFAULT_STATUS = DlListing.Status.DRAFT;
    private static Clock clock = Clock.systemUTC();
    private static final Timestamp ZONED_DATE_TIME_SHOULD_NOT_BE_THIS = new Timestamp(clock.millis());
    private static Boolean DEFAULT_APPROVED = Boolean.FALSE;
    @Autowired
    private DlListingService dlListingService;

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private DlListingRepository dlListingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private DlCategoryRepository dlCategoryRepository;

    @Autowired
    private DlLocationRepository dlLocationRepository;

    @Autowired
    private DlContentFieldRepository dlContentFieldRepository;

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

    private MockMvc restDlListingMockMvc;

    private DlListing dlListing;
    private DlCategory dlCategory;
    private DlLocation dlLocation;
    private DlContentField dlPhoneCF;
    private DlContentField dlHeightCF;

    public static DlListing createEntity() {
        return DlListingBuilder.aDlListing()
                .withTitle(DEFAULT_TITLE)
                .withContent(DEFAULT_CONTENT)
                .withName(DEFAULT_NAME)
                .withCreated(DEFAULT_CREATED)
                .withModified(DEFAULT_MODIFIED)
                .withStatus(DEFAULT_STATUS)
                .withApproved(DEFAULT_APPROVED)
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode(DEFAULT_LANGUAGE_CODE)
                        .withTranslationGroup(new TranslationGroup())
                        .build())
                .build();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlListingResource dlListingResource = new DlListingResource(dlListingService, localeResolver);
        this.restDlListingMockMvc = MockMvcBuilders.standaloneSetup(dlListingResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        // setup dlListing
        dlListing = createEntity();
        dlListing.setUser(userRepository.findOneByLogin("user").orElse(null));

        // setup dl category availability
        dlCategory = DlCategoryResourceIntTest.createEntity();

        // setup dl location availability
        dlLocation = DlLocationResourceIntTest.createEntity();

        // setup dl content fields
        dlHeightCF = DlContentFieldResourceTest.createField(DlContentField.Type.NUMBER, "Height", 1,
                Collections.singleton(dlCategory));
        dlHeightCF.qlString(new QlString().languageCode("en").context("dl-content-field").name("dl-content-field-" + DlContentField.Type.NUMBER + "-" + dlHeightCF.getName()).value(dlHeightCF.getName()).status(0));
        dlPhoneCF = DlContentFieldResourceTest.createField(DlContentField.Type.NUMBER, "Phone", 2,
                Collections.singleton(dlCategory));
        dlPhoneCF.qlString(new QlString().languageCode("en").context("dl-content-field").name("dl-content-field-" + DlContentField.Type.NUMBER + "-" + dlPhoneCF.getName()).value(dlPhoneCF.getName()).status(0));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void getAllDlListings() throws Exception {
        // Initialize the database
        dlCategoryRepository.saveAndFlush(dlCategory);
        Optional<User> user = userRepository.findOneByLogin("user");
        dlListing.setUser(user.orElse(null));
        dlListing.setDlCategories(new HashSet<DlCategory>() {{
            add(dlCategory);
        }});
        dlListingRepository.saveAndFlush(dlListing);

        // Get all the navMenus
        ResultActions resultActions = restDlListingMockMvc.perform(get(RESOURCE_API_DL_LISTINGS + "?sort=id,desc&languageCode=en"));
        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dlListing.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].dlCategories.[*].name").value(hasItem(DlCategoryResourceIntTest.DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].dlCategories.[*].slug").value(hasItem(DlCategoryResourceIntTest.DEFAULT_SLUG)));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void createDlListing() throws Exception {
        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();
        dlCategoryRepository.saveAndFlush(dlCategory);
        dlLocationRepository.saveAndFlush(dlLocation);
        dlContentFieldRepository.saveAndFlush(dlHeightCF);
        dlContentFieldRepository.saveAndFlush(dlPhoneCF);

        DlListingDTO dlListingDTO = DlListingDTOBuilder.aDlListingDTO()
                .withTitle(DEFAULT_TITLE)
                .withContent(DEFAULT_CONTENT)
                .withName(SHOULD_NOT_BE_THIS_NAME)
                .withCreated(ZONED_DATE_TIME_SHOULD_NOT_BE_THIS)
                .withModified(ZONED_DATE_TIME_SHOULD_NOT_BE_THIS)
                .withAuthor(new UserDTO(10000L, "some_login", "some first name", "some last name"))
                .withLanguageCode(LANGUAGE_CODE_SHOULD_NOT_BE_THIS)
                .withSourceLanguageCode(SOURCE_LANGUAGE_CODE_SHOULD_NOT_BE_THIS)
                .withTranslationGroupId(TRANSLATION_GROUP_ID_SHOULD_NOT_BE_THIS)
                .addTranslation(TranslationDTOBuilder.aTranslationDTO()
                        .withId(1000L)
                        .build())
                .withStatus(DlListing.Status.PUBLISH_REQUEST)
                .addDlCategory(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory))
                .addDlLocation(dlLocationMapper.dlLocationToDlLocationDTO(dlLocation))
                .addDlListingField(new DlListingFieldDTO().id(dlHeightCF.getId()).value("180"))
                .addDlListingField(new DlListingFieldDTO().id(dlPhoneCF.getId()).value("+123 456 555"))
                .withExpirationDate("2020-01-01")
                .build();


        restDlListingMockMvc.perform(post(RESOURCE_API_DL_LISTINGS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlListingDTO)))
                .andExpect(status().isCreated());

        // Validate the DlListing in the database
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        Optional<User> optionalUser = userRepository.findOneByLogin("user");
        User user = optionalUser.orElse(null);

        DlListing dlListingSaved = dlListingList.get(dlListingList.size() - 1);
        assertThat(dlListingSaved.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(dlListingSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlListingSaved.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(dlListingSaved.getStatus()).isEqualTo(DlListing.Status.DRAFT);
        assertThat(dlListingSaved.getTranslation().getLanguageCode()).isEqualTo(user.getLangKey());
        assertThat(dlListingSaved.getTranslation().getSourceLanguageCode()).isNull();
        assertThat(dlListingSaved.getTranslation().getTranslationGroup().getId()).isNotEqualTo(TRANSLATION_GROUP_ID_SHOULD_NOT_BE_THIS);
        assertThat(dlListingSaved.getDlCategories().iterator().next().getId()).isEqualTo(dlCategory.getId());
        assertThat(dlListingSaved.getDlListingLocationRels().iterator().next().getDlLocation().getId()).isEqualTo(dlLocation.getId());
        assertThat(dlListingSaved.getUser().getId()).isEqualTo(user.getId());
        assertThat(dlListingSaved.getCreated()).isNotEqualTo(ZONED_DATE_TIME_SHOULD_NOT_BE_THIS);
        assertThat(dlListingSaved.getModified()).isNotEqualTo(ZONED_DATE_TIME_SHOULD_NOT_BE_THIS);
        assertThat(dlListingSaved.getDlAttachments()).isNull();

        Set<DlListingContentFieldRel> dlListingContentFieldRels = dlListingSaved.getDlListingContentFieldRels();
        dlListingContentFieldRels.stream().filter(dlContentFieldRelationship ->
                dlContentFieldRelationship.getDlContentField().getId().equals(dlHeightCF.getId()))
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
        dlCategoryRepository.saveAndFlush(dlCategory);
        dlLocationRepository.saveAndFlush(dlLocation);
        dlContentFieldRepository.saveAndFlush(dlHeightCF);
        dlContentFieldRepository.saveAndFlush(dlPhoneCF);

        DlListingContentFieldRel dlListingContentFieldRelHeight = new DlListingContentFieldRel();
        dlListingContentFieldRelHeight.setDlContentField(dlHeightCF);
        dlListingContentFieldRelHeight.setDlListing(dlListing);
        dlListingContentFieldRelHeight.setValue("180");
        dlListing.addDlContentFieldRelationships(dlListingContentFieldRelHeight);

        DlListingContentFieldRel dlListingContentFieldRelPhone = new DlListingContentFieldRel();
        dlListingContentFieldRelPhone.setDlContentField(dlPhoneCF);
        dlListingContentFieldRelPhone.setDlListing(dlListing);
        dlListingContentFieldRelPhone.setValue("+123 456 666");
        dlListing.addDlContentFieldRelationships(dlListingContentFieldRelHeight);

        dlListingRepository.saveAndFlush(dlListing);

        DlListingDTO createdDlListingDTO = dlListingMapper.dlListingToDlListingDTO(dlListing, null);

        // making updates to the dlListing so at the end object is updated and published
        createdDlListingDTO.setContent(UPDATE_CONTENT);
        createdDlListingDTO.setDlCategories(Collections.singletonList(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory)));
        createdDlListingDTO.setDlLocations(Collections.singletonList(dlLocationMapper.dlLocationToDlLocationDTO(dlLocation)));
        createdDlListingDTO.getDlListingFields().clear();
        createdDlListingDTO.addDlListingField(new DlListingFieldDTO().id(dlHeightCF.getId()).value("190"));
        createdDlListingDTO.addDlListingField(new DlListingFieldDTO().id(dlPhoneCF.getId()).value("+123 456 666"));

        // make the put request for updating the listing
        restDlListingMockMvc.perform(put(RESOURCE_API_DL_LISTINGS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdDlListingDTO)))
                .andExpect(status().isOk());

        // make da asserts
        List<DlListing> dlListingList = dlListingRepository.findAll();
        assertThat(dlListingList).hasSize(databaseSizeBeforeCreate + 1);

        DlListing dlListingSaved = dlListingList.get(dlListingList.size() - 1);
        assertThat(dlListingSaved.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(dlListingSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlListingSaved.getContent()).isEqualTo(UPDATE_CONTENT);
        assertThat(dlListingSaved.getStatus()).isEqualTo(DlListing.Status.DRAFT);
        assertThat(dlListingSaved.getTranslation().getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
        assertThat(dlListingSaved.getDlCategories().iterator().next().getId()).isEqualTo(dlCategory.getId());
        assertThat(dlListingSaved.getDlListingLocationRels().iterator().next().getDlLocation().getId()).isEqualTo(dlLocation.getId());

        Set<DlListingContentFieldRel> dlListingContentFieldRels = dlListingSaved.getDlListingContentFieldRels();
        dlListingContentFieldRels.stream().filter(dlContentFieldRelationship ->
                dlContentFieldRelationship.getDlContentField().getId().equals(dlHeightCF.getId()))
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
        dlListingDTO.setTitle(DEFAULT_TITLE);
        dlListingDTO.setLanguageCode(DEFAULT_LANGUAGE_CODE);

        MvcResult mvcResult = restDlListingMockMvc.perform(post(RESOURCE_API_DL_LISTINGS)
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
        restDlListingMockMvc.perform(put(RESOURCE_API_DL_LISTINGS + "/publish")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdDlListingDTO)))
                .andExpect(status().isOk());

        DlListing publishedDlListing = dlListingRepository.findOne(createdDlListingDTO.getId());
        assertThat(publishedDlListing.getStatus()).isEqualTo(DlListing.Status.PUBLISH_REQUEST);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void uploadAttachmentToDlListing() throws Exception {
        int databaseSizeBeforeCreate = dlListingRepository.findAll().size();

        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setTitle(DEFAULT_TITLE);
        dlListingDTO.setLanguageCode(DEFAULT_LANGUAGE_CODE);

        MvcResult mvcResult = restDlListingMockMvc.perform(post(RESOURCE_API_DL_LISTINGS)
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

        // make the upload
        AttachmentDTO[] uploadedAttachmentDTOs = doFileUpload(createdDlListingDTO.getId());
        assertThat(uploadedAttachmentDTOs).hasSize(1);
        AttachmentDTO attachmentDTO = uploadedAttachmentDTOs[0];

        // test removal of attachments
        MvcResult mvcResultDelete = restDlListingMockMvc.perform(delete(RESOURCE_API_DL_LISTINGS + "/" + createdDlListingDTO.getId() + "/attachments/" + attachmentDTO.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsStringFromDelete = mvcResultDelete.getResponse().getContentAsString();
        DlListingDTO deletedAttachmentDlListingDTO = new ObjectMapper().readValue(contentAsStringFromDelete,
                DlListingDTO.class);
        assertThat(deletedAttachmentDlListingDTO.getAttachments()).isNull();

        // verify it also in database
        DlListing updatedDlListing = dlListingRepository.findOne(deletedAttachmentDlListingDTO.getId());
        assertThat(updatedDlListing.getDlAttachments()).isEmpty();
    }

    private AttachmentDTO[] doFileUpload(Long id) throws Exception {
        String contentType = Files.probeContentType(imageFile.toPath());

        MockMultipartFile multipartFile =
                new MockMultipartFile("files[]", imageFile.getName(), contentType, new FileInputStream(imageFile));

        ResultActions resultActions = this.restDlListingMockMvc.perform(fileUpload(RESOURCE_API_DL_LISTINGS + "/" + id + "/upload")
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
        dlListingRepository.saveAndFlush(dlListing);

        Language lanEn = new Language().code("en").active(true).englishName("English");
        Language lanBg = new Language().code("bg").active(true).englishName("Bulgarian");
        Language lanRo = new Language().code("ro").active(true).englishName("Romanian");
        Language lanRu = new Language().code("ru").active(true).englishName("Russian");
        languageRepository.saveAndFlush(lanEn);
        languageRepository.saveAndFlush(lanBg);
        languageRepository.saveAndFlush(lanRo);
        languageRepository.saveAndFlush(lanRu);

        // Get active languages
        restDlListingMockMvc.perform(get(RESOURCE_API_DL_LISTINGS + "/active-languages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].code").value(hasItem("en")))
                .andExpect(jsonPath("$.[*].englishName").value(hasItem("English")))
                .andExpect(jsonPath("$.[*].count").value(hasItem(1)));
    }

}
