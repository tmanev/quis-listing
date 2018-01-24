package com.manev.quislisting.config;

import org.hibernate.cache.jcache.JCacheRegionFactory;
import org.hibernate.cache.spi.CacheDataDescription;

import javax.cache.Cache;
import java.util.Properties;

/**
 * Extends the default {@code JCacheRegionFactory} but makes sure all caches are already existing to prevent spontaneous
 * creation of badly configured caches (e.g. {@code new MutableConfiguration()}.
 */
public class NoDefaultJCacheRegionFactory extends JCacheRegionFactory {

    @Override
    protected Cache<Object, Object> createCache(String regionName, Properties properties, CacheDataDescription
            metadata) {
        throw new IllegalStateException("All Hibernate caches should be created upfront. " +
                "Please update CacheConfiguration.java to add " + regionName);
    }
}