package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.taxonomy.NavMenuService;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.NavMenuDTO;
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
public class NavMenuAdminRest {

    private static final String ENTITY_NAME = "NavMenu";

    private final Logger log = LoggerFactory.getLogger(NavMenuAdminRest.class);
    private final NavMenuService navMenuService;

    public NavMenuAdminRest(NavMenuService navMenuService) {
        this.navMenuService = navMenuService;
    }

    @PostMapping(AdminRestRouter.NavMenu.LIST)
    public ResponseEntity<NavMenuDTO> createNavMenu(@RequestBody NavMenuDTO navMenuDTO) throws URISyntaxException {
        log.debug("REST request to save PostCategoryDTO : {}", navMenuDTO);
        if (navMenuDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        NavMenuDTO result = navMenuService.save(navMenuDTO);
        return ResponseEntity.created(new URI(AdminRestRouter.NavMenu.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.NavMenu.LIST)
    public ResponseEntity<NavMenuDTO> updateNavMenu(@RequestBody NavMenuDTO navMenuDTO) throws URISyntaxException {
        log.debug("REST request to update NavMenuDTO : {}", navMenuDTO);
        if (navMenuDTO.getId() == null) {
            return createNavMenu(navMenuDTO);
        }
        NavMenuDTO result = navMenuService.save(navMenuDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, navMenuDTO.getId().toString()))
                .body(result);
    }

    @GetMapping(AdminRestRouter.NavMenu.LIST)
    public ResponseEntity<List<NavMenuDTO>> getAllNavMenus(Pageable pageable, @RequestParam Map<String, String> allRequestParams) {
        log.debug("REST request to get a page of NavMenuDTO");
        Page<NavMenuDTO> page = navMenuService.findAll(pageable, allRequestParams);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.NavMenu.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.NavMenu.DETAIL)
    public ResponseEntity<NavMenuDTO> getNavMenu(@PathVariable Long id) {
        log.debug("REST request to get NavMenuDTO : {}", id);
        NavMenuDTO navMenuDTO = navMenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(navMenuDTO));
    }

    @DeleteMapping(AdminRestRouter.NavMenu.DETAIL)
    public ResponseEntity<Void> deleteNavMenu(@PathVariable Long id) {
        log.debug("REST request to delete NavMenuDTO : {}", id);
        navMenuService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping(AdminRestRouter.NavMenu.ACTIVE_LANGUAGES)
    public List<ActiveLanguageDTO> getActiveLanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return navMenuService.findAllActiveLanguages();
    }

}
