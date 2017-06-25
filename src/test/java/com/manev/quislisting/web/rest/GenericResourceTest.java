package com.manev.quislisting.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.QuisListingApp;
import com.manev.quislisting.config.JcrConfiguration;
import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.web.rest.post.AttachmentResourceTest;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.jcr.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.manev.quislisting.domain.post.discriminator.Attachment.QL_ATTACHED_FILE;
import static com.manev.quislisting.domain.post.discriminator.Attachment.QL_ATTACHMENT_METADATA;

public abstract class GenericResourceTest {
    protected File imageFile;
    protected List<Attachment> attachmentsToBeDeletedFromJcrInAfter;

    @Autowired
    private JcrConfiguration jcrConfiguration;

    @Before
    public void initGenericTest() throws URISyntaxException {
        this.imageFile = createFile();
        this.attachmentsToBeDeletedFromJcrInAfter = new ArrayList<>();
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

    public static File createFile() throws URISyntaxException {
        URL resource = AttachmentResourceTest.class.getResource("/images/small fish.jpg");
        return new File(resource.toURI());
    }

    protected void setupSecurityContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("admin",
                "admin",
                grantedAuthorities);
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
    }
}
