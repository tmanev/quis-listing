package com.manev.quislisting.service.storage.components;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.QlImageFile;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class StoreComponent {

    private final Logger log = LoggerFactory.getLogger(StoreComponent.class);
    private QuisListingProperties quisListingProperties;

    public StoreComponent(QuisListingProperties quisListingProperties) {
        this.quisListingProperties = quisListingProperties;
    }

    public AttachmentDTO storeInRepository(QlImageFile qlImageFile) throws IOException {
        File pathDirForOriginal = getPathDirForOriginal();
        BufferedImage bufferedImage = qlImageFile.getBufferedImage();
        File fileNode = createFileNode(QlFileUtils.generateFileName(qlImageFile.getOriginalFilename()), pathDirForOriginal, bufferedImage);

        AttachmentDTO attachmentDTO = createAttachmentDTO(qlImageFile.getOriginalFilename(), fileNode.getName());

        AttachmentMetadata attachmentMetadata = new AttachmentMetadata();
        AttachmentMetadata.DetailSize detailSize = new AttachmentMetadata.DetailSize();
        detailSize.setFile(extractPathForUrl(fileNode.getPath().replace("\\", "/").split(quisListingProperties.getAttachmentStoragePath())[1]));
        detailSize.setWidth(bufferedImage.getWidth());
        detailSize.setHeight(bufferedImage.getHeight());
        detailSize.setSize(fileNode.length());
        detailSize.setMimeType(qlImageFile.getContentType());
        attachmentMetadata.setDetail(detailSize);

        attachmentDTO.setAttachmentMetadata(attachmentMetadata);

        return attachmentDTO;
    }

    private String extractPathForUrl(String urlPath) {
        return urlPath;
    }

    public AttachmentMetadata.ImageResizeMeta storeResizedImage(File pathDirForStorage, String originalFilename, String contentType, String imageSizeType, BufferedImage resizedImage) throws IOException {
        if (resizedImage != null) {
            File fileNode = createFileNode(QlFileUtils.generateFileName(originalFilename), pathDirForStorage, resizedImage);

            AttachmentMetadata.ImageResizeMeta imageResizeMeta = new AttachmentMetadata.ImageResizeMeta();
            imageResizeMeta.setName(imageSizeType);
            AttachmentMetadata.DetailSize detail = new AttachmentMetadata.DetailSize();
            detail.setFile(extractPathForUrl(fileNode.getPath().replace("\\", "/").split(quisListingProperties.getAttachmentStoragePath())[1]));
            detail.setMimeType(contentType);
            detail.setWidth(resizedImage.getWidth());
            detail.setHeight(resizedImage.getHeight());
            detail.setSize(fileNode.length());
            imageResizeMeta.setDetail(detail);
            return imageResizeMeta;
        }
        return null;
    }

    private AttachmentDTO createAttachmentDTO(String originalFilename, String fileNameSlug) {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setFileName(originalFilename);
        attachmentDTO.setFileNameSlug(fileNameSlug);
        return attachmentDTO;
    }

    private File getPathDirForStorage() {
        String pathStr = getPathFromCurrentDate();

        // check if file path exists
        return new File(quisListingProperties.getAttachmentStoragePath() + File.separator + pathStr);
    }

    private File getPathDirForOriginal() {
        String pathStr = getPathFromCurrentDate();

        // check if file path exists
        return new File(quisListingProperties.getAttachmentStoragePath() + File.separator + pathStr + File.separator + "originals");
    }

    private String getPathFromCurrentDate() {
        DateTime dateTime = new DateTime();
        String yearStr = String.valueOf(dateTime.getYear());
        String monthOfYearStr = String.format("%02d", dateTime.getMonthOfYear());
        String dayOfMonthStr = String.format("%02d", dateTime.getDayOfMonth());
        return yearStr + File.separator + monthOfYearStr + File.separator + dayOfMonthStr;
    }

    private File createFileNode(String fileName, File pathToStore, BufferedImage bufferedImage) throws IOException {
        if (pathToStore.exists()) {
            // store file
            File checkedFileName = new File(pathToStore, fileName);

            writeStream(bufferedImage, FilenameUtils.getExtension(checkedFileName.getName()), checkedFileName);

            return checkedFileName;
        } else {
            // create and store file
            Path directories = Files.createDirectories(pathToStore.toPath());

            String extension = FilenameUtils.getExtension(fileName);
            File file = new File(directories.toFile(), fileName);

            writeStream(bufferedImage, extension, file);
            return file;
        }
    }

    private void writeStream(BufferedImage resizedImage, String extension, File file) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, extension.isEmpty() ? "jpg" : extension, os);
        Files.copy(new ByteArrayInputStream(os.toByteArray()), file.toPath());
    }

    public void removeInRepository(List<String> filePaths) {
        for (String filePath : filePaths) {
            removeInRepository(filePath);
        }
    }

    public void removeInRepository(String filePath) {
        File file = new File(quisListingProperties.getAttachmentStoragePath() + File.separator + filePath);
        if (file.exists()) {
            try {
                Files.delete(Paths.get(file.toURI()));
            } catch (IOException e) {
                log.error("Failed to remove file: {}", filePath, e);
            }
        }
    }

    public AttachmentStreamResource getResource(String absPath) throws IOException {
        AttachmentStreamResource attachmentStreamResource;

        File file = new File(quisListingProperties.getAttachmentStoragePath() + File.separator + absPath);
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
        attachmentStreamResource = new AttachmentStreamResource(inputStreamResource, Files.probeContentType(file.toPath()),
                file.getName());

        return attachmentStreamResource;
    }

    public File getFile(String path) {
        return new File(quisListingProperties.getAttachmentStoragePath() + File.separator + path);
    }
}
