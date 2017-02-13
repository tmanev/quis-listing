package com.manev.quislisting.web.rest;

import com.manev.quislisting.domain.ContentFieldGroup;
import com.manev.quislisting.service.ContentFieldGroupService;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/content-field-groups")
public class ContentFieldGroupResource {

    private ContentFieldGroupService contentFieldGroupService;

    public ContentFieldGroupResource(ContentFieldGroupService contentFieldGroupService) {
        this.contentFieldGroupService = contentFieldGroupService;
    }

    @GetMapping
    public ResponseEntity<List<ContentFieldGroup>> getAllContentFieldGroups(Pageable pageable) throws URISyntaxException {
        Page<ContentFieldGroup> page = contentFieldGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/admin/content-field-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
