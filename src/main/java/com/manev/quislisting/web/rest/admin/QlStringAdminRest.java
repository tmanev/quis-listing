package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.service.filter.QlStringFilter;
import com.manev.quislisting.service.qlml.QlStringService;
import com.manev.quislisting.web.rest.AdminRestRouter;
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
public class QlStringAdminRest {

    private static final String ENTITY_NAME = "qlString";
    private final Logger log = LoggerFactory.getLogger(QlStringAdminRest.class);
    private final QlStringService qlStringService;

    public QlStringAdminRest(QlStringService qlStringService) {
        this.qlStringService = qlStringService;
    }

    @GetMapping(AdminRestRouter.QlString.LIST)
    public ResponseEntity<List<QlString>> getAllQlStrings(QlStringFilter qlStringFilter,
            @PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
        log.debug("REST request to get a page of QlStrings");
        Page<QlString> page = qlStringService.findAllByFilter(qlStringFilter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.QlString.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.QlString.DETAIL)
    public ResponseEntity<QlString> getQlString(@PathVariable Long id) {
        log.debug("REST request to get QlString : {}", id);
        QlString qlString = qlStringService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qlString));
    }

    @PostMapping(AdminRestRouter.QlString.LIST)
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

    @PutMapping(AdminRestRouter.QlString.LIST)
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
