package com.manev.quislisting.service;

import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.service.util.SlugUtil;
import com.manev.quislisting.web.rest.taxonomy.DlLocationResourceIntTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DlLocationTestComponent {

    @Autowired
    private DlLocationRepository dlLocationRepository;

    public DlLocation initDlLocation() {
        DlLocation dlLocation = DlLocationResourceIntTest.createEntity();
        return dlLocationRepository.saveAndFlush(dlLocation);
    }

    public DlLocation initDlLocation(String name) {
        DlLocation dlLocation = DlLocationResourceIntTest.createEntity();
        dlLocation.setName(name);
        dlLocation.setSlug(SlugUtil.slugify(name));
        return dlLocationRepository.saveAndFlush(dlLocation);
    }

}
