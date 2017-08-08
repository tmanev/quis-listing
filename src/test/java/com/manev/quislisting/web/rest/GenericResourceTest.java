package com.manev.quislisting.web.rest;

import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.service.storage.components.StoreComponent;
import com.manev.quislisting.service.util.AttachmentUtil;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

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
    private StoreComponent storeComponent;

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
    public void clearJcrRepoSaves() throws IOException {
        for (DlAttachment attachment : attachmentsToBeDeletedFromJcrInAfter) {
            storeComponent.removeInRepository(AttachmentUtil.getFilePaths(attachment));
        }
    }

}
