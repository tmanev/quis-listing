package com.manev.quislisting.config;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.jcr.RepositoryException;
import java.io.IOException;

@Configuration
public class JcrConfiguration {

    @Autowired
    ApplicationContext ctx;

    @Bean
    public RepositoryConfig jcrRepoConfig() throws ConfigurationException, IOException {
        Resource resource = ctx.getResource("classpath:repository.xml");
        return RepositoryConfig.create(resource.getFile().getPath(), "./ql-repo");
    }

    @Bean
    public RepositoryImpl repository() throws RepositoryException, IOException {
        return RepositoryImpl.create(jcrRepoConfig());
    }

}
