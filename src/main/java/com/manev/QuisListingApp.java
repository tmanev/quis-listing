package com.manev;

import com.manev.quislisting.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.util.StringUtils;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class})
public class QuisListingApp {

    private static final Logger log = LoggerFactory.getLogger(QuisListingApp.class);

    private Environment env;

    public QuisListingApp(Environment env) {
        this.env = env;
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(QuisListingApp.class);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (!StringUtils.isEmpty(env.getProperty("server.ssl.key-store"))) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}\n\t" +
                        "External: \t{}://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getActiveProfiles());
    }

    /**
     * Initializes QuisListingApplication.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     */
    @PostConstruct
    public void initApplication() {
        String activeProfilesStr = Arrays.toString(env.getActiveProfiles());
        log.info("Running with Spring profile(s) : {}", activeProfilesStr);
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_TEST)) {
            log.error("You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'test' profiles at the same time.");
        }
    }
}
