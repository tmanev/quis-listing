package com.manev.quislisting.config;

import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class JcrConfiguration {

    public JcrConfiguration(QuisListingProperties quisListingProperties) {
        File file = new File(quisListingProperties.getAttachmentStoragePath());
        if (!file.isDirectory()) {
            file.mkdir();
        }
    }

}
