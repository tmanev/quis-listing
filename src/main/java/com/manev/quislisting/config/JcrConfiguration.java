package com.manev.quislisting.config;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.io.IOException;

@Configuration
public class JcrConfiguration {

    private static final String ADMIN = "admin";
    private final ApplicationContext ctx;
    private final QuisListingProperties quisListingProperties;

    private RepositoryConfig repositoryConfig;
    private RepositoryImpl repositoryImpl;
    private Session session;

    public JcrConfiguration(ApplicationContext ctx, QuisListingProperties quisListingProperties) throws IOException, RepositoryException {
        this.ctx = ctx;
        this.quisListingProperties = quisListingProperties;
        jcrRepoConfig();
    }

//    @Bean
    public RepositoryConfig jcrRepoConfig() throws RepositoryException, IOException {
        Resource resource = ctx.getResource("classpath:repository.xml");
        this.repositoryConfig = RepositoryConfig.create(resource.getFile().getPath(), quisListingProperties.getJcrRepository().getHome());
        this.repositoryImpl = RepositoryImpl.create(repositoryConfig);
        this.session = repositoryImpl.login(new SimpleCredentials(ADMIN, ADMIN.toCharArray()));

        return repositoryConfig;
    }

//    @Bean
    public RepositoryImpl repository() throws RepositoryException, IOException {
        return repositoryImpl;
    }

    public RepositoryImpl getRepositoryImpl() {
        return repositoryImpl;
    }

    public void setRepositoryImpl(RepositoryImpl repositoryImpl) {
        this.repositoryImpl = repositoryImpl;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
