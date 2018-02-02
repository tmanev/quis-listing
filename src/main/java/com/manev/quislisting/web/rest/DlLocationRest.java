package com.manev.quislisting.web.rest;

import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DlLocationRest {

    private final Logger log = LoggerFactory.getLogger(DlLocationRest.class);
    private final DlLocationService dlLocationService;

    public DlLocationRest(DlLocationService dlLocationService) {
        this.dlLocationService = dlLocationService;
    }

    @GetMapping(RestRouter.DlLocation.LIST)
    public ResponseEntity<List<DlLocationDTO>> getAllDlLocations(@RequestParam String parentId,
                                                                 @RequestParam(required = false, defaultValue = "en") String languageCode) {
        log.debug("REST request to get a page of DlLocationDTO");
        return new ResponseEntity<>(dlLocationService.findAllByParentId(Long.valueOf(parentId), languageCode), HttpStatus.OK);
    }

}
