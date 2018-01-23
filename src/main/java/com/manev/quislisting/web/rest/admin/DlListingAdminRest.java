package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.dto.ApproveDTO;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DlListingAdminRest {

    private static final String ENTITY_NAME = "DlListing";

    private final Logger log = LoggerFactory.getLogger(DlListingAdminRest.class);
    private final DlListingService dlListingService;
    private final DlListingRebuildService dlListingRebuildService;

    public DlListingAdminRest(DlListingService dlListingService, DlListingRebuildService dlListingRebuildService) {
        this.dlListingService = dlListingService;
        this.dlListingRebuildService = dlListingRebuildService;
    }

    @PostMapping(AdminRestRouter.DlListing.LIST)
    public ResponseEntity<DlListingDTO> createDlListing(@RequestBody DlListingDTO dlListingDTO) throws URISyntaxException {
        log.debug("REST request to save DlListingDTO : {}", dlListingDTO);
        if (dlListingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlListingDTO result = dlListingService.save(dlListingDTO, null);
        return ResponseEntity.created(new URI(AdminRestRouter.DlListing.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.DlListing.LIST)
    public ResponseEntity<DlListingDTO> updateDlListing(@RequestBody DlListingDTO dlListingDTO) throws URISyntaxException {
        log.debug("REST request to update DlListingDTO : {}", dlListingDTO);
        if (dlListingDTO.getId() == null) {
            return createDlListing(dlListingDTO);
        }
        DlListingDTO result = dlListingService.save(dlListingDTO, null);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.DlListing.PUBLISH)
    public ResponseEntity<DlListingDTO> updateAndPublish(@RequestBody DlListingDTO dlListingDTO) {
        log.debug("REST request to publish DlListingDTO : {}", dlListingDTO);
        if (dlListingDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idnotexists", "Listing must have an ID")).body(null);
        }

        DlListingDTO result = dlListingService.save(dlListingDTO, null);
        dlListingService.validateForPublishing(result);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlListingDTO.getId().toString()))
                .body(result);
    }

    @PostMapping(value = AdminRestRouter.DlListing.UPLOAD)
    public ResponseEntity<List<AttachmentDTO>> handleFileUpload(MultipartRequest multipartRequest, @PathVariable Long id) throws IOException {

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        List<AttachmentDTO> result = dlListingService.uploadFile(fileMap, id);

        return ResponseEntity.ok().body(result);
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
        DlListingDTO dlListingDTO = dlListingService.findOne(id, null);
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
}
