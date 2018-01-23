package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.DlContentFieldService;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import com.manev.quislisting.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
public class DlContentFieldAdminRest {

    private static final String ENTITY_NAME = "DlContentField";

    private final Logger log = LoggerFactory.getLogger(DlCategoryAdminRest.class);
    private final DlContentFieldService dlContentFieldService;

    public DlContentFieldAdminRest(DlContentFieldService dlContentFieldService) {
        this.dlContentFieldService = dlContentFieldService;
    }

    @PostMapping(AdminRestRouter.DlContentFields.LIST)
    public ResponseEntity<DlContentFieldDTO> createDlContentField(@RequestBody DlContentFieldDTO dlContentFieldDTO) throws URISyntaxException {
        log.debug("REST request to save DlContentFieldDTO : {}", dlContentFieldDTO);
        if (dlContentFieldDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlContentFieldDTO result = dlContentFieldService.save(dlContentFieldDTO);
        return ResponseEntity.created(new URI(AdminRestRouter.DlContentFields.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.DlContentFields.LIST)
    public ResponseEntity<DlContentFieldDTO> updateDlContentField(@RequestBody DlContentFieldDTO dlContentFieldDTO) throws URISyntaxException {
        log.debug("REST request to update DlContentFieldDTO : {}", dlContentFieldDTO);
        if (dlContentFieldDTO.getId() == null) {
            return createDlContentField(dlContentFieldDTO);
        }
        DlContentFieldDTO result = dlContentFieldService.save(dlContentFieldDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlContentFieldDTO.getId().toString()))
                .body(result);
    }

    @GetMapping(AdminRestRouter.DlContentFields.LIST)
    public ResponseEntity<List<DlContentFieldDTO>> getAllDlContentFields(Pageable pageable) {
        log.debug("REST request to get a page of DlContentFieldDTO");
        Page<DlContentFieldDTO> page = dlContentFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.DlContentFields.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.DlContentFields.DETAIL)
    public ResponseEntity<DlContentFieldDTO> getDlContentField(@PathVariable Long id) {
        log.debug("REST request to get DlContentFieldDTO : {}", id);
        DlContentFieldDTO dlContentFieldDTO = dlContentFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlContentFieldDTO));
    }

    @DeleteMapping(AdminRestRouter.DlContentFields.DETAIL)
    public ResponseEntity<Void> deleteDlContentField(@PathVariable Long id) {
        log.debug("REST request to delete DlContentFieldDTO : {}", id);
        dlContentFieldService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
