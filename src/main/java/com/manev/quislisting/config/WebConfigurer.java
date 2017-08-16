package com.manev.quislisting.config;

import com.manev.quislisting.web.mvc.MyErrorViewResolver;
import com.manev.quislisting.web.mvc.PageNotFoundController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private Environment env;

    private QuisListingProperties quisListingProperties;

    private PageNotFoundController pageNotFoundController;

    public WebConfigurer(Environment env, QuisListingProperties quisListingProperties, PageNotFoundController pageNotFoundController) {
        this.env = env;
        this.quisListingProperties = quisListingProperties;
        this.pageNotFoundController = pageNotFoundController;
    }

    /**
     * Customize the Servlet engine: Mime types, the document root, the cache.
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
        mappings.add("html", "text/html;charset=utf-8");
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);
        // When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
        setLocationForStaticAssets(container);
    }

    private void setLocationForStaticAssets(ConfigurableEmbeddedServletContainer container) {
        File root;
        String prefixPath = resolvePathPrefix();
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            root = new File(prefixPath + "target/www/");
        } else {
            root = new File(prefixPath + "src/main/webapp/");
        }
        if (root.exists() && root.isDirectory()) {
            container.setDocumentRoot(root);
        }
    }

    /**
     * Resolve path prefix to static resources.
     */
    private String resolvePathPrefix() {
        String fullExecutablePath = this.getClass().getResource("").getPath();
        String rootPath = Paths.get(".").toUri().normalize().getPath();
        String extractedPath = fullExecutablePath.replace(rootPath, "");
        int extractionEndIndex = extractedPath.indexOf("target/");
        if (extractionEndIndex <= 0) {
            return "";
        }
        return extractedPath.substring(0, extractionEndIndex);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            String activeProfilesStr = Arrays.toString(env.getActiveProfiles());
            log.info("Web application configuration, using profiles: {}", activeProfilesStr);
        }

        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
            initH2Console(servletContext);
        }
        log.info("Web application fully configured");
    }

    /**
     * Initializes H2 console.
     */
    private void initH2Console(ServletContext servletContext) {
        log.debug("Initialize H2 console");
        ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", new org.h2.server.web.WebServlet());
        h2ConsoleServlet.addMapping("/h2-console/*");
        h2ConsoleServlet.setInitParameter("-properties", "src/main/resources/");
        h2ConsoleServlet.setLoadOnStartup(1);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = quisListingProperties.getCors();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/v2/api-docs", config);
        }
        return new CorsFilter(source);
    }

    @Bean
    public ErrorViewResolver errorViewResolver() {
        return new MyErrorViewResolver(pageNotFoundController);
    }


}
