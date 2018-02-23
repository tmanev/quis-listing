package com.manev.quislisting.service;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.exception.MissingConfigurationException;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.springframework.util.StringUtils;

@Service
@Scope("singleton")
public class GeoLocationService {

    private static final Logger log = LoggerFactory.getLogger(GeoLocationService.class);
    private final QuisListingProperties quisListingProperties;

    private DatabaseReader dbReader;

    private QlConfigService qlConfigService;

    private LanguageRepository languageRepository;

    public GeoLocationService(QuisListingProperties quisListingProperties, QlConfigService qlConfigService, LanguageRepository languageRepository) {
        this.quisListingProperties = quisListingProperties;
        this.qlConfigService = qlConfigService;
        this.languageRepository = languageRepository;
    }

    public String countryIsoFromIp(String ip) {
        String remoteIp = ip;
        String manualIp = getManualIp();
        if (!StringUtils.isEmpty(manualIp)) {
            remoteIp = manualIp;
        }

        if (dbReader == null) {
            try {
                dbReader = initializeDbReader();
            } catch (IOException e) {
                log.error("DB reader could not be initialized", e);
            }
        }

        try {
            InetAddress ipAddress = InetAddress.getByName(remoteIp);
            log.debug("Looking for ip address {}", remoteIp);
            CountryResponse countryResponse = dbReader.country(ipAddress);

            if (countryResponse != null && countryResponse.getCountry() != null) {
                String langKeyByIp = countryResponse.getCountry().getIsoCode().toLowerCase();
                log.debug("Language by ip address {}", langKeyByIp);
                List<Language> allByActive = languageRepository.findAllByActive(Boolean.TRUE);

                if (isExistingInActiveLanguages(countryResponse, allByActive)) {
                    return langKeyByIp;
                } else {
                    log.debug("Language not active setting default en");
                    return "en";
                }

            }
        } catch (AddressNotFoundException ex) {
            log.warn("IP address {} not found in database", remoteIp);
        } catch (GeoIp2Exception e) {
            log.error("Country location cannot be read", e);
        } catch (UnknownHostException e) {
            log.error("Unknown host error", e);
        } catch (IOException e) {
            log.error("IO error", e);
        }

        return null;
    }

    private Boolean isExistingInActiveLanguages(CountryResponse countryResponse, List<Language> allByActive) {
        for (Language language : allByActive) {
            if (language.getCode().equalsIgnoreCase(countryResponse.getCountry().getIsoCode().toLowerCase())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private String getManualIp() {
        try {
            QlConfig oneByKey = qlConfigService.findOneByKey("manual-ip-address");
            return oneByKey.getValue();
        } catch (MissingConfigurationException ex) {
            // nothing happens
        }
        return null;
    }

    private File initializeDbFile() {
        return new File(quisListingProperties.getGeoLocationDbPath());
    }

    private DatabaseReader initializeDbReader() throws IOException {
        return new DatabaseReader.Builder(initializeDbFile()).build();
    }
}
