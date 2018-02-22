package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.DlAttachmentRegenerateService;
import com.manev.quislisting.service.dto.ApproveDTO;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.rebuildindex.DlListingRebuildService;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DlListingAdminRest {

    private static final String ENTITY_NAME = "DlListing";

    private final Logger log = LoggerFactory.getLogger(DlListingAdminRest.class);
    private final DlListingService dlListingService;
    private final DlListingRebuildService dlListingRebuildService;
    private final DlAttachmentRegenerateService dlAttachmentRegenerateService;

    public DlListingAdminRest(DlListingService dlListingService, DlListingRebuildService dlListingRebuildService, DlAttachmentRegenerateService dlAttachmentRegenerateService) {
        this.dlListingService = dlListingService;
        this.dlListingRebuildService = dlListingRebuildService;
        this.dlAttachmentRegenerateService = dlAttachmentRegenerateService;
    }

    @GetMapping(AdminRestRouter.DlListing.LIST)
    public ResponseEntity<List<DlListingDTO>> getAllListings(Pageable pageable, @RequestParam Map<String, String> allRequestParams) {
        log.debug("REST request to get a page of DlListingDTO");
        Page<DlListingDTO> page = dlListingService.findAll(pageable, allRequestParams);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.DlListing.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.DlListing.DETAIL)
    public ResponseEntity<DlListingDTO> getDlListing(@PathVariable Long id) {
        log.debug("REST request to get DlListingDTO : {}", id);
        DlListingDTO dlListingDTO = dlListingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlListingDTO));
    }

    @DeleteMapping(AdminRestRouter.DlListing.DETAIL)
    public ResponseEntity<Void> deleteDlListing(@PathVariable Long id) {
        log.debug("REST request to delete DlListingDTO : {}", id);
        dlListingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping(AdminRestRouter.DlListing.ATTACHMENT_DETAIL)
    public ResponseEntity<DlListingDTO> deleteDlListingAttachment(@PathVariable Long id, @PathVariable Long attachmentId) {
        log.debug("REST request to delete attachment with id : {} in DlListingDTO : {}", attachmentId, id);
        DlListingDTO result = dlListingService.deleteDlListingAttachment(id, attachmentId);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @GetMapping(AdminRestRouter.DlListing.ACTIVE_LANGUAGES)
    public List<ActiveLanguageDTO> getActiveLanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return dlListingService.findAllActiveLanguages();
    }

    @PutMapping(AdminRestRouter.DlListing.APPROVE)
    public ResponseEntity<DlListingDTO> approveListing(@PathVariable Long id) {
        log.debug("REST request to approve listing with id : {}", id);

        DlListingDTO dlListingDTO = dlListingService.approveListing(id);
        return ResponseEntity.ok().body(dlListingDTO);
    }

    @PutMapping(AdminRestRouter.DlListing.DISAPPROVE)
    public ResponseEntity<DlListingDTO> disapproveListing(@PathVariable Long id, @RequestBody ApproveDTO approveDTO) {
        log.debug("REST request to disapprove listing with id : {}", id);

        DlListingDTO dlListingDTO = dlListingService.disapproveListing(id, approveDTO);
        return ResponseEntity.ok().body(dlListingDTO);
    }

    @GetMapping(AdminRestRouter.DlListing.REBUILD_INDEX)
    public ResponseEntity<Void> rebuildIndex() {
        dlListingRebuildService.rebuildElasticsearchData();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(AdminRestRouter.DlListing.REBUILD_IMAGES)
    public ResponseEntity<Void> rebuildImages() throws IOException {
        dlAttachmentRegenerateService.reGenerateImages();
        return ResponseEntity.noContent().build();
    }
}
