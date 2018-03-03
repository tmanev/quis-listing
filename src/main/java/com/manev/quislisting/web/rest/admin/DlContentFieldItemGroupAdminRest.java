package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.DlContentFieldItemGroupService;
import com.manev.quislisting.service.dto.DlContentFieldItemGroupDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class DlContentFieldItemGroupAdminRest {

    private static final String ENTITY_NAME = "DlContentFieldItemGroup";

    private final Logger log = LoggerFactory.getLogger(DlContentFieldItemGroupAdminRest.class);
    private final DlContentFieldItemGroupService dlContentFieldItemGroupService;

    public DlContentFieldItemGroupAdminRest(DlContentFieldItemGroupService dlContentFieldItemGroupService) {
        this.dlContentFieldItemGroupService = dlContentFieldItemGroupService;
    }

    @PostMapping(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.LIST)
    public ResponseEntity<DlContentFieldItemGroupDTO> createDlContentFieldItemGroup(@PathVariable Long dlContentFieldId,
            @RequestBody DlContentFieldItemGroupDTO dlContentFieldItemGroupDTO) throws URISyntaxException {
        log.debug("REST request to save DlContentFieldItemGroupDTO : {}", dlContentFieldItemGroupDTO);
        if (dlContentFieldItemGroupDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlContentFieldItemGroupDTO result = dlContentFieldItemGroupService.save(dlContentFieldItemGroupDTO, dlContentFieldId);
        return ResponseEntity.created(new URI(AdminRestRouter.DlContentFieldGroup.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.LIST)
    public ResponseEntity<DlContentFieldItemGroupDTO> updateDlContentFieldItemGroup(@PathVariable Long dlContentFieldId,
            @RequestBody DlContentFieldItemGroupDTO dlContentFieldItemGroupDTO) throws URISyntaxException {
        log.debug("REST request to update DlContentFieldItemGroupDTO : {}", dlContentFieldItemGroupDTO);
        if (dlContentFieldItemGroupDTO.getId() == null) {
            return createDlContentFieldItemGroup(dlContentFieldId, dlContentFieldItemGroupDTO);
        }
        DlContentFieldItemGroupDTO result = dlContentFieldItemGroupService.save(dlContentFieldItemGroupDTO, dlContentFieldId);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlContentFieldItemGroupDTO.getId().toString()))
                .body(result);
    }

    @GetMapping(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.LIST)
    public ResponseEntity<List<DlContentFieldItemGroupDTO>> getAllDlContentFieldItemGroups(@PathVariable Long dlContentFieldId,
            Pageable pageable) {
        log.debug("REST request to get a page of DlContentFieldItemGroupDTO");
        Page<DlContentFieldItemGroupDTO> page = dlContentFieldItemGroupService.findAll(pageable, dlContentFieldId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.DlContentFieldGroup.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.DETAIL)
    public ResponseEntity<DlContentFieldItemGroupDTO> getDlContentFieldItemGroup(@PathVariable Long dlContentFieldId, @PathVariable Long id) {
        log.debug("REST request to get DlContentFieldItemGroupDTO : {}, belonging to DlContentField id: {}", id, dlContentFieldId);
        DlContentFieldItemGroupDTO group = dlContentFieldItemGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(group));
    }

    @DeleteMapping(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.DETAIL)
    public ResponseEntity<Void> deleteDlContentFieldItemGroup(@PathVariable Long dlContentFieldId, @PathVariable Long id) {
        log.debug("REST request to delete DlContentFieldItemGroupDTO : {}, belonging to DlContentField id: {}", id, dlContentFieldId);
        dlContentFieldItemGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
