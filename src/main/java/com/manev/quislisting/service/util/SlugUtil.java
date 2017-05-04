package com.manev.quislisting.service.util;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

public class SlugUtil {
    public static String getFileNameSlug(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = FilenameUtils.getBaseName(originalFilename);
        Slugify slugify = new Slugify();
        return slugify.slugify(fileName) + (extension.isEmpty() ? "" : "." + slugify.slugify(extension));
    }

    public static String metaContentFieldId(Long id) {
        return "content_field_" + id;
    }
}
