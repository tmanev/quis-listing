package com.manev.quislisting.web.rest;

import com.manev.quislisting.service.DlContentFieldGroupService;
import com.manev.quislisting.service.dto.DlContentFieldGroupDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.manev.quislisting.web.rest.RestRouter.Rest.DlContentFieldGroup.BASE;

@RestController
@RequestMapping(BASE)
public class DlContentFieldGroupResource {

    private static final String ENTITY_NAME = "DlContentField";

    private final Logger log = LoggerFactory.getLogger(DlCategoryResource.class);
    private final DlContentFieldGroupService dlContentFieldGroupService;

    public DlContentFieldGroupResource(DlContentFieldGroupService dlContentFieldGroupService) {
        this.dlContentFieldGroupService = dlContentFieldGroupService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DlContentFieldGroupDTO> createDlContentFieldGroup(@RequestBody DlContentFieldGroupDTO dlContentFieldGroupDTO) throws URISyntaxException {
        log.debug("REST request to save DlContentFieldGroupDTO : {}", dlContentFieldGroupDTO);
        if (dlContentFieldGroupDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlContentFieldGroupDTO result = dlContentFieldGroupService.save(dlContentFieldGroupDTO);
        return ResponseEntity.created(new URI(BASE + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<DlContentFieldGroupDTO> updateDlContentFieldGroup(@RequestBody DlContentFieldGroupDTO dlContentFieldGroupDTO) throws URISyntaxException {
        log.debug("REST request to update DlContentFieldGroupDTO : {}", dlContentFieldGroupDTO);
        if (dlContentFieldGroupDTO.getId() == null) {
            return createDlContentFieldGroup(dlContentFieldGroupDTO);
        }
        DlContentFieldGroupDTO result = dlContentFieldGroupService.save(dlContentFieldGroupDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlContentFieldGroupDTO.getId().toString()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<DlContentFieldGroupDTO>> getAllDlContentFieldGroups(Pageable pageable) {
        log.debug("REST request to get a page of DlContentFieldGroupDTO");
        Page<DlContentFieldGroupDTO> page = dlContentFieldGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DlContentFieldGroupDTO> getDlContentFieldGroup(@PathVariable Long id) {
        log.debug("REST request to get DlContentFieldGroupDTO : {}", id);
        DlContentFieldGroupDTO dlContentFieldGroupDTO = dlContentFieldGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlContentFieldGroupDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDlContentFieldGroup(@PathVariable Long id) {
        log.debug("REST request to delete DlContentFieldGroupDTO : {}", id);
        dlContentFieldGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
