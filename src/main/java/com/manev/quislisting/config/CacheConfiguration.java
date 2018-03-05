package com.manev.quislisting.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(QuisListingProperties quisListingProperties) {
        QuisListingProperties.Cache.Ehcache ehcache =
                quisListingProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                        ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                        .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.User.class.getName() + ".authorities", jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.qlml.Language.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.qlml.LanguageTranslation.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.qlml.QlString.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.qlml.StringTranslation.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.qlml.QlString.class.getName() + ".stringTranslation", jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.Translation.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.TranslationGroup.class.getName(), jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.StaticPage.class.getName(), jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.QlConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.EmailTemplate.class.getName(), jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.taxonomy.discriminator.DlCategory.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.taxonomy.discriminator.DlCategory.class.getName() + ".children", jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.taxonomy.discriminator.DlCategory.class.getName() + ".dlListings", jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.taxonomy.discriminator.DlLocation.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.taxonomy.discriminator.DlLocation.class.getName() + ".children", jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.taxonomy.discriminator.NavMenu.class.getName(), jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.post.discriminator.DlListing.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.post.discriminator.DlListing.class.getName() + ".dlCategories", jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.post.discriminator.DlListing.class.getName() + ".dlAttachments", jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.post.discriminator.DlListing.class.getName() + ".dlListingContentFieldRels", jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.post.discriminator.DlListing.class.getName() + ".dlListingLocationRels", jcacheConfiguration);

            cm.createCache(com.manev.quislisting.domain.DlListingLocationRel.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.DlAttachment.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.DlAttachment.class.getName() + ".dlAttachmentResizes", jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.DlAttachmentResize.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.DlListingContentFieldRel.class.getName(), jcacheConfiguration);
            cm.createCache(com.manev.quislisting.domain.DlListingContentFieldRel.class.getName() + ".dlContentFieldItems", jcacheConfiguration);

            // Services
            cm.createCache("findDlContentFieldsByCategoryId", jcacheConfiguration);


        };
    }

    /**
     * Use by Spring Security, to get events from Hazelcast.
     *
     * @return the session registry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}
