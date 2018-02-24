package com.manev.quislisting.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UserService;
import com.manev.quislisting.service.form.DlListingForm;
import com.manev.quislisting.service.form.DlListingPatch;
import com.manev.quislisting.service.mapper.DlListingDtoToDlListingBaseMapper;
import com.manev.quislisting.service.mapper.DlListingDtoToDlListingModelMapper;
import com.manev.quislisting.service.model.DlListingBaseModel;
import com.manev.quislisting.service.model.DlListingModel;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.web.rest.filter.DlListingSearchFilter;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import com.manev.quislisting.web.rest.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DlListingRest {

    private static final String ENTITY_NAME = "DlListing";

    private final Logger log = LoggerFactory.getLogger(DlListingRest.class);
    private final DlListingService dlListingService;
    private final UserService userService;
    private final DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper;
    private final DlListingDtoToDlListingModelMapper dlListingDtoToDlListingModelMapper;

    public DlListingRest(DlListingService dlListingService, UserService userService, DlListingDtoToDlListingBaseMapper dlListingDtoToDlListingBaseMapper, DlListingDtoToDlListingModelMapper dlListingDtoToDlListingModelMapper) {
        this.dlListingService = dlListingService;
        this.userService = userService;
        this.dlListingDtoToDlListingBaseMapper = dlListingDtoToDlListingBaseMapper;
        this.dlListingDtoToDlListingModelMapper = dlListingDtoToDlListingModelMapper;
    }

    @PatchMapping(RestRouter.DlListing.LIST)
    public ResponseEntity<DlListingDTO> updateDlListingPartial(@RequestBody DlListingPatch dlListingPatch) {
        DlListingDTO result;
        switch (dlListingPatch.getPath()) {
            case DESCRIPTION:
                result = dlListingService.updateDescription(dlListingPatch.getValue());
                break;
            case DETAILS:
                result = dlListingService.updateDetails(dlListingPatch.getValue());
                break;
            case LOCATION:
                result = dlListingService.updateLocation(dlListingPatch.getValue());
                break;
            case FEATURED_ATTACHMENT:
                result = dlListingService.updateFeaturedAttachment(dlListingPatch.getValue());
                break;
            case STATUS:
                result = dlListingService.updateStatus(dlListingPatch.getValue());
                break;
                default:
                    return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "Please specify path")).body(null);
        }

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PostMapping(RestRouter.DlListing.LIST)
    public ResponseEntity<DlListingDTO> createDlListing(@RequestBody DlListingForm dlListingForm) throws URISyntaxException {
        log.debug("REST request to save DlListingDTO : {}", dlListingForm);
        if (dlListingForm.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        User loggedUser = getAuthenticatedUser(SecurityUtils.getCurrentUserLogin());
        DlListingDTO result = dlListingService.create(dlListingForm, loggedUser, loggedUser.getLangKey());
        return ResponseEntity.created(new URI(RestRouter.DlListing.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(RestRouter.DlListing.LIST)
    public ResponseEntity<DlListingDTO> updateDlListing(@RequestBody DlListingForm dlListingForm) throws URISyntaxException {
        log.debug("REST request to update DlListingDTO : {}", dlListingForm);
        if (dlListingForm.getId() == null) {
            return createDlListing(dlListingForm);
        }

        DlListingDTO result = dlListingService.update(dlListingForm);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(RestRouter.DlListing.PUBLISH)
    public ResponseEntity<DlListingDTO> updateAndPublish(@RequestBody DlListingForm dlListingForm) {
        log.debug("REST request to publish DlListingDTO : {}", dlListingForm);
        if (dlListingForm.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idnotexists", "Listing must have an ID")).body(null);
        }

        dlListingService.validateForPublishing(dlListingForm);
        DlListingDTO result = dlListingService.publishListing(dlListingForm.getId());

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlListingForm.getId().toString()))
                .body(result);
    }

    @PostMapping(RestRouter.DlListing.UPLOAD)
    public ResponseEntity<List<AttachmentDTO>> handleFileUpload(MultipartRequest multipartRequest, @PathVariable Long id) throws IOException {

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        List<AttachmentDTO> result = dlListingService.uploadFile(fileMap, id);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping(RestRouter.DlListing.LIST)
    public ResponseEntity<List<DlListingDTO>> getAllListings(Pageable pageable,
                                                             @RequestParam(required = false, defaultValue = "en") String languageCode) {
        log.debug("REST request to get a page of DlListingDTO");

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> oneByLogin = userService.findOneByLogin(currentUserLogin);
        if (!oneByLogin.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Page<DlListingDTO> page = dlListingService.findAllByLanguageAndUser(pageable, languageCode, oneByLogin.get());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RestRouter.DlListing.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(RestRouter.DlListing.DETAIL)
    public ResponseEntity<DlListingModel> getDlListing(@PathVariable Long id,
                                                           @RequestParam(required = false, defaultValue = "en") String languageCode) {
        log.debug("REST request to get DlListingDTO : {}", id);
        DlListingDTO dlListingDTO = dlListingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlListingDtoToDlListingModelMapper.convert(dlListingDTO, languageCode)));
    }

    @DeleteMapping(RestRouter.DlListing.DETAIL)
    public ResponseEntity<Void> deleteDlListing(@PathVariable Long id) {
        log.debug("REST request to delete DlListingDTO : {}", id);
        dlListingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping(RestRouter.DlListing.ATTACHMENT_DETAIL)
    public ResponseEntity<Void> deleteDlListingAttachment(@PathVariable Long id, @PathVariable Long attachmentId) {
        log.debug("REST request to delete attachment with id : {} in DlListingDTO : {}", attachmentId, id);
        dlListingService.deleteDlListingAttachment(id, attachmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(RestRouter.DlListing.ACTIVE_LANGUAGES)
    public List<ActiveLanguageDTO> getActiveLanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return dlListingService.findAllActiveLanguages();
    }

    @GetMapping(RestRouter.DlListing.RECENT)
    public ResponseEntity<List<DlListingBaseModel>> getRecentListings(Pageable pageable,
                                                                @RequestParam(required = false, defaultValue = "en") String languageCode) {
        log.debug("REST request to get a page of DlListingDTO");

        Page<DlListingDTO> page = dlListingService.findAllForFrontPage(pageable, languageCode);
        Page<DlListingBaseModel> dlListingBaseModels = page.map(dlListingDTO -> dlListingDtoToDlListingBaseMapper.convert(dlListingDTO, new DlListingBaseModel()));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(dlListingBaseModels, RestRouter.DlListing.RECENT);
        return new ResponseEntity<>(dlListingBaseModels.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(RestRouter.DlListing.SEARCH)
    public ResponseEntity<List<DlListingBaseModel>> searchListings(@RequestParam String query, @ApiParam Pageable pageable,
                                                             @RequestParam(required = false, defaultValue = "en") String languageCode) throws IOException {
        log.debug("REST request to search for a page of Books for query {}", query);

        ObjectMapper mapper = new ObjectMapper();
        DlListingSearchFilter dlListingSearchFilter = mapper.readValue(URLDecoder.decode(query, "UTF-8"), DlListingSearchFilter.class);

        if (dlListingSearchFilter.getLanguageCode() == null) {
            dlListingSearchFilter.setLanguageCode(languageCode);
        }

        Page<DlListingDTO> page = dlListingService.search(dlListingSearchFilter, pageable);
        Page<DlListingBaseModel> dlListingBaseModels = page.map(dlListingDTO -> dlListingDtoToDlListingBaseMapper.convert(dlListingDTO, new DlListingBaseModel()));
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, dlListingBaseModels, RestRouter.DlListing.SEARCH);
        return new ResponseEntity<>(dlListingBaseModels.getContent(), headers, HttpStatus.OK);
    }

    private User getAuthenticatedUser(String login) {
        Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(login);
        if (userWithAuthoritiesByLogin.isPresent()) {
            return userWithAuthoritiesByLogin.get();
        }
        throw new UsernameNotFoundException("User " + login + " was not found in the " +
                "database");
    }
}
