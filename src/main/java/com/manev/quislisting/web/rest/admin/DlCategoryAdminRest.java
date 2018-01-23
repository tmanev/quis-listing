package com.manev.quislisting.web.rest.admin;

import com.manev.quislisting.service.taxonomy.DlCategoryService;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.web.rest.AdminRestRouter;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import com.manev.quislisting.web.rest.util.ResponseUtil;
import com.manev.quislisting.web.rest.vm.BindDlTermTaxonomyVM;
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
public class DlCategoryAdminRest {

    private static final String ENTITY_NAME = "DlCategory";

    private final Logger log = LoggerFactory.getLogger(DlCategoryAdminRest.class);
    private final DlCategoryService dlCategoryService;

    public DlCategoryAdminRest(DlCategoryService dlCategoryService) {
        this.dlCategoryService = dlCategoryService;
    }

    @PostMapping(AdminRestRouter.DlCategory.LIST)
    public ResponseEntity<DlCategoryDTO> createDlCategory(@RequestBody DlCategoryDTO dlCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save DlCategoryDTO : {}", dlCategoryDTO);
        if (dlCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }

        DlCategoryDTO result = dlCategoryService.save(dlCategoryDTO);
        return ResponseEntity.created(new URI(AdminRestRouter.DlCategory.LIST + String.format("/%s", result.getId())))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(AdminRestRouter.DlCategory.LIST)
    public ResponseEntity<DlCategoryDTO> updateDlCategory(@RequestBody DlCategoryDTO dlCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update DlCategoryDTO : {}", dlCategoryDTO);
        if (dlCategoryDTO.getId() == null) {
            return createDlCategory(dlCategoryDTO);
        }
        DlCategoryDTO result = dlCategoryService.save(dlCategoryDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dlCategoryDTO.getId().toString()))
                .body(result);
    }

    @GetMapping(AdminRestRouter.DlCategory.LIST)
    public ResponseEntity<List<DlCategoryDTO>> getAllDlCategories(Pageable pageable, @RequestParam Map<String, String> allRequestParams) {
        log.debug("REST request to get a page of DlCategoryDTO");
        Page<DlCategoryDTO> page = dlCategoryService.findAll(pageable, allRequestParams);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, AdminRestRouter.DlCategory.LIST);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(AdminRestRouter.DlCategory.DETAIL)
    public ResponseEntity<DlCategoryDTO> getDlCategory(@PathVariable Long id) {
        log.debug("REST request to get DlCategoryDTO : {}", id);
        DlCategoryDTO dlCategoryDTO = dlCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlCategoryDTO));
    }

    @GetMapping(AdminRestRouter.DlCategory.DETAIL_BY_TRANSLATION)
    public ResponseEntity<DlCategoryDTO> getDlCategoryByTranslationId(@PathVariable Long id) {
        log.debug("REST request to get DlCategoryDTO by translation id : {}", id);
        DlCategoryDTO dlCategoryDTO = dlCategoryService.findOneByTranslationId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dlCategoryDTO));
    }

    @PostMapping(AdminRestRouter.DlCategory.BIND_CATEGORIES)
    public ResponseEntity<Void> bindDlCategories(@RequestBody BindDlTermTaxonomyVM bindDlTermTaxonomyVM) {
        dlCategoryService.bindDlCategories(bindDlTermTaxonomyVM.getSourceId(), bindDlTermTaxonomyVM.getTargetId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(AdminRestRouter.DlCategory.DETAIL)
    public ResponseEntity<Void> deleteDlCategory(@PathVariable Long id) {
        log.debug("REST request to delete DlCategoryDTO : {}", id);
        dlCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping(AdminRestRouter.DlCategory.ACTIVE_LANGUAGES)
    public List<ActiveLanguageDTO> getActiveLanguages() {
        log.debug("REST request to retrieve active languages for dlCategories : {}");
        return dlCategoryService.findAllActiveLanguages();
    }

}
