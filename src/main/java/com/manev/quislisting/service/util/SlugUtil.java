package com.manev.quislisting.service.util;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

public class SlugUtil {

    private SlugUtil() {
        // hide the public constructor
    }

    public static String getFileNameSlug(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = FilenameUtils.getBaseName(originalFilename);
        Slugify slugify = new Slugify();
        return slugify.slugify(fileName) + (extension.isEmpty() ? "" : "." + slugify.slugify(extension));
    }

    public static String slugify(String name) {
        return new Slugify().slugify(name);
    }
}
