package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_DL_LISTINGS;
import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_DL_LISTINGS;

@RestController
@RequestMapping(RESOURCE_API_ADMIN_DL_LISTINGS)
public class DlAdminListingResource {

    private static final String ENTITY_NAME = "DlListing";

    private final Logger log = LoggerFactory.getLogger(DlAdminListingResource.class);
    private final DlListingService dlListingService;

    public DlAdminListingResource(DlListingService dlListingService) {
        this.dlListingService = dlListingService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DlListingDTO> createDlListing(@RequestBody DlListingDTO dlListingDTO) throws URISyntaxException {
        log.debug("REST request to save DlListingDTO : {}", dlListingDTO);
        if (dlListingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlListingDTO result = dlListingService.save(dlListingDTO);
        return ResponseEntity.created(new URI(RESOURCE_API_DL_LISTINGS + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<DlListingDTO> updateDlListing(@RequestBody DlListingDTO dlListingDTO) throws URISyntaxException {
        log.debug("REST request to update DlListingDTO : {}", dlListingDTO);
        if (dlListingDTO.getId() == null) {
            return createDlListing(dlListingDTO);
        }
        DlListingDTO result = dlListingService.save(dlListingDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @RequestMapping(path = "/publish", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DlListingDTO> updateAndPublish(@RequestBody DlListingDTO dlListingDTO) {
        log.debug("REST request to publish DlListingDTO : {}", dlListingDTO);
        if (dlListingDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idnotexists", "Listing must have an ID")).body(null);
        }

        DlListingDTO result = dlListingService.save(dlListingDTO);
        dlListingService.validateForPublishing(result);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlListingDTO.getId().toString()))
                .body(result);
    }

    @PostMapping(value = "/{id}/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DlListingDTO> handleFileUpload(MultipartRequest multipartRequest, @PathVariable Long id) throws IOException {

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        DlListingDTO result = dlListingService.uploadFile(fileMap, id);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<DlListingDTO>> getAllListings(Pageable pageable, @RequestParam Map<String, String> allRequestParams) {
        log.debug("REST request to get a page of DlListingDTO");
        Page<DlListingDTO> page = dlListingService.findAll(pageable, allRequestParams);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_DL_LISTINGS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DlListingDTO> getDlListing(@PathVariable Long id) {
        log.debug("REST request to get DlListingDTO : {}", id);
        DlListingDTO dlListingDTO = dlListingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlListingDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDlListing(@PathVariable Long id) {
        log.debug("REST request to delete DlListingDTO : {}", id);
        dlListingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping("/{id}/attachments/{attachmentId}")
    public ResponseEntity<DlListingDTO> deleteDlListingAttachment(@PathVariable Long id, @PathVariable Long attachmentId) throws IOException {
        log.debug("REST request to delete attachment with id : {} in DlListingDTO : {}", attachmentId, id);
        DlListingDTO result = dlListingService.deleteDlListingAttachment(id, attachmentId);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @GetMapping("/active-languages")
    public List<ActiveLanguageDTO> getActiveLanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return dlListingService.findAllActiveLanguages();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<DlListingDTO> approveListing(@PathVariable Long id) {
        log.debug("REST request to approve listing with id : {}", id);

        DlListingDTO dlListingDTO = dlListingService.approveListing(id);
        return ResponseEntity.ok().body(dlListingDTO);
    }

    @PutMapping("/{id}/disapprove")
    public ResponseEntity<DlListingDTO> disapproveListing(@PathVariable Long id) {
        log.debug("REST request to disapprove listing with id : {}", id);

        DlListingDTO dlListingDTO = dlListingService.disapproveListing(id);
        return ResponseEntity.ok().body(dlListingDTO);
    }
}
