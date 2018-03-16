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
    private final Social social = new Social();
    private final Twitter twitter = new Twitter();
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

    public void setAttachmentStoragePath(final String attachmentStoragePath) {
        this.attachmentStoragePath = attachmentStoragePath;
    }

    public String getGeoLocationDbPath() {
        return geoLocationDbPath;
    }

    public void setGeoLocationDbPath(final String geoLocationDbPath) {
        this.geoLocationDbPath = geoLocationDbPath;
    }

    public QuisListingProperties.Async getAsync() {
        return this.async;
    }

    public Cache getCache() {
        return cache;
    }

    public Social getSocial() {
        return social;
    }

    public Twitter getTwitter() {
        return twitter;
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

                public void setSecret(final String secret) {
                    this.secret = secret;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(final long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(final long tokenValidityInSecondsForRememberMe) {
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

        public void setDisplayOnActiveProfiles(final String[] displayOnActiveProfiles) {
            this.displayOnActiveProfiles = displayOnActiveProfiles;
        }
    }

    public static class Mail {
        private String from = "";

        public String getFrom() {
            return this.from;
        }

        public void setFrom(final String from) {
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

        public void setCorePoolSize(final int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return this.maxPoolSize;
        }

        public void setMaxPoolSize(final int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return this.queueCapacity;
        }

        public void setQueueCapacity(final int queueCapacity) {
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

            public void setTimeToLiveSeconds(final int timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public long getMaxEntries() {
                return maxEntries;
            }

            public void setMaxEntries(final long maxEntries) {
                this.maxEntries = maxEntries;
            }
        }
    }

    public static class Social {
        private String redirectAfterSignIn = "";

        public String getRedirectAfterSignIn() {
            return this.redirectAfterSignIn;
        }

        public void setRedirectAfterSignIn(final String redirectAfterSignIn) {
            this.redirectAfterSignIn = redirectAfterSignIn;
        }

    }

    public static class Twitter {
        private String accessToken = "";
        private String accessTokenSecret = "";

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(final String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessTokenSecret() {
            return accessTokenSecret;
        }

        public void setAccessTokenSecret(final String accessTokenSecret) {
            this.accessTokenSecret = accessTokenSecret;
        }
    }

}
