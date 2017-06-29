package com.manev.quislisting.config;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.jcr.RepositoryException;
import java.io.IOException;

@Configuration
public class JcrConfiguration {

    private final ApplicationContext ctx;

    private final QuisListingProperties quisListingProperties;

    public JcrConfiguration(ApplicationContext ctx, QuisListingProperties quisListingProperties) {
        this.ctx = ctx;
        this.quisListingProperties = quisListingProperties;
    }

    @Bean
    public RepositoryConfig jcrRepoConfig() throws ConfigurationException, IOException {
        Resource resource = ctx.getResource("classpath:repository.xml");
        return RepositoryConfig.create(resource.getFile().getPath(), quisListingProperties.getJcrRepository().getHome());
    }

    @Bean
    public RepositoryImpl repository() throws RepositoryException, IOException {
        return RepositoryImpl.create(jcrRepoConfig());
    }

}
