package com.manev.quislisting.web.rest.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.QuisListingApp;
import com.manev.quislisting.config.JcrConfiguration;
import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.repository.post.AttachmentRepository;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.AttachmentService;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.mapper.AttachmentMapper;
import com.manev.quislisting.service.storage.StorageService;
import com.manev.quislisting.web.ContentController;
import com.manev.quislisting.web.rest.TestUtil;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
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

import javax.jcr.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.manev.quislisting.domain.post.discriminator.Attachment.QL_ATTACHED_FILE;
import static com.manev.quislisting.domain.post.discriminator.Attachment.QL_ATTACHMENT_METADATA;
import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_ATTACHMENTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class AttachmentResourceTest {

    public static final String TITLE = "small fish";
    public static final String TITLE_UPDATED = "small fish updated";
    private final Logger log = LoggerFactory.getLogger(AttachmentResourceTest.class);

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private StorageService storageService;

    @Autowired
    private JcrConfiguration jcrConfiguration;

    private MockMvc restAttachmentMockMvc;
    private MockMvc contentControllerMockMvc;

    private File imageFile;

    private List<Attachment> attachmentsToBeDeletedFromJcrInAfter;

    private String yearStr;
    private String monthOfYearStr;

    public static File createFile() throws URISyntaxException {
        URL resource = AttachmentResourceTest.class.getResource("/images/small fish.jpg");
        return new File(resource.toURI());
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttachmentResource attachmentResource = new AttachmentResource(attachmentService);
        this.restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
        setupSecurityContext();
    }

    @Before
    public void initTest() throws URISyntaxException {
        attachmentRepository.deleteAll();
        this.imageFile = createFile();
        this.attachmentsToBeDeletedFromJcrInAfter = new ArrayList<>();
        DateTime dateTime = new DateTime();
        this.yearStr = String.valueOf(dateTime.getYear());
        this.monthOfYearStr = String.format("%02d", dateTime.getMonthOfYear());
    }

    @After
    public void clearJcrRepoSaves() throws IOException, RepositoryException {
        for (Attachment attachment : attachmentsToBeDeletedFromJcrInAfter) {
            List<String> filePaths = new ArrayList<>();

            filePaths.add(attachment.getPostMetaValue(QL_ATTACHED_FILE));
            String attachmentMetaStr = attachment.getPostMetaValue(QL_ATTACHMENT_METADATA);
            AttachmentMetadata attachmentMetadata = new ObjectMapper().readValue(attachmentMetaStr, AttachmentMetadata.class);
            List<AttachmentMetadata.ImageResizeMeta> imageResizeMetas = attachmentMetadata.getImageResizeMetas();
            for (AttachmentMetadata.ImageResizeMeta imageResizeMeta : imageResizeMetas) {
                filePaths.add(imageResizeMeta.getDetail().getFile());
            }

            Repository repository = jcrConfiguration.repository();
            Session session = repository.login(
                    new SimpleCredentials("admin", "admin".toCharArray()));
            try {
                for (String filePath : filePaths) {
                    if (session.itemExists(filePath)) {
                        Node node = session.getNode(filePath);
                        node.remove();
                    }
                }
                session.save();
            } finally {
                session.logout();
            }
        }
    }

    @Test
    @Transactional
    public void uploadAttachment() throws Exception {
        Attachment attachment = doFileUpload();

        assertThat(attachment.getName()).isEqualTo("small-fish");
        assertThat(attachment.getTitle()).isEqualTo("small fish");
        assertThat(attachment.getStatus()).isEqualTo(Attachment.Status.TEMP.getValue());
        assertThat(attachment.getMimeType()).isEqualTo("image/jpeg");

        assertThat(attachment.getPostMetaValue(QL_ATTACHED_FILE)).isEqualTo("/" + yearStr + "/" + monthOfYearStr + "/" + "small-fish.jpg");

        String attachmentMetaStr = attachment.getPostMetaValue(QL_ATTACHMENT_METADATA);
        AttachmentMetadata attachmentMetadata = new ObjectMapper().readValue(attachmentMetaStr, AttachmentMetadata.class);
        List<AttachmentMetadata.ImageResizeMeta> imageResizeMetas = attachmentMetadata.getImageResizeMetas();
        assertThat(imageResizeMetas.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void getAttachment() throws Exception {
        // make the upload
        Attachment attachment = doFileUpload();


        List<String> filePaths = new ArrayList<>();
        filePaths.add(attachment.getPostMetaValue(QL_ATTACHED_FILE));

        String attachmentMetaStr = attachment.getPostMetaValue(QL_ATTACHMENT_METADATA);
        AttachmentMetadata attachmentMetadata = new ObjectMapper().readValue(attachmentMetaStr, AttachmentMetadata.class);
        List<AttachmentMetadata.ImageResizeMeta> imageResizeMetas = attachmentMetadata.getImageResizeMetas();
        for (AttachmentMetadata.ImageResizeMeta imageResizeMeta : imageResizeMetas) {
            filePaths.add(imageResizeMeta.getDetail().getFile());
        }

        ContentController contentController = new ContentController(storageService);
        MockMvc contentControllerMockMvc = MockMvcBuilders.standaloneSetup(contentController)
                .build();
        for (String filePath : filePaths) {
            contentControllerMockMvc.perform(get("/content/files" + filePath))
                    .andExpect(status().isOk());
        }

        ResultActions resultActions = restAttachmentMockMvc.perform(get(RESOURCE_API_ADMIN_ATTACHMENTS + "/{id}",
                attachment.getId()));
        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
                .andExpect(jsonPath("$.name").value(attachment.getName()))
                .andExpect(jsonPath("$.title").value(attachment.getTitle()))
                .andExpect(jsonPath("$.mimeType").value(attachment.getMimeType()))
                .andExpect(jsonPath("$.attachmentMetadata.file").value(attachment.getPostMetaValue(QL_ATTACHED_FILE)))
                .andExpect(jsonPath("$.attachmentMetadata.imageResizeMetas.[0].name").value("dl-thumbnail"))
                .andExpect(jsonPath("$.attachmentMetadata.imageResizeMetas.[0].detail.file").value(String.format("/%s/%s/small-fish-180x133.jpg", yearStr, monthOfYearStr)))
                .andExpect(jsonPath("$.attachmentMetadata.imageResizeMetas.[1].name").value("dl-medium"))
                .andExpect(jsonPath("$.attachmentMetadata.imageResizeMetas.[1].detail.file").value(String.format("/%s/%s/small-fish-300x222.jpg", yearStr, monthOfYearStr)))
        ;

    }

    @Test
    @Transactional
    public void deleteAttachment() throws Exception {
        // 1. upload attachment
        Attachment savedEntity = doFileUpload();

        // 2. Delete the attachment
        this.restAttachmentMockMvc.perform(delete(RESOURCE_API_ADMIN_ATTACHMENTS + "/{id}",
                savedEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // 3. verify that Attachment entity is deleted from repository
        assertThat(attachmentRepository.findAll().size()).isEqualTo(0);

        // 4. verify jcr that all generated files are removed from JCR repo
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(savedEntity);
        AttachmentMetadata attachmentMetadata = attachmentDTO.getAttachmentMetadata();
        List<String> filePaths = new ArrayList<>();
        filePaths.add(attachmentMetadata.getFile());
        List<AttachmentMetadata.ImageResizeMeta> imageResizeMetas = attachmentMetadata.getImageResizeMetas();
        for (AttachmentMetadata.ImageResizeMeta imageResizeMeta : imageResizeMetas) {
            filePaths.add(imageResizeMeta.getDetail().getFile());
        }

        Repository repository = jcrConfiguration.repository();
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));
        try {
            for (String filePath : filePaths) {
                assertFalse("File path should not exist: " + filePath, session.nodeExists(filePath));
            }
            session.save();
        } finally {
            session.logout();
        }
    }

    @Test
    @Transactional
    public void updateAttachment() throws Exception {
        Attachment attachment = doFileUpload();
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        assertEquals(TITLE, attachment.getTitle());

        attachment.setTitle(TITLE_UPDATED);
        AttachmentDTO updatedAttachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);

        restAttachmentMockMvc.perform(put(RESOURCE_API_ADMIN_ATTACHMENTS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAttachmentDTO)))
                .andExpect(status().isOk());

        // Validate that Attachment is updated in database
        List<Attachment> all = attachmentRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate);

        Attachment one = attachmentRepository.findOne(updatedAttachmentDTO.getId());
        assertThat(one.getTitle()).isEqualTo(TITLE_UPDATED);
    }

    private Attachment doFileUpload() throws Exception {
        String contentType = Files.probeContentType(imageFile.toPath());

        MockMultipartFile multipartFile =
                new MockMultipartFile("files[]", imageFile.getName(), contentType, new FileInputStream(imageFile));

        this.restAttachmentMockMvc.perform(fileUpload(RESOURCE_API_ADMIN_ATTACHMENTS + "/upload")
                .file(multipartFile))
                .andExpect(status().isCreated());

        // test get call to verify the resource
        List<Attachment> all = attachmentRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        Attachment attachment = all.get(0);
        attachmentsToBeDeletedFromJcrInAfter.add(attachment);

        return attachment;
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

}