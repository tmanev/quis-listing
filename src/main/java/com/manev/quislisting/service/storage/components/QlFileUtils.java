package com.manev.quislisting.service.storage.components;

import com.github.slugify.Slugify;
import com.manev.quislisting.service.util.SlugUtil;
import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

public class QlFileUtils {

    private QlFileUtils() {
    }

    public static String generateFileName(String originalFileName) {
        String fileNameSlug = SlugUtil.getFileNameSlug(originalFileName);
        String extension = FilenameUtils.getExtension(fileNameSlug);
        return UUID.randomUUID()
                + (extension.isEmpty() ? "" : "."
                + new Slugify().slugify(extension));
    }

}
