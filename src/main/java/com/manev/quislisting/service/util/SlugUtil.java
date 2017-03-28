package com.manev.quislisting.service.util;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtil {
    public static String getFileNameSlug(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = FilenameUtils.getBaseName(originalFilename);
        Slugify slugify = new Slugify();
        return slugify.slugify(fileName) + (extension.isEmpty() ? "" : "." + slugify.slugify(extension));
    }
}
