package com.manev.quislisting.service.storage.components;

import com.github.slugify.Slugify;
import com.manev.quislisting.config.JcrConfiguration;
import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.storage.ResizedImages;
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
import java.util.List;

import static com.manev.quislisting.service.storage.StorageService.*;

@Component
public class StoreComponent {

    private JcrConfiguration jcrConfiguration;

    public StoreComponent(JcrConfiguration jcrConfiguration) {
        this.jcrConfiguration = jcrConfiguration;
    }

    public AttachmentDTO storeInRepository(MultipartFile file, BufferedImage inputWatermarked, ResizedImages resizedImages) throws IOException, RepositoryException {
        AttachmentDTO attachmentDTO;
        Session session = jcrConfiguration.getSession();

        String fileNameSlug = SlugUtil.getFileNameSlug(file.getOriginalFilename());

        ZonedDateTime currentDateTime = ZonedDateTime.now();
        Node fileNode = createFileNode(fileNameSlug, session);
        Node resourceNode = createResourceNode(file.getContentType(), FilenameUtils.getExtension(fileNameSlug), inputWatermarked, session, fileNode, currentDateTime);
        attachmentDTO = createAttachmentDTO(file.getOriginalFilename(), fileNode.getName(), file.getContentType(), currentDateTime);

        AttachmentMetadata attachmentMetadata = new AttachmentMetadata();
        AttachmentMetadata.DetailSize detailSize = new AttachmentMetadata.DetailSize();
        detailSize.setFile(fileNode.getPath());
        detailSize.setWidth(inputWatermarked.getWidth());
        detailSize.setHeight(inputWatermarked.getHeight());
        detailSize.setSize(resourceNode.getProperty(JcrConstants.JCR_DATA).getBinary().getSize());
        attachmentMetadata.setDetail(detailSize);

        AttachmentMetadata.ImageResizeMeta thumbnailImageResizeMeta = storeImage(fileNameSlug, file.getContentType(), session, currentDateTime, DL_THUMBNAIL, resizedImages.getThumbnail());
        AttachmentMetadata.ImageResizeMeta mediumImageResizeMeta = storeImage(fileNameSlug, file.getContentType(), session, currentDateTime, DL_MEDIUM, resizedImages.getMedium());
        AttachmentMetadata.ImageResizeMeta bigImageResizeMeta = storeImage(fileNameSlug, file.getContentType(), session, currentDateTime, DL_BIG, resizedImages.getBig());

        attachmentMetadata.setThumbnailImageResizeMeta(thumbnailImageResizeMeta);
        attachmentMetadata.setMediumImageResizeMeta(mediumImageResizeMeta);
        attachmentMetadata.setBigImageResizeMeta(bigImageResizeMeta);

        attachmentDTO.setAttachmentMetadata(attachmentMetadata);

        session.save();

        return attachmentDTO;
    }

    private AttachmentMetadata.ImageResizeMeta storeImage(String fileNameSlug, String contentType, Session session, ZonedDateTime currentDateTime, String key, BufferedImage resizedImage) throws RepositoryException, IOException {
        if (resizedImage != null) {
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
            return imageResizeMeta;
        }
        return null;
    }

    private AttachmentDTO createAttachmentDTO(String originalFilename, String fileNameSlug, String contentType, ZonedDateTime currentDateTime) {
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


    private Node createFileNode(String fileName, Session session) throws RepositoryException {
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

        String checkedFileName = checkFileName(fileName, monthOfYearNode);
        return monthOfYearNode.addNode(checkedFileName, JcrConstants.NT_FILE);
    }

    private String checkFileName(String fileName, Node monthOfYearNode) throws RepositoryException {
        String checkedFileName = fileName;
        String baseName = FilenameUtils.getBaseName(fileName);
        int counter = 0;
        while (monthOfYearNode.hasNode(fileName)) {
            String extension = FilenameUtils.getExtension(fileName);
            checkedFileName = baseName + "-" + (counter + 1) + (extension.isEmpty() ? "" : "." + extension);
            counter++;
        }
        return checkedFileName;
    }

    public void removeInRepository(List<String> filePaths) throws IOException, RepositoryException {
        Session session = jcrConfiguration.getSession();

        for (String filePath : filePaths) {
            session.removeItem(filePath);
        }

        session.save();
    }

    public AttachmentStreamResource getResource(String absPath) throws RepositoryException, IOException {
        AttachmentStreamResource attachmentStreamResource;
        Session session = jcrConfiguration.getSession();

        Node fileNode = session.getNode(absPath);
        Node contentNode = fileNode.getNode(Node.JCR_CONTENT);
        Property jcrDataProperty = contentNode.getProperty(JcrConstants.JCR_DATA);
        Property mimeType = contentNode.getProperty(JcrConstants.JCR_MIMETYPE);

        Binary binary = jcrDataProperty.getBinary();
        InputStreamResource inputStreamResource = new InputStreamResource(binary.getStream());
        attachmentStreamResource = new AttachmentStreamResource(inputStreamResource, mimeType.getString(),
                fileNode.getName());

        session.save();

        return attachmentStreamResource;
    }
}
