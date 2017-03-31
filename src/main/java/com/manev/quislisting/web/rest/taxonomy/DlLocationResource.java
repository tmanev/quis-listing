package com.manev.quislisting.web.rest.taxonomy;

import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import com.manev.quislisting.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_DL_LOCATIONS;

@RestController
@RequestMapping(RESOURCE_API_ADMIN_DL_LOCATIONS)
public class DlLocationResource {

    private static final String ENTITY_NAME = "DlLocation";

    private final Logger log = LoggerFactory.getLogger(DlLocationResource.class);
    private final DlLocationService dlLocationService;

    public DlLocationResource(DlLocationService dlLocationService) {
        this.dlLocationService = dlLocationService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DlLocationDTO> createDlLocation(@RequestBody DlLocationDTO dlLocationDTO) throws URISyntaxException {
        log.debug("REST request to save DlLocationDTO : {}", dlLocationDTO);
        if (dlLocationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlLocationDTO result = dlLocationService.save(dlLocationDTO);
        return ResponseEntity.created(new URI(RESOURCE_API_ADMIN_DL_LOCATIONS + "/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<DlLocationDTO> updateDlLocation(@RequestBody DlLocationDTO dlLocationDTO) throws URISyntaxException {
        log.debug("REST request to update DlLocationDTO : {}", dlLocationDTO);
        if (dlLocationDTO.getId() == null) {
            return createDlLocation(dlLocationDTO);
        }
        DlLocationDTO result = dlLocationService.save(dlLocationDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlLocationDTO.getId().toString()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<DlLocationDTO>> getAllDlLocations(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of DlLocationDTO");
        Page<DlLocationDTO> page = dlLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_ADMIN_DL_LOCATIONS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DlLocationDTO> getDlLocation(@PathVariable Long id) {
        log.debug("REST request to get DlLocationDTO : {}", id);
        DlLocationDTO dlLocationDTO = dlLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlLocationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDlLocation(@PathVariable Long id) {
        log.debug("REST request to delete DlLocationDTO : {}", id);
        dlLocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/active-languages")
    public List<ActiveLanguageDTO> getActiveLanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return dlLocationService.findAllActiveLanguages();
    }

}
