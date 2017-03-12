package com.manev.quislisting.web.rest;

import com.manev.quislisting.service.DlContentFieldService;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.web.rest.taxonomy.DlCategoryResource;
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

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_DL_CONTENT_FIELDS;

@RestController
@RequestMapping(RESOURCE_API_DL_CONTENT_FIELDS)
public class DlContentFieldResource {

    private static final String ENTITY_NAME = "DlContentField";

    private final Logger log = LoggerFactory.getLogger(DlCategoryResource.class);
    private final DlContentFieldService dlContentFieldService;

    public DlContentFieldResource(DlContentFieldService dlContentFieldService) {
        this.dlContentFieldService = dlContentFieldService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DlContentFieldDTO> createDlContentField(@RequestBody DlContentFieldDTO dlContentFieldDTO) throws URISyntaxException {
        log.debug("REST request to save DlCategoryDTO : {}", dlContentFieldDTO);
        if (dlContentFieldDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlContentFieldDTO result = dlContentFieldService.save(dlContentFieldDTO);
        return ResponseEntity.created(new URI(RESOURCE_API_DL_CONTENT_FIELDS + "/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<DlContentFieldDTO> updateDlCategory(@RequestBody DlContentFieldDTO dlContentFieldDTO) throws URISyntaxException {
        log.debug("REST request to update DlContentFieldDTO : {}", dlContentFieldDTO);
        if (dlContentFieldDTO.getId() == null) {
            return createDlContentField(dlContentFieldDTO);
        }
        DlContentFieldDTO result = dlContentFieldService.save(dlContentFieldDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlContentFieldDTO.getId().toString()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<DlContentFieldDTO>> getAllDlContentFields(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of DlContentFieldDTO");
        Page<DlContentFieldDTO> page = dlContentFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_DL_CONTENT_FIELDS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DlContentFieldDTO> getDlCategory(@PathVariable Long id) {
        log.debug("REST request to get DlContentFieldDTO : {}", id);
        DlContentFieldDTO dlContentFieldDTO = dlContentFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlContentFieldDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDlCategory(@PathVariable Long id) {
        log.debug("REST request to delete DlContentFieldDTO : {}", id);
        dlContentFieldService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
