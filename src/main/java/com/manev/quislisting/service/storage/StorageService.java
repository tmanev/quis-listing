package com.manev.quislisting.service.storage;

import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.exception.AttachmentStreamResourceException;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.storage.components.StoreComponent;
import com.manev.quislisting.service.util.ImageResizeUtil;
import com.manev.quislisting.service.util.ImageWatermarkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class StorageService {

    private static final String DL_MEDIUM = "dl-medium";
    private static final String DL_BIG = "dl-big";
    public static final String DL_THUMBNAIL = "dl-thumbnail";
    private static final Map<String, String> DL_SCALE_IMAGE_SIZES = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(StorageService.class);
    private StoreComponent storeComponent;

    public StorageService(StoreComponent storeComponent) {
        this.storeComponent = storeComponent;
        DL_SCALE_IMAGE_SIZES.put(DL_THUMBNAIL, "180x180");
        DL_SCALE_IMAGE_SIZES.put(DL_MEDIUM, "300x300");
        DL_SCALE_IMAGE_SIZES.put(DL_BIG, "900x900");
    }

    public AttachmentStreamResource loadAsResource(String filename) {
        try {
            return storeComponent.getResource(filename);
        } catch (RepositoryException | IOException e) {
            logger.error("Resource {} cannot be retrieved", filename);
            throw new AttachmentStreamResourceException("Resource cannot be retrieved", e);
        }
    }

    public AttachmentDTO store(MultipartFile file) throws IOException, RepositoryException {
        InputStream watermarkStream = this.getClass().getClassLoader()
                .getResourceAsStream("images/ql-logo-01-50x50.png");
        BufferedImage inputWatermarked = ImageWatermarkUtil.addImageWatermark(watermarkStream, file.getInputStream());

        Map<String, BufferedImage> resizedImages = new HashMap<>();
        Set<Map.Entry<String, String>> entries = DL_SCALE_IMAGE_SIZES.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String size = entry.getValue();

            String[] sizesArr = size.split("x");
            Integer width = Integer.valueOf(sizesArr[0]);
            Integer height = Integer.valueOf(sizesArr[1]);
            if (inputWatermarked.getWidth() >= width || inputWatermarked.getHeight() >= height) {
                BufferedImage resizedBufferedImage = ImageResizeUtil.resizeImage(inputWatermarked, width, height);
                resizedImages.put(key, resizedBufferedImage);
            }
        }

        return storeComponent.storeInRepository(file, inputWatermarked, resizedImages);
    }

    public void delete(List<String> filePaths) throws IOException, RepositoryException {
        storeComponent.removeInRepository(filePaths);
    }
}
