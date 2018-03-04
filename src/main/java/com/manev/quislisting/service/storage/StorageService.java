package com.manev.quislisting.service.storage;

import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.exception.AttachmentStreamResourceException;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.QlImageFile;
import com.manev.quislisting.service.storage.components.StoreComponent;
import com.manev.quislisting.service.util.ImageResizeUtil;
import com.manev.quislisting.service.util.ImageWatermarkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

@Component
public class StorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    private static final String DL_SMALL_SIZE = "333x222";
    private static final String DL_ORIGINAL = "original";
    private static final String DL_SMALL = "small";
    private StoreComponent storeComponent;

    public StorageService(StoreComponent storeComponent) {
        this.storeComponent = storeComponent;
    }

    public AttachmentStreamResource loadAsResource(String filename) {
        try {
            return storeComponent.getResource(filename);
        } catch (IOException e) {
            logger.error("Resource {} cannot be retrieved", filename);
            throw new AttachmentStreamResourceException("Resource cannot be retrieved", e);
        }
    }

    public File getFile(String path) {
        return storeComponent.getFile(path);
    }

    public AttachmentDTO store(QlImageFile qlImageFile) throws IOException {
        AttachmentDTO attachmentDTO = storeComponent.storeInRepository(qlImageFile);
        storeResizes(attachmentDTO, qlImageFile);

        return attachmentDTO;
    }

    public void storeResizes(AttachmentDTO attachmentDTO, QlImageFile qlImageFile) throws IOException {
        AttachmentMetadata attachmentMetadata = attachmentDTO.getAttachmentMetadata();

        InputStream watermarkStream = this.getClass().getClassLoader()
                .getResourceAsStream("images/ql-logo-01-50x50.png");
        BufferedImage inputWatermarked = ImageWatermarkUtil.addImageWatermark(watermarkStream, qlImageFile.getBufferedImage());

        ResizedImages resizedImages = new ResizedImages();
        resizedImages.setSmall(resizeImage(DL_SMALL_SIZE, inputWatermarked));
        resizedImages.setOriginal(inputWatermarked);

        File file = storeComponent.getFile(attachmentDTO.getAttachmentMetadata().getDetail().getFile());
        Path path = file.toPath();
        Path parent = path.getParent().getParent();
        File parentDir = parent.toFile();

        AttachmentMetadata.ImageResizeMeta smallImageResizeMeta = storeComponent.storeResizedImage(parentDir, qlImageFile.getOriginalFilename(), qlImageFile.getContentType(), DL_SMALL, resizedImages.getSmall());
        AttachmentMetadata.ImageResizeMeta originalImageResizeMeta = storeComponent.storeResizedImage(parentDir, qlImageFile.getOriginalFilename(), qlImageFile.getContentType(), DL_ORIGINAL, resizedImages.getOriginal());

        attachmentMetadata.setSmallImageResizeMeta(smallImageResizeMeta);
        attachmentMetadata.setOriginalImageResizeMeta(originalImageResizeMeta);
    }

    private BufferedImage resizeImage(String size, BufferedImage inputWatermarked) {
        String[] sizesArr = size.split("x");
        Integer width = Integer.valueOf(sizesArr[0]);
        Integer height = Integer.valueOf(sizesArr[1]);
        if (inputWatermarked.getWidth() >= width || inputWatermarked.getHeight() >= height) {
            return ImageResizeUtil.resizeImage(inputWatermarked, width, height);
        }
        return null;
    }

    public void delete(List<String> filePaths) {
        storeComponent.removeInRepository(filePaths);
    }

    public void delete(String filePath) {
        storeComponent.removeInRepository(filePath);
    }
}
