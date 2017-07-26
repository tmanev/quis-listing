package com.manev.quislisting.web.rest;

import com.manev.quislisting.config.JcrConfiguration;
import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.service.util.AttachmentUtil;
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
    protected List<DlAttachment> attachmentsToBeDeletedFromJcrInAfter;

    @Autowired
    private JcrConfiguration jcrConfiguration;

    private static File createFile() throws URISyntaxException {
        URL resource = GenericResourceTest.class.getResource("/images/small fish.jpg");
        return new File(resource.toURI());
    }

    @Before
    public void initGenericTest() throws URISyntaxException {
        this.imageFile = createFile();
        this.attachmentsToBeDeletedFromJcrInAfter = new ArrayList<>();
    }

    @After
    public void clearJcrRepoSaves() throws IOException, RepositoryException {
        for (DlAttachment attachment : attachmentsToBeDeletedFromJcrInAfter) {

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
