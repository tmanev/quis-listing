package com.manev.quislisting.web.rest;

import com.manev.quislisting.config.JcrConfiguration;
import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.service.util.AttachmentUtil;
import com.manev.quislisting.web.rest.post.AttachmentResourceTest;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericResourceTest {
    protected File imageFile;
    protected List<Attachment> attachmentsToBeDeletedFromJcrInAfter;

    @Autowired
    private JcrConfiguration jcrConfiguration;

    private static File createFile() throws URISyntaxException {
        URL resource = AttachmentResourceTest.class.getResource("/images/small fish.jpg");
        return new File(resource.toURI());
    }

    @Before
    public void initGenericTest() throws URISyntaxException {
        this.imageFile = createFile();
        this.attachmentsToBeDeletedFromJcrInAfter = new ArrayList<>();
    }

    @After
    public void clearJcrRepoSaves() throws IOException, RepositoryException {
        for (Attachment attachment : attachmentsToBeDeletedFromJcrInAfter) {

            List<String> filePaths = AttachmentUtil.getFilePaths(attachment);

            Session session = jcrConfiguration.getSession();
            for (String filePath : filePaths) {
                if (session.itemExists(filePath)) {
                    Node node = session.getNode(filePath);
                    node.remove();
                }
            }
            session.save();
        }
    }

}
