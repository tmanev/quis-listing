package com.manev.quislisting.config;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.springframework.context.annotation.Configuration;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

@Configuration
public class JcrConfiguration {

    private static final String ADMIN = "admin";
    private final QuisListingProperties quisListingProperties;

    private RepositoryImpl repositoryImpl;
    private Session session;

    public JcrConfiguration(QuisListingProperties quisListingProperties) throws RepositoryException {
        this.quisListingProperties = quisListingProperties;
        jcrRepoConfig();
    }

    private void jcrRepoConfig() throws RepositoryException {
        RepositoryConfig repositoryConfig = RepositoryConfig.create(this.getClass().getResource("/repository.xml").getPath(),
                quisListingProperties.getJcrRepository().getHome());
        this.repositoryImpl = RepositoryImpl.create(repositoryConfig);
        this.session = repositoryImpl.login(new SimpleCredentials(ADMIN, ADMIN.toCharArray()));
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
