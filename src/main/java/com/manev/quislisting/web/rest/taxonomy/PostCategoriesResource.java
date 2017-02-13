package com.manev.quislisting.web.rest.taxonomy;

import com.manev.quislisting.service.taxonomy.PostCategoryService;
import com.manev.quislisting.service.taxonomy.dto.PostCategoryDTO;
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

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_POST_CATEGORIES;

@RestController
@RequestMapping(RESOURCE_API_POST_CATEGORIES)
public class PostCategoriesResource {

    private static final String ENTITY_NAME = "PostCategory";

    private final Logger log = LoggerFactory.getLogger(PostCategoriesResource.class);
    private final PostCategoryService postCategoryService;

    public PostCategoriesResource(PostCategoryService postCategoryService) {
        this.postCategoryService = postCategoryService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostCategoryDTO> createPostCategory(@RequestBody PostCategoryDTO postCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save PostCategoryDTO : {}", postCategoryDTO);
        if (postCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new postCategoryDTO cannot already have an ID")).body(null);
        }

        PostCategoryDTO result = postCategoryService.save(postCategoryDTO);
        return ResponseEntity.created(new URI(RESOURCE_API_POST_CATEGORIES + "/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<PostCategoryDTO> updatePostCategory(@RequestBody PostCategoryDTO postCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update PostCategoryDTO : {}", postCategoryDTO);
        if (postCategoryDTO.getId() == null) {
            return createPostCategory(postCategoryDTO);
        }
        PostCategoryDTO result = postCategoryService.save(postCategoryDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postCategoryDTO.getId().toString()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<PostCategoryDTO>> getAllPostCategories(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of PostCategoryDTO");
        Page<PostCategoryDTO> page = postCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_POST_CATEGORIES);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostCategoryDTO> getPostCategory(@PathVariable Long id) {
        log.debug("REST request to get PostCategoryDTO : {}", id);
        PostCategoryDTO postCategoryDTO = postCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postCategoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostCategory(@PathVariable Long id) {
        log.debug("REST request to delete PostCategoryDTO : {}", id);
        postCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
