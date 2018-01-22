package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.DlContentFieldItemService;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import com.manev.quislisting.web.rest.AdminRestRouter;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class DlContentFieldItemResource {

    private static final String ENTITY_NAME = "DlContentFieldItem";

    private final Logger log = LoggerFactory.getLogger(DlCategoryResource.class);
    private final DlContentFieldItemService dlContentFieldItemService;

    public DlContentFieldItemResource(DlContentFieldItemService dlContentFieldItemService) {
        this.dlContentFieldItemService = dlContentFieldItemService;
    }

    @PostMapping(AdminRestRouter.DlContentFields.DlContentFieldItem.LIST)
    public ResponseEntity<DlContentFieldItemDTO> createDlContentFieldItem(@PathVariable("dlContentFieldId") Long dlContentFieldId,
                                                                          @RequestBody DlContentFieldItemDTO dlContentFieldItemDTO) throws URISyntaxException {
        log.debug("REST request to save DlContentFieldItemDTO : {}", dlContentFieldItemDTO);
        if (dlContentFieldItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlContentFieldItemDTO result = dlContentFieldItemService.save(dlContentFieldItemDTO, dlContentFieldId);
        return ResponseEntity.created(new URI(AdminRestRouter.DlContentFields.DlContentFieldItem.LIST.replace("{dlContentFieldId}", dlContentFieldId.toString()) + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.DlContentFields.DlContentFieldItem.LIST)
    public ResponseEntity<DlContentFieldItemDTO> updateDlContentFieldItem(@PathVariable("dlContentFieldId") Long dlContentFieldId,
                                                                          @RequestBody DlContentFieldItemDTO dlContentFieldItemDTO) throws URISyntaxException {
        log.debug("REST request to update DlContentFieldItemDTO : {}", dlContentFieldItemDTO);
        if (dlContentFieldItemDTO.getId() == null) {
            return createDlContentFieldItem(dlContentFieldId, dlContentFieldItemDTO);
        }
        DlContentFieldItemDTO result = dlContentFieldItemService.save(dlContentFieldItemDTO, dlContentFieldId);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlContentFieldItemDTO.getId().toString()))
                .body(result);
    }

    @GetMapping(AdminRestRouter.DlContentFields.DlContentFieldItem.LIST)
    public ResponseEntity<List<DlContentFieldItemDTO>> getAllDlContentFieldItems(@PathVariable("dlContentFieldId") Long dlContentFieldId,
                                                                                 @RequestParam(value = "parentId", required = false) Long parentId,
                                                                                 Pageable pageable) {
        log.debug("REST request to get a page of DlContentFieldDTO");
        Page<DlContentFieldItemDTO> page = dlContentFieldItemService.findAll(pageable, dlContentFieldId, parentId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.DlContentFields.DlContentFieldItem.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.DlContentFields.DlContentFieldItem.DETAIL)
    public ResponseEntity<DlContentFieldItemDTO> getDlContentFieldItem(@PathVariable Long dlContentFieldId, @PathVariable Long id) {
        log.debug("REST request to get DlContentFieldItemDTO : {}, belonging to DlContentField id: {}", id, dlContentFieldId);
        DlContentFieldItemDTO dlContentFieldItemDTO = dlContentFieldItemService.findOne(id, null);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlContentFieldItemDTO));
    }

    @DeleteMapping(AdminRestRouter.DlContentFields.DlContentFieldItem.DETAIL)
    public ResponseEntity<Void> deleteDlContentFieldItem(@PathVariable Long dlContentFieldId, @PathVariable Long id) {
        log.debug("REST request to delete DlContentFieldDTO : {}, belonging to DlContentField id: {}", id, dlContentFieldId);
        dlContentFieldItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
