package com.manev.quislisting.web.rest.qlml;

import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.service.dto.EmailNotificationDTO;
import com.manev.quislisting.service.qlml.QlStringService;
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

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_QL_STRINGS;

/**
 * Created by adri on 4/10/2017.
 */
@RestController
@RequestMapping(RESOURCE_API_ADMIN_QL_STRINGS)
public class QlStringResource {

    private static  final String ENTITY_NAME= "qlString";
    private final Logger log = LoggerFactory.getLogger(QlStringResource.class);
    private final QlStringService qlStringService;

    public QlStringResource(QlStringService qlStringService){
        this.qlStringService=qlStringService;
    }


    @GetMapping
    public ResponseEntity<List<QlString>> getAllQlStrings(@PageableDefault(page = 0, value = Integer.MAX_VALUE)Pageable pageable, @RequestParam Map<String, String> allRequestParms)
            throws URISyntaxException {
        log.debug("REST request to get a page of QlStrings");
        Page<QlString> page = qlStringService.findAll(pageable, allRequestParms);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_ADMIN_QL_STRINGS);
        return  new ResponseEntity<List<QlString>>(page.getContent(), headers, HttpStatus.OK);

    }
    @GetMapping("/{id}")
    public ResponseEntity<QlString> getQlString(@PathVariable Long id){
        log.debug("REST request to get QlString : {}", id);
        QlString qlString=qlStringService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qlString));
    }






}
