package com.manev.quislisting.service.storage.components;

import com.github.slugify.Slugify;
import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.storage.ResizedImages;
import com.manev.quislisting.service.util.SlugUtil;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

    private static final String DL_MEDIUM = "medium";
    private static final String DL_LARGE = "large";
    private static final String DL_SMALL = "small";
    private QuisListingProperties quisListingProperties;

    public StoreComponent(QuisListingProperties quisListingProperties) {
        this.quisListingProperties = quisListingProperties;
    }

    public AttachmentDTO storeInRepository(MultipartFile file, BufferedImage inputWatermarked, ResizedImages resizedImages) throws IOException {

        String fileNameSlug = SlugUtil.getFileNameSlug(file.getOriginalFilename());

        File fileNode = createFileNode(fileNameSlug, inputWatermarked, file.getContentType());

        AttachmentDTO attachmentDTO = createAttachmentDTO(file.getOriginalFilename(), fileNode.getName());

        AttachmentMetadata attachmentMetadata = new AttachmentMetadata();
        AttachmentMetadata.DetailSize detailSize = new AttachmentMetadata.DetailSize();
        detailSize.setFile(extractPathForUrl(fileNode.getPath().replace("\\", "/").split(quisListingProperties.getAttachmentStoragePath())[1]));
        detailSize.setWidth(inputWatermarked.getWidth());
        detailSize.setHeight(inputWatermarked.getHeight());
        detailSize.setSize(fileNode.length());
        detailSize.setMimeType(file.getContentType());
        attachmentMetadata.setDetail(detailSize);

        AttachmentMetadata.ImageResizeMeta smallImageResizeMeta = storeImage(fileNode.getName(), file.getContentType(), DL_SMALL, resizedImages.getSmall());
        AttachmentMetadata.ImageResizeMeta mediumImageResizeMeta = storeImage(fileNode.getName(), file.getContentType(), DL_MEDIUM, resizedImages.getMedium());
        AttachmentMetadata.ImageResizeMeta largeImageResizeMeta = storeImage(fileNode.getName(), file.getContentType(), DL_LARGE, resizedImages.getBig());

        attachmentMetadata.setSmallImageResizeMeta(smallImageResizeMeta);
        attachmentMetadata.setMediumImageResizeMeta(mediumImageResizeMeta);
        attachmentMetadata.setLargeImageResizeMeta(largeImageResizeMeta);

        attachmentDTO.setAttachmentMetadata(attachmentMetadata);

        return attachmentDTO;
    }

    private String extractPathForUrl(String urlPath) {
        return urlPath;
    }

    private AttachmentMetadata.ImageResizeMeta storeImage(String fileNameSlug, String contentType, String key, BufferedImage resizedImage) throws IOException {
        if (resizedImage != null) {
            String extension = FilenameUtils.getExtension(fileNameSlug);
            String fileName = FilenameUtils.getBaseName(fileNameSlug);
            String fileNameSlugResize = fileName + "-" + key + (extension.isEmpty() ? "" : "." + new Slugify().slugify(extension));
            File fileNode = createFileNode(fileNameSlugResize, resizedImage, contentType);

            AttachmentMetadata.ImageResizeMeta imageResizeMeta = new AttachmentMetadata.ImageResizeMeta();
            imageResizeMeta.setName(key);
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

    private File createFileNode(String fileName, BufferedImage resizedImage, String contentType) throws IOException {
        DateTime dateTime = new DateTime();
        String yearStr = String.valueOf(dateTime.getYear());
        String monthOfYearStr = String.format("%02d", dateTime.getMonthOfYear());
        String dayOfMonthStr = String.format("%02d", dateTime.getDayOfMonth());
        String pathStr = yearStr + File.separator + monthOfYearStr + File.separator + dayOfMonthStr;

        // check if file path exists
        File pathDir = new File(quisListingProperties.getAttachmentStoragePath() + File.separator + pathStr);
        try {
            if (pathDir.exists()) {
                // store file
                File checkedFileName = checkFileName(fileName, pathDir);

                writeStream(resizedImage, FilenameUtils.getExtension(checkedFileName.getName()), checkedFileName, contentType);

                return checkedFileName;
            } else {
                // create and store file

                Path directories = Files.createDirectories(pathDir.toPath());

                String extension = FilenameUtils.getExtension(fileName);
                File file = new File(directories.toFile(), fileName);

                writeStream(resizedImage, extension, file, contentType);
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void writeStream(BufferedImage resizedImage, String extension, File file, String contentType) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, extension.isEmpty() ? "jpg" : extension, os);
        Files.copy(new ByteArrayInputStream(os.toByteArray()), file.toPath());
    }

    private File checkFileName(String fileName, File parent) {
        String checkedFileName;
        String baseName = FilenameUtils.getBaseName(fileName);
        int counter = 0;
        File file = new File(parent, fileName);

        while (file.exists()) {
            String extension = FilenameUtils.getExtension(fileName);
            checkedFileName = baseName + "-" + (counter + 1) + (extension.isEmpty() ? "" : "." + extension);
            file = new File(parent, checkedFileName);
            counter++;
        }
        return file;
    }

    public void removeInRepository(List<String> filePaths) {
        for (String filePath : filePaths) {
            File file = new File(quisListingProperties.getAttachmentStoragePath() + File.separator + filePath);
            if (file.exists()) {
                try {
                    Files.delete(Paths.get(file.toURI()));
                } catch (IOException e) {
                    log.error("Failed to remove file: {}", filePath, e);
                }
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
}
