package com.manev.quislisting.web.rest;

import com.manev.quislisting.domain.ContentFieldGroup;
import com.manev.quislisting.service.ContentFieldGroupService;
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
import java.util.Optional;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_CONTENT_FIELD_GROUPS;

@RestController
@RequestMapping(RESOURCE_API_ADMIN_CONTENT_FIELD_GROUPS)
public class ContentFieldGroupResource {
    private static final String ENTITY_NAME = "ContentFieldGroup";
    private final Logger log = LoggerFactory.getLogger(ContentFieldGroupResource.class);
    private ContentFieldGroupService contentFieldGroupService;

    public ContentFieldGroupResource(ContentFieldGroupService contentFieldGroupService) {
        this.contentFieldGroupService = contentFieldGroupService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContentFieldGroup> createContendFieldGroup(@RequestBody ContentFieldGroup contentFieldGroup) throws URISyntaxException {
        log.debug("REST request to save ContentFieldGroup : {}", contentFieldGroup);
        if (contentFieldGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new postCategoryDTO cannot already have an ID")).body(null);
        }

        ContentFieldGroup result = contentFieldGroupService.save(contentFieldGroup);
        return ResponseEntity.created(new URI(RESOURCE_API_ADMIN_CONTENT_FIELD_GROUPS + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<ContentFieldGroup> updateContendFieldGroup(@RequestBody ContentFieldGroup contentFieldGroup) throws URISyntaxException {
        log.debug("REST request to update ContentFieldGroup : {}", contentFieldGroup);
        if (contentFieldGroup.getId() == null) {
            return updateContendFieldGroup(contentFieldGroup);
        }
        ContentFieldGroup result = contentFieldGroupService.save(contentFieldGroup);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contentFieldGroup.getId().toString()))
                .body(result);
    }


    @GetMapping
    public ResponseEntity<List<ContentFieldGroup>> getAllContentFieldGroups(Pageable pageable) {
        Page<ContentFieldGroup> page = contentFieldGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_ADMIN_CONTENT_FIELD_GROUPS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentFieldGroup> getContentFieldGroup(@PathVariable Long id) {
        log.debug("REST request to get ContentFieldGroup : {}", id);
        ContentFieldGroup contentFieldGroup = contentFieldGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contentFieldGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContentFieldGroup(@PathVariable Long id) {
        log.debug("REST request to delete ContentFieldGroup : {}", id);
        contentFieldGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
