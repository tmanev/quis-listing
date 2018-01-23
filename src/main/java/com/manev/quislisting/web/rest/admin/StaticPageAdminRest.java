package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.post.StaticPageService;
import com.manev.quislisting.service.post.dto.StaticPageDTO;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.web.rest.AdminRestRouter;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class StaticPageAdminRest {

    private static final String ENTITY_NAME = "QlStaticPage";

    private final Logger log = LoggerFactory.getLogger(StaticPageAdminRest.class);
    private final StaticPageService staticPageService;

    public StaticPageAdminRest(StaticPageService staticPageService) {
        this.staticPageService = staticPageService;
    }

    @PostMapping(AdminRestRouter.StaticPage.LIST)
    public ResponseEntity<StaticPageDTO> createQlPage(@RequestBody StaticPageDTO pageDTO) throws URISyntaxException {
        log.debug("REST request to save StaticPageDTO : {}", pageDTO);
        if (pageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        StaticPageDTO result = staticPageService.save(pageDTO);
        return ResponseEntity.created(new URI(AdminRestRouter.StaticPage.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.StaticPage.LIST)
    public ResponseEntity<StaticPageDTO> updateQlPage(@RequestBody StaticPageDTO pageDTO) throws URISyntaxException {
        log.debug("REST request to update StaticPageDTO : {}", pageDTO);
        if (pageDTO.getId() == null) {
            return createQlPage(pageDTO);
        }
        StaticPageDTO result = staticPageService.save(pageDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pageDTO.getId().toString()))
                .body(result);
    }

    @GetMapping(AdminRestRouter.StaticPage.LIST)
    public ResponseEntity<List<StaticPageDTO>> getAllPages(Pageable pageable, @RequestParam Map<String, String> allRequestParams) {
        log.debug("REST request to get a page of StaticPageDTO");
        Page<StaticPageDTO> page = staticPageService.findAll(pageable, allRequestParams);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.StaticPage.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.StaticPage.DETAIL)
    public ResponseEntity<StaticPageDTO> getQlPage(@PathVariable Long id) {
        log.debug("REST request to get DlListingDTO : {}", id);
        StaticPageDTO pageDTO = staticPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pageDTO));
    }

    @DeleteMapping(AdminRestRouter.StaticPage.DETAIL)
    public ResponseEntity<Void> deleteQlPage(@PathVariable Long id) {
        log.debug("REST request to delete StaticPageDTO : {}", id);
        staticPageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping(AdminRestRouter.StaticPage.ACTIVE_LANGUAGES)
    public List<ActiveLanguageDTO> getActiveLanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return staticPageService.findAllActiveLanguages();
    }
}
