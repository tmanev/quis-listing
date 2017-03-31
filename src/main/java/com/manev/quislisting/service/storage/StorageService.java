package com.manev.quislisting.service.storage;

import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.storage.components.StoreComponent;
import com.manev.quislisting.service.util.ImageResizer2Util;
import com.manev.quislisting.service.util.ImageWatermarkUtil;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StorageService {

    public static final String DL_MEDIUM = "dl-medium";
    public static final String DL_BIG = "dl-big";
    public static final String DL_THUMBNAIL = "dl-thumbnail";
    public static Map<String, String> DL_SCALE_IMAGE_SIZES = new HashMap<String, String>() {
        {
            put(DL_THUMBNAIL, "180x180");
            put(DL_MEDIUM, "300x300");
            put(DL_BIG, "900x900");
        }
    };
    private StoreComponent storeComponent;

    public StorageService(StoreComponent storeComponent) {
        this.storeComponent = storeComponent;
    }

    public AttachmentStreamResource loadAsResource(String filename) {
        try {
            return storeComponent.getResource(filename);
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AttachmentDTO store(MultipartFile file) throws IOException, RepositoryException {
        InputStream watermarkStream = this.getClass().getClassLoader()
                .getResourceAsStream("images/ql-logo-01-50x50.png");
        BufferedImage inputWatermarked = ImageWatermarkUtil.addImageWatermark(watermarkStream, file.getInputStream());

        Map<String, BufferedImage> resizedImages = new HashMap<>();
        for (String key : DL_SCALE_IMAGE_SIZES.keySet()) {
            String size = DL_SCALE_IMAGE_SIZES.get(key);
            String[] sizesArr = size.split("x");
            Integer width = Integer.valueOf(sizesArr[0]);
            Integer height = Integer.valueOf(sizesArr[1]);
            if (inputWatermarked.getWidth() >= width || inputWatermarked.getHeight() >= height) {
                BufferedImage resizedBufferedImage = ImageResizer2Util.resizeImage(inputWatermarked, width, height, false);
                resizedImages.put(key, resizedBufferedImage);
            }
        }

        return storeComponent.storeInRepository(file, inputWatermarked, resizedImages);
    }

    public void delete(List<String> filePaths) throws IOException, RepositoryException {
        storeComponent.removeInRepository(filePaths);
    }
}
