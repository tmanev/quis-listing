package com.manev.quislisting.service.storage.components;

import com.github.slugify.Slugify;
import com.manev.quislisting.config.JcrConfiguration;
import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.util.SlugUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.jackrabbit.JcrConstants;
import org.joda.time.DateTime;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.jcr.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StoreComponent {

    private JcrConfiguration jcrConfiguration;

    public StoreComponent(JcrConfiguration jcrConfiguration) {
        this.jcrConfiguration = jcrConfiguration;
    }

    public AttachmentDTO storeInRepository(MultipartFile file, BufferedImage inputWatermarked, Map<String, BufferedImage> resizedImages) throws IOException, RepositoryException {
        AttachmentDTO attachmentDTO;
        Repository repository = jcrConfiguration.repository();
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));

        String fileNameSlug = SlugUtil.getFileNameSlug(file.getOriginalFilename());

        try {
            ZonedDateTime currentDateTime = ZonedDateTime.now();
            Node fileNode = createFileNode(fileNameSlug, session);
            Node resourceNode = createResourceNode(file.getContentType(), FilenameUtils.getExtension(fileNameSlug), inputWatermarked, session, fileNode, currentDateTime);
            attachmentDTO = createAttachmentDTO(file.getOriginalFilename(), fileNode.getName(), file.getContentType(), currentDateTime);

            AttachmentMetadata attachmentMetadata = new AttachmentMetadata();
            attachmentMetadata.setFile(fileNode.getPath());
            attachmentMetadata.setWidth(inputWatermarked.getWidth());
            attachmentMetadata.setHeight(inputWatermarked.getHeight());
            attachmentMetadata.setSize(resourceNode.getProperty(JcrConstants.JCR_DATA).getBinary().getSize());

            List<AttachmentMetadata.ImageResizeMeta> resizedImagesMeta = storeResizedImages(fileNode.getName(), file.getContentType(), resizedImages, session, currentDateTime);
            attachmentMetadata.setImageResizeMetas(resizedImagesMeta);

            attachmentDTO.setAttachmentMetadata(attachmentMetadata);

            session.save();
        } finally {
            session.logout();
        }

        return attachmentDTO;
    }

    private List<AttachmentMetadata.ImageResizeMeta> storeResizedImages(String fileNameSlug,
                                                                        String contentType, Map<String, BufferedImage> resizedImages, Session session, ZonedDateTime currentDateTime) throws IOException, RepositoryException {
        List<AttachmentMetadata.ImageResizeMeta> resizeMetaList = new ArrayList<>();

        for (String key : resizedImages.keySet()) {
            BufferedImage resizedImage = resizedImages.get(key);
            String extension = FilenameUtils.getExtension(fileNameSlug);
            String fileName = FilenameUtils.getBaseName(fileNameSlug);
            String fileNameSlugResize = fileName + "-" + resizedImage.getWidth() + "x" + resizedImage.getHeight() + (extension.isEmpty() ? "" : "." + new Slugify().slugify(extension));
            Node fileNode = createFileNode(fileNameSlugResize, session);
            Node resourceNode = createResourceNode(contentType, extension, resizedImage, session, fileNode, currentDateTime);

            AttachmentMetadata.ImageResizeMeta imageResizeMeta = new AttachmentMetadata.ImageResizeMeta();
            imageResizeMeta.setName(key);
            AttachmentMetadata.DetailSize detail = new AttachmentMetadata.DetailSize();
            detail.setFile(fileNode.getPath());
            detail.setMimeType(contentType);
            detail.setWidth(resizedImage.getWidth());
            detail.setHeight(resizedImage.getHeight());
            detail.setSize(resourceNode.getProperty(JcrConstants.JCR_DATA).getBinary().getSize());
            imageResizeMeta.setDetail(detail);

            resizeMetaList.add(imageResizeMeta);
        }

        return resizeMetaList;
    }

    private AttachmentDTO createAttachmentDTO(String originalFilename, String fileNameSlug, String contentType, ZonedDateTime currentDateTime) throws RepositoryException {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setTitle(FilenameUtils.getBaseName(originalFilename));
        attachmentDTO.setName(FilenameUtils.getBaseName(fileNameSlug));
        attachmentDTO.setMimeType(contentType);
        attachmentDTO.setCreated(currentDateTime);
        attachmentDTO.setModified(currentDateTime);
        return attachmentDTO;
    }

    private Node createResourceNode(String mimeType, String extension, BufferedImage inputWatermarked, Session session, Node fileNode, ZonedDateTime currentDateTime) throws RepositoryException, IOException {
        Node resultNode = fileNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(inputWatermarked, extension.isEmpty() ? "jpg" : extension, os);

        Binary binary = session.getValueFactory().createBinary(new ByteArrayInputStream(os.toByteArray()));
        resultNode.setProperty(JcrConstants.JCR_DATA, binary);
        resultNode.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
        resultNode.setProperty(JcrConstants.JCR_LASTMODIFIED, currentDateTime.format(DateTimeFormatter.ISO_INSTANT));

        return resultNode;
    }


    private Node createFileNode(String fileName, Session session) throws RepositoryException, IOException {
        DateTime dateTime = new DateTime();
        String yearStr = String.valueOf(dateTime.getYear());
        String monthOfYearStr = String.format("%02d", dateTime.getMonthOfYear());

        Node root = session.getRootNode();

        // Store content
        Node yearNode;
        if (root.hasNode(yearStr)) {
            yearNode = root.getNode(yearStr);
        } else {
            yearNode = root.addNode(yearStr);
        }
        Node monthOfYearNode;
        if (yearNode.hasNode(monthOfYearStr)) {
            monthOfYearNode = yearNode.getNode(monthOfYearStr);
        } else {
            monthOfYearNode = yearNode.addNode(monthOfYearStr);
        }

        Node fileNode;

        int counter = 0;

        String baseName = FilenameUtils.getBaseName(fileName);
        while (monthOfYearNode.hasNode(fileName)) {
            String extension = FilenameUtils.getExtension(fileName);
            fileName = baseName + "-" + (counter + 1) + (extension.isEmpty() ? "" : "." + extension);
            counter++;
        }

        fileNode = monthOfYearNode.addNode(fileName, JcrConstants.NT_FILE);

        return fileNode;
    }

    public void removeInRepository(List<String> filePaths) throws IOException, RepositoryException {
        Repository repository = jcrConfiguration.repository();
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));

        try {
            for (String filePath : filePaths) {
                session.removeItem(filePath);
            }
            session.save();
        } finally {
            session.logout();
        }
    }

    public AttachmentStreamResource getResource(String absPath) throws RepositoryException, IOException {
        AttachmentStreamResource attachmentStreamResource;
        Repository repository = jcrConfiguration.repository();
        Session session = repository.login(
                new SimpleCredentials("admin", "admin".toCharArray()));

        try {
            Node fileNode = session.getNode(absPath);
            Node contentNode = fileNode.getNode(Node.JCR_CONTENT);
            Property jcrDataProperty = contentNode.getProperty(JcrConstants.JCR_DATA);
            Property mimeType = contentNode.getProperty(JcrConstants.JCR_MIMETYPE);

            Binary binary = jcrDataProperty.getBinary();
            InputStreamResource inputStreamResource = new InputStreamResource(binary.getStream());
            attachmentStreamResource = new AttachmentStreamResource(inputStreamResource, mimeType.getString(),
                    fileNode.getName());

            session.save();
        } finally {
            session.logout();
        }

        return attachmentStreamResource;
    }
}
