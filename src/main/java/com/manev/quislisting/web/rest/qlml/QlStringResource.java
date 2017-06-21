package com.manev.quislisting.web.rest.qlml;

import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.service.qlml.QlStringService;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import com.manev.quislisting.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_QL_STRINGS;

@RestController
@RequestMapping(RESOURCE_API_ADMIN_QL_STRINGS)
public class QlStringResource {

    private static final String ENTITY_NAME = "qlString";
    private final Logger log = LoggerFactory.getLogger(QlStringResource.class);
    private final QlStringService qlStringService;

    public QlStringResource(QlStringService qlStringService) {
        this.qlStringService = qlStringService;
    }

    @GetMapping
    public ResponseEntity<List<QlString>> getAllQlStrings(@PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
        log.debug("REST request to get a page of QlStrings");
        Page<QlString> page = qlStringService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_ADMIN_QL_STRINGS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QlString> getQlString(@PathVariable Long id) {
        log.debug("REST request to get QlString : {}", id);
        QlString qlString = qlStringService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qlString));
    }

    @PostMapping
    public ResponseEntity<QlString> createQlString(@RequestBody QlString qlString) throws URISyntaxException {
        log.debug("REST request to save QlString : {}", qlString);
        if (qlString.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new qlString cannot already have an ID")).body(null);
        }
        QlString result = qlStringService.save(qlString);
        return ResponseEntity.created(new URI("/api/ql-string/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<QlString> updateQlString(@RequestBody QlString qlString) throws URISyntaxException {
        log.debug("REST request to update QlString : {}", qlString);
        if (qlString.getId() == null) {
            return createQlString(qlString);
        }
        QlString result = qlStringService.save(qlString);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, qlString.getId().toString()))
                .body(result);
    }

}
