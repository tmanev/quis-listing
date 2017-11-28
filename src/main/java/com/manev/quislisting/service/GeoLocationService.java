package com.manev.quislisting.service;

import com.manev.quislisting.config.QuisListingProperties;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.exception.MissingConfigurationException;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class GeoLocationService {

    private static final Logger log = LoggerFactory.getLogger(GeoLocationService.class);
    private final QuisListingProperties quisListingProperties;

    private DatabaseReader dbReader;

    private QlConfigService qlConfigService;

    public GeoLocationService(QuisListingProperties quisListingProperties, QlConfigService qlConfigService) {
        this.quisListingProperties = quisListingProperties;
        this.qlConfigService = qlConfigService;
    }

    public String countryIsoFromIp(String ip) {
        String remoteIp = ip;
        String manualIp = getManualIp();
        if (manualIp != null) {
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
            log.info("Looking for ip address {}", remoteIp);
            CountryResponse countryResponse = dbReader.country(ipAddress);
            if (countryResponse != null && countryResponse.getCountry() != null) {
                return countryResponse.getCountry().getIsoCode().toLowerCase();
            }
        } catch (GeoIp2Exception e) {
            log.error("Country location cannot be read", e);
        } catch (UnknownHostException e) {
            log.error("Unknown host error", e);
        } catch (IOException e) {
            log.error("IO error", e);
        }

        return null;
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
