package com.manev.quislisting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Properties specific to Quis Listing.
 * <p>
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "quislisting", ignoreUnknownFields = false)
public class QuisListingProperties {

    private final Security security = new Security();
    private final Ribbon ribbon = new Ribbon();
    private final QuisListingProperties.Mail mail = new QuisListingProperties.Mail();
    private final CorsConfiguration cors = new CorsConfiguration();
    private final QuisListingProperties.Async async = new QuisListingProperties.Async();
    private String attachmentStoragePath;
    private String geoLocationDbPath;
    private final Cache cache = new Cache();

    public Security getSecurity() {
        return security;
    }

    public Ribbon getRibbon() {
        return ribbon;
    }

    public QuisListingProperties.Mail getMail() {
        return this.mail;
    }

    public CorsConfiguration getCors() {
        return this.cors;
    }

    public String getAttachmentStoragePath() {
        return attachmentStoragePath;
    }

    public void setAttachmentStoragePath(String attachmentStoragePath) {
        this.attachmentStoragePath = attachmentStoragePath;
    }

    public String getGeoLocationDbPath() {
        return geoLocationDbPath;
    }

    public void setGeoLocationDbPath(String geoLocationDbPath) {
        this.geoLocationDbPath = geoLocationDbPath;
    }

    public QuisListingProperties.Async getAsync() {
        return this.async;
    }

    public Cache getCache() {
        return cache;
    }

    public static class Security {

        private final Authentication authentication = new Authentication();

        public Authentication getAuthentication() {
            return authentication;
        }

        public static class Authentication {

            private final Jwt jwt = new Jwt();

            public Jwt getJwt() {
                return jwt;
            }

            public static class Jwt {

                private String secret;

                private long tokenValidityInSeconds = 1800;

                private long tokenValidityInSecondsForRememberMe = 2592000;

                public String getSecret() {
                    return secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }
    }

    public static class Ribbon {

        private String[] displayOnActiveProfiles;

        public String[] getDisplayOnActiveProfiles() {
            return displayOnActiveProfiles;
        }

        public void setDisplayOnActiveProfiles(String[] displayOnActiveProfiles) {
            this.displayOnActiveProfiles = displayOnActiveProfiles;
        }
    }

    public static class Mail {
        private String from = "";

        public String getFrom() {
            return this.from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

    }

    public static class Async {
        private int corePoolSize = 2;
        private int maxPoolSize = 50;
        private int queueCapacity = 10000;

        public Async() {
            // default constructor
        }

        public int getCorePoolSize() {
            return this.corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return this.maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return this.queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class Cache {

        private final Ehcache ehcache = new Ehcache();

        public Ehcache getEhcache() {
            return ehcache;
        }

        public static class Ehcache {

            private int timeToLiveSeconds = 3600;

            private long maxEntries = 100;

            public int getTimeToLiveSeconds() {
                return timeToLiveSeconds;
            }

            public void setTimeToLiveSeconds(int timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public long getMaxEntries() {
                return maxEntries;
            }

            public void setMaxEntries(long maxEntries) {
                this.maxEntries = maxEntries;
            }
        }
    }

}
