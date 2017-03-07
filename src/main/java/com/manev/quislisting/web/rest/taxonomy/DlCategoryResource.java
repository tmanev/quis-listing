package com.manev.quislisting.web.rest.taxonomy;

import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
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
import java.util.Map;
import java.util.Optional;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_DL_CATEGORIES;
import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_DL_LOCATIONS;

@RestController
@RequestMapping(RESOURCE_API_ADMIN_DL_CATEGORIES)
public class DlCategoryResource {

    private static final String ENTITY_NAME = "DlCategory";

    private final Logger log = LoggerFactory.getLogger(DlCategoryResource.class);
    private final DlCategoryService dlCategoryService;

    public DlCategoryResource(DlCategoryService dlCategoryService) {
        this.dlCategoryService = dlCategoryService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DlCategoryDTO> createDlCategory(@RequestBody DlCategoryDTO dlCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save DlCategoryDTO : {}", dlCategoryDTO);
        if (dlCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlCategoryDTO result = dlCategoryService.save(dlCategoryDTO);
        return ResponseEntity.created(new URI(RESOURCE_API_DL_LOCATIONS + "/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<DlCategoryDTO> updateDlCategory(@RequestBody DlCategoryDTO dlCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update DlCategoryDTO : {}", dlCategoryDTO);
        if (dlCategoryDTO.getId() == null) {
            return createDlCategory(dlCategoryDTO);
        }
        DlCategoryDTO result = dlCategoryService.save(dlCategoryDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlCategoryDTO.getId().toString()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<DlCategoryDTO>> getAllDlCategories(Pageable pageable, @RequestParam Map<String, String> allRequestParams)
            throws URISyntaxException {
        log.debug("REST request to get a page of DlCategoryDTO");
        Page<DlCategoryDTO> page = dlCategoryService.findAll(pageable, allRequestParams);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_DL_LOCATIONS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DlCategoryDTO> getDlCategory(@PathVariable Long id) {
        log.debug("REST request to get DlCategoryDTO : {}", id);
        DlCategoryDTO dlCategoryDTO = dlCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlCategoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDlCategory(@PathVariable Long id) {
        log.debug("REST request to delete DlCategoryDTO : {}", id);
        dlCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/active-languages")
    public List<ActiveLanguageDTO> getActivelanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return dlCategoryService.findAllActiveLanguages();
    }

}
