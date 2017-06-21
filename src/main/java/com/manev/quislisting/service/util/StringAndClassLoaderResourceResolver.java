package com.manev.quislisting.service.util;

import org.apache.commons.io.IOUtils;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.Context;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.util.ClassLoaderUtils;
import org.thymeleaf.util.Validate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class StringAndClassLoaderResourceResolver implements IResourceResolver {


    public StringAndClassLoaderResourceResolver() {
        super();
    }


    public String getName() {
        return getClass().getName().toUpperCase();
    }


    public InputStream getResourceAsStream(final TemplateProcessingParameters params, final String resourceName) {
        Validate.notNull(resourceName, "Resource name cannot be null");
        if (StringContext.class.isAssignableFrom(params.getContext().getClass())) {
            String content = ((StringContext) params.getContext()).getContent();
            return IOUtils.toInputStream(content, StandardCharsets.UTF_8);
        }
        return ClassLoaderUtils.getClassLoader(ClassLoaderResourceResolver.class).getResourceAsStream(resourceName);
    }

    public static class StringContext extends Context {

        private final String content;

        public StringContext(String content) {
            this.content = content;
        }

        public StringContext(String content, Locale locale) {
            super(locale);
            this.content = content;
        }

        public StringContext(String content, Locale locale, Map<String, ?> variables) {
            super(locale, variables);
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}