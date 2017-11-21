package com.manev.quislisting.web.rest.post;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_DL_LISTINGS;

@RestController
@RequestMapping(RESOURCE_API_DL_LISTINGS)
public class DlListingResource {

    private static final String ENTITY_NAME = "DlListing";

    private final Logger log = LoggerFactory.getLogger(DlListingResource.class);
    private final DlListingService dlListingService;
    private final LocaleResolver localeResolver;

    public DlListingResource(DlListingService dlListingService, LocaleResolver localeResolver) {
        this.dlListingService = dlListingService;
        this.localeResolver = localeResolver;
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

        dlListingService.validateForPublishing(dlListingDTO);
        DlListingDTO result = dlListingService.saveAndApplyForPublishing(dlListingDTO);

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
    public ResponseEntity<List<DlListingDTO>> getAllListings(Pageable pageable,
                                                             @RequestParam Map<String, String> allRequestParams,
                                                             HttpServletRequest request) {
        log.debug("REST request to get a page of DlListingDTO");

        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        log.debug("Language from cookie: {}", language);
        Page<DlListingDTO> page = dlListingService.findAllByLanguageAndUser(pageable, language);
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
}
