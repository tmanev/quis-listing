package com.manev.quislisting.web.rest.post;

import com.manev.quislisting.service.post.QlPageService;
import com.manev.quislisting.service.post.dto.QlPageDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_QL_PAGES;

@RestController
@RequestMapping(RESOURCE_API_ADMIN_QL_PAGES)
public class QlPageResource {

    private static final String ENTITY_NAME = "QlPage";

    private final Logger log = LoggerFactory.getLogger(QlPageResource.class);
    private final QlPageService qlPageService;

    public QlPageResource(QlPageService qlPageService) {
        this.qlPageService = qlPageService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QlPageDTO> createQlPage(@RequestBody QlPageDTO pageDTO) throws URISyntaxException {
        log.debug("REST request to save QlPageDTO : {}", pageDTO);
        if (pageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        QlPageDTO result = qlPageService.save(pageDTO);
        return ResponseEntity.created(new URI(RESOURCE_API_ADMIN_QL_PAGES + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<QlPageDTO> updateQlPage(@RequestBody QlPageDTO pageDTO) throws URISyntaxException {
        log.debug("REST request to update QlPageDTO : {}", pageDTO);
        if (pageDTO.getId() == null) {
            return createQlPage(pageDTO);
        }
        QlPageDTO result = qlPageService.save(pageDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pageDTO.getId().toString()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<QlPageDTO>> getAllPages(Pageable pageable, @RequestParam Map<String, String> allRequestParams) {
        log.debug("REST request to get a page of QlPageDTO");
        Page<QlPageDTO> page = qlPageService.findAll(pageable, allRequestParams);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_ADMIN_QL_PAGES);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QlPageDTO> getQlPage(@PathVariable Long id) {
        log.debug("REST request to get DlListingDTO : {}", id);
        QlPageDTO pageDTO = qlPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQlPage(@PathVariable Long id) {
        log.debug("REST request to delete QlPageDTO : {}", id);
        qlPageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/active-languages")
    public List<ActiveLanguageDTO> getActiveLanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return qlPageService.findAllActiveLanguages();
    }
}
